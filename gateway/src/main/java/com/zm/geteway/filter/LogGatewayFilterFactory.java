package com.zm.geteway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class LogGatewayFilterFactory extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {

    public LogGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("enabled");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("进入自定义过滤器");
            if(!config.enabled){
                //不需要记录日志
                return chain.filter(exchange);
            }
            return chain.filter(exchange).then(
//                Mono.defer(() ->{
//                    ServerHttpResponse response = exchange.getResponse();
//                    if(HttpStatus.TOO_MANY_REQUESTS == response.getStatusCode()) {
//                        return Mono.error(new RuntimeException());
//                    } else {
//                        return Mono.empty();
//                    }
//                })
            );
        };
    }

    /**
     * 配置参数类
     */
    public static class Config {

        //是否开启日志记录
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
