package org.jh.forum.search.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "meilisearch")
public class MeilisearchConfig {
    private String hostUrl;
    private String apiKey;

    @Bean
    public Client client() {
        Config cfg = new Config(hostUrl, apiKey);
        return new Client(cfg);
    }
}