package com.zm.geteway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义过滤器，只能在代码中配置，如果需要在配置文件中配置，继承GatewayFilterFactory工厂
 */
public class ManagerFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("======我是个自定义过滤器=======");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
