package com.zm.geteway;

import com.zm.geteway.filter.ManagerFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.zm")
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        //添加自定义过滤器路由
        return builder.routes()
                .route(r -> r.path("/api-test/**")
                        .filters(f -> f.filter(new ManagerFilter())
                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar").stripPrefix(1))
                        .uri("lb://zm-manager")
                        .order(1)
                        .id("customer_filter_router")
                )
                .build();
    }
}
