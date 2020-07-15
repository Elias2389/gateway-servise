package com.ae.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("algun_codigo_secreto_aeiou")
    private String jwtKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(getTokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/api/products/product",
                        "/api/items/item",
                        "/api/users/user")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/product/{id}",
                        "/api/items/item/{id}/count/{count}",
                        "/api/users/user/id/{id}").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/products/**", "/api/items/**", "/api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().cors().configurationSource(getCorsConfigurationCors());
    }

    @Bean
    public CorsConfigurationSource getCorsConfigurationCors() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("*"));
        corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "OPTIONS"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource webCors = new UrlBasedCorsConfigurationSource();
        webCors.registerCorsConfiguration("/**", corsConfig);
        return webCors;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> getCorsFilter() {
        FilterRegistrationBean<CorsFilter> filter =
                new FilterRegistrationBean<CorsFilter>(new CorsFilter(getCorsConfigurationCors()));
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }

    @Bean
    public JwtTokenStore getTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(jwtKey);
        return tokenConverter;
    }
}
