package com.vallim.payments.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/payments", "/webhooks", "/api-mock/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//                .authorizeHttpRequests(a -> a
//                        .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
//                        .anyRequest().authenticated()
//                ).build();
//    }

}
