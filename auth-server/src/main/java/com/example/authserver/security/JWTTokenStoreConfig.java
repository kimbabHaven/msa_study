package com.example.authserver.security;

import com.example.authserver.config.CustomConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 인증 서버가 JWT 토큰을 생성, 서명, 해석하는 방법 지정
 */
@Configuration
public class JWTTokenStoreConfig {

    private final CustomConfig customConfig;

    public JWTTokenStoreConfig(CustomConfig customConfig) {
        this.customConfig = customConfig;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 서비스에 전달된 토큰에서 데이터를 읽는데 사용
     * @return
     */
    @Bean
    @Primary        // 특정 타입의 빈이 둘 이상인 경우 (여기선 DefaultTokenServices) @Primary 로 지정된 타입을 자동 주입
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    /**
     * JWT 와 OAuth2 인증 서버 사이의 변환기
     * 토큰 서명에 사용되는 서명키 사용 (여기선 대칭 키)
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(customConfig.getJwtSigningKey());      // 토큰 서명에 사용되는 서명키 정의
        return converter;
    }

    /**
     * OAuth2 에 JWT 토큰 확장 클래스인 JWTTokenEnhancer 클래스를 사용한다고 알리기 위해 빈으로 노출
     * 여기서 노출하면 JWTOAuth2Config 에서 사용 가능
     * @return
     */
    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return new JWTTokenEnhancer();
    }
}