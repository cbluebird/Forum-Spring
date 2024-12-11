package org.jh.forum.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "org.jh.forum")
public class ForumOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumOssApplication.class, args);
    }

}
