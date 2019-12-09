package com.zm.geteway.filter;

import com.zm.common.utils.RedisUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *  全局验证请求带token
 */
@Component
public class TokenFilter implements GlobalFilter, Ordered {
    public static Logger log = Logger.getLogger(TokenFilter.class);
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        log.info("请求地址：" + uri);
        String token = exchange.getRequest().getHeaders().getFirst("sign");
        if(StringUtils.isEmpty(token)){
            token = exchange.getRequest().getQueryParams().getFirst("sign");
        }

        String user_code = (String) redisUtil.get(token);
        if(StringUtils.isEmpty(user_code)){
            //token 不存在
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
