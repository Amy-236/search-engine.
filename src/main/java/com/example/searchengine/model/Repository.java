package com.example.searchengine.model;

import lombok.Data;

/**
 * 仓库信息模型类
 */
@Data
public class Repository {
    /**
     * 仓库ID
     */
    private Long repositoryId;
    
    /**
     * 仓库完整名称（owner/repo）
     */
    private String fullName;
    
    /**
     * 仓库HTML访问地址
     */
    private String htmlUrl;
    
    /**
     * 仓库README内容
     */
    private String readme;
} 