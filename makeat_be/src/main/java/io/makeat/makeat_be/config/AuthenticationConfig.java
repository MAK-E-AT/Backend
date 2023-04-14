package io.makeat.makeat_be.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.GET, "/user").permitAll()
                                .anyRequest().authenticated()
//                        .and()
//                        .sessionManagement()
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용하는 경우에 씀
                ).build();

    }
}
