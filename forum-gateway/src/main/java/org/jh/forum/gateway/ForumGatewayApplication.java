package org.jh.forum.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ForumGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumGatewayApplication.class, args);
    }

}
