package com.example.searchengine.api.impl;

import com.example.searchengine.api.GiteeApi;
import com.example.searchengine.api.RepositoryService;
import com.example.searchengine.entity.RepositoryEntity;
import com.example.searchengine.mapper.RepositoryMapper;
import com.example.searchengine.model.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 仓库服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryServiceImpl implements RepositoryService {
    private final GiteeApi giteeApi;
    private final RepositoryMapper repositoryMapper;

    @Override
    public int fetchAndSaveRepositories(String username) {
        // 1. 获取用户仓库列表
        List<Repository> repositories = giteeApi.getUserRepositories(username);
        
        // 2. 如果用户仓库为空，尝试获取组织仓库
        if (repositories.isEmpty()) {
            log.info("用户 {} 的仓库列表为空，尝试获取组织仓库", username);
            repositories = giteeApi.getOrgRepositories(username);
        }

        if (repositories.isEmpty()) {
            log.warn("用户/组织 {} 没有任何仓库", username);
            return 0;
        }

        // 3. 获取每个仓库的README并转换为实体
        List<RepositoryEntity> entities = new ArrayList<>();
        for (Repository repo : repositories) {
            try {
                // 解析仓库全名获取仓库名
                String repoName = repo.getFullName().split("/")[1];
                
                // 获取README内容
                String readme = giteeApi.getRepositoryReadme(username, repoName);
                
                // 转换为实体
                RepositoryEntity entity = new RepositoryEntity();
                entity.setFullName(repo.getFullName());
                entity.setHtmlUrl(repo.getHtmlUrl());
                entity.setRepositoryId(repo.getRepositoryId());
                entity.setReadme(readme);
                
                entities.add(entity);
                
                log.info("成功获取仓库 {} 的README", repo.getFullName());
            } catch (Exception e) {
                log.error("获取仓库 {} 的README失败: {}", repo.getFullName(), e.getMessage());
            }
        }

        // 4. 批量保存到数据库
        if (!entities.isEmpty()) {
            int count = repositoryMapper.batchInsert(entities);
            log.info("成功保存 {} 个仓库信息到数据库", count);
            return count;
        }

        return 0;
    }
} 