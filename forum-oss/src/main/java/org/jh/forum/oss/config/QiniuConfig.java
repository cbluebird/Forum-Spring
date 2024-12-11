package org.jh.forum.oss.config;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss.qiniu")
public class QiniuConfig {
    private String domain;
    private String path;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

    @Bean
    public UploadManager uploadManager() {
        Configuration cfg = new Configuration(Region.autoRegion());
        return new UploadManager(cfg);
    }
}
