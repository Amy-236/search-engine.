package com.example.searchengine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

/**
 * Gitee API配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gitee")
public class GiteeConfig {
    /**
     * Gitee API基础URL
     */
    private String baseUrl = "https://gitee.com/api/v5";
    
    /**
     * 个人访问令牌
     */
    private String accessToken;
    
    /**
     * 每页数据条数
     */
    private Integer pageSize = 100;
} 