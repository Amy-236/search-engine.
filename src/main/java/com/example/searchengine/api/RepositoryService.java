package com.example.searchengine.api;

import java.util.List;

/**
 * 仓库服务接口
 */
public interface RepositoryService {
    /**
     * 获取并保存用户或组织的仓库信息
     * @param username 用户名或组织名
     * @return 保存的仓库数量
     */
    int fetchAndSaveRepositories(String username);
} 