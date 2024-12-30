package org.jh.forum.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Result;
import org.jh.forum.common.constant.FeignConstant;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                InputStream bodyStream = response.body().asInputStream();
                Result<?> result = objectMapper.readValue(bodyStream, Result.class);
                if (response.status() == 400) {
                    return new BizException(ErrorCode.fromCode(result.getCode()));
                } else if (response.status() == 500) {
                    return new SystemException(ErrorCode.fromCode(result.getCode()));
                }
            } catch (IOException e) {
                return new SystemException(ErrorCode.FEIGN_ERROR, e);
            }
            return new SystemException(ErrorCode.FEIGN_ERROR);
        };
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (requestTemplate) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    if ("content-length".equals(headerName)) {
                        continue;
                    }
                    requestTemplate.header(headerName, request.getHeader(headerName));
                }
                if (request.getHeader(FeignConstant.F_REQUEST_ID) == null) {
                    String uuid = String.valueOf(UUID.randomUUID());
                    requestTemplate.header(FeignConstant.F_REQUEST_ID, uuid);
                }
            }
        };
    }
}