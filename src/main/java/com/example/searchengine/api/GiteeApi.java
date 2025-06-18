package com.example.searchengine.api;

import com.example.searchengine.model.Repository;
import java.util.List;

/**
 * Gitee API接口定义
 */
public interface GiteeApi {
    /**
     * 搜索用户
     * @param keyword 搜索关键词
     * @return 用户名称列表
     */
    List<String> searchUsers(String keyword);

    /**
     * 获取用户的仓库列表
     * @param username 用户名
     * @return 仓库列表
     */
    List<Repository> getUserRepositories(String username);

    /**
     * 获取组织的仓库列表
     * @param orgName 组织名称
     * @return 仓库列表
     */
    List<Repository> getOrgRepositories(String orgName);

    /**
     * 获取仓库的README信息
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @return 包含README的仓库信息
     */
    String getRepositoryReadme(String owner, String repo);
} 