package com.zm.geteway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class RequestRateLimiterConfig {

    @Bean
    @Primary
    KeyResolver uriKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
    }
}
