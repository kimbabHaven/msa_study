package com.example.oauth2server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* 사용자 ID, 패스워드, 역할 정의
 * AuthenticationManager을 사용하려면 WebSecurityConfigurerAdapter 를 상속받는 클래스 생성 후
 * authenticationManagerBean 메소드를 오버라이드한 후 Bean 으로 등록 후 사용
*/
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    @Bean       // 스프링 시큐리티가 인증 처리하는데 사용
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean       // 스프링 시큐리티에서 반환될 사용자 정보 저장
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("assuUser").password(passwordEncoder.encode("user1234")).roles("USER")
                .and()
                .withUser("assuAdmin").password(passwordEncoder.encode("admin1234")).roles("USER", "ADMIN");
    }
}