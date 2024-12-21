package org.jh.forum.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class SaTokenConfig {
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/api/auth/login")
                .addExclude("/api/user/register")
                .setAuth(obj -> {
                    SaRouter.match("/**", r -> {
                        StpUtil.checkLogin();
                    });
                    String userId = StpUtil.getLoginIdAsString();
                    ServerWebExchange exchange = (ServerWebExchange) SaHolder.getStorage().getSource();
                    ServerHttpRequest modifiedRequest = exchange
                            .getRequest()
                            .mutate()
                            .header("X-User-Id", userId)
                            .build();
                    exchange.mutate().request(modifiedRequest).build();
                })
                .setError(e -> SaResult.error(e.getMessage()));
    }
}