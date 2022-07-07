package com.example.apigateway.filter;

import com.example.apigateway.config.CustomConfig;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.view.RequestContext;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public static final String CORRELATION_ID = "assu-correlation-id";
    public static final String AUTH_TOKEN = "Authorization";

    public GlobalFilter() {
        super(Config.class);
    }

    @Autowired
    private CustomConfig customConfig;

    /**
     * 모든 라우터가 실행되어질 때 실행됨
     */
    @Override
    public GatewayFilter apply(Config config) {
        //filter에서 하고 싶은 내용을 재정의
        //pre filter 동작
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage: {}, {}", config.getBaseMessage(), request.getRemoteAddress());

            if (isCorrelationIdPresent(request)) {
                // 헤더에 assu-correlation-id 가 있다면
                log.debug("============ assu-correlation-id found in pre filter: {}. ", getCorrelationId(request));
            } else {
                request.mutate().header(CORRELATION_ID, UUID.randomUUID().toString()).build();
                // 헤더에 assu-correlation-id 가 없다면 상관관계 ID 생성하여 RequestContext 의 addZuulRequestHeader 로 추가
                //setCorrelationId(request, UUID.randomUUID().toString());
            }

            if (config.isPreLogger()) {
                log.info("Global Filter Start: request id -> {}", request.getId());
                log.info("============ user id is {}.",  getUserId(request));
            }

            //post filter 동작
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("Global Filter End: response code -> {}", response.getStatusCode());
                    log.info("Global Filter End: URI!!! -> {}", request.getURI());
                }
            }));
        });
    }
    //@Component
    //public class AuthorizationFilter implements WebFilter {
    //    @Override
    //    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    //        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, "Bearer " + authHeader).build();
    //        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
    //        return chain.filter(mutatedExchange);
    //    }
    //}

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    private String getUserId(ServerHttpRequest request) {
        String result = "";
        String auth = request.getHeaders().getFirst(AUTH_TOKEN);
        log.debug("check auth_token : {} ==========================================", auth);

        if (auth != null) {
            // HTTP Authorization 헤더에서 토큰 파싱
            String authToken = auth.replace("Bearer ", "");
            try {
                // 토큰 서명에 사용된 서명 키를 전달해서 Jwts 클래스를 사용해 토큰 파싱
                Claims claims = Jwts.parser()
                        .setSigningKey(customConfig.getJwtSigningKey().getBytes("UTF-8"))
                        .parseClaimsJws(authToken).getBody();
                // JWT 토큰에서 userId 가져옴 (userId 는 인증 서버의 JWTTokenEnhancer 에서 추가했음)
                result = (String) claims.get("userId");
                // {user_name=assuAdmin, scope=[mobileclient], exp=1601582137, userId=12345, authorities=[ROLE_ADMIN, ROLE_USER], jti=595aa7f9-7887-4263-85b1-20aa3555ffd2, client_id=assuapp}
                log.info("claims: {}", claims);
            } catch (SignatureException e) {
                log.error("Invalid JWT signature: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                log.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.error("JWT claims string is empty: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Exception : {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * HTTP 헤더에서 assu-correlation-id 조회
     */
    public String getCorrelationId(ServerHttpRequest request) {
        return request.getHeaders().getFirst(CORRELATION_ID);
//        String correlation = request.getHeaders().getFirst(CORRELATION_ID);
//
//        if (correlation != null) {
//            // assu-correlation-id 가 이미 설정되어 있다면 해당값 리턴
//            return correlation;
//        } else {
//            // 헤더에 없다면 ZuulRequestHeaders 확인
//            // 주울은 유입되는 요청에 직접 HTTP 요청 헤더를 추가하거나 수정하지 않음
//            return ctx.getZuulRequestHeaders().get(CORRELATION_ID);
//        }
    }


    /**
     * HTTP 요청 헤더에 상관관계 ID 추가
     *      이때 RequestContext 에 addZuulRequestHeader() 메서드로 추가해야 함
     *
     *      이 메서드는 Zuul 서버의 필터를 지나는 동안 추가되는 별도의 HTTP 헤더 맵을 관리하는데
     *      ZuulRequestHeader 맵에 보관된 데이터는 Zuul 서버가 대상 서비스를 호출할 때 합쳐짐
     * @param correlationId
     */
    public void setCorrelationId(ServerHttpRequest request, String correlationId) {
        log.info(correlationId + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        request.getHeaders().add(CORRELATION_ID, correlationId);
    }

    /**
     * 헤더에 assu-correlation-id 가 있는지 확인
     */
    private boolean isCorrelationIdPresent(ServerHttpRequest request) {
        if (getCorrelationId(request) != null) {
            return true;
        }
        return false;
    }

}
