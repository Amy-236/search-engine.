package com.example.searchengine.api.impl;

import com.example.searchengine.api.GiteeApi;
import com.example.searchengine.config.GiteeConfig;
import com.example.searchengine.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Gitee API实现类
 */
@Service
@RequiredArgsConstructor
public class GiteeApiImpl implements GiteeApi {
    private final GiteeConfig giteeConfig;
    private final RestTemplate restTemplate;

    @Override
    public List<String> searchUsers(String keyword) {
        List<String> users = new ArrayList<>();
        int page = 1;
        boolean hasMore = true;

        while (hasMore) {
            String url = UriComponentsBuilder.fromHttpUrl(giteeConfig.getBaseUrl() + "/search/users")
                    .queryParam("q", keyword)
                    .queryParam("access_token", giteeConfig.getAccessToken())
                    .queryParam("page", page)
                    .queryParam("per_page", giteeConfig.getPageSize())
                    .build()
                    .toUriString();

            List<Map<String, Object>> items = restTemplate.getForObject(url, List.class);
            
            if (items == null || items.isEmpty()) {
                hasMore = false;
            } else {
                items.forEach(item -> users.add((String) item.get("login")));
                page++;
            }
        }

        return users;
    }

    @Override
    public List<Repository> getUserRepositories(String username) {
        List<Repository> repositories = new ArrayList<>();
        int page = 1;
        boolean hasMore = true;

        while (hasMore) {
            String url = UriComponentsBuilder.fromHttpUrl(giteeConfig.getBaseUrl() + "/users/" + username + "/repos")
                    .queryParam("access_token", giteeConfig.getAccessToken())
                    .queryParam("page", page)
                    .queryParam("per_page", giteeConfig.getPageSize())
                    .build()
                    .toUriString();

            List<Map<String, Object>> items = restTemplate.getForObject(url, List.class);
            
            if (items == null || items.isEmpty()) {
                hasMore = false;
            } else {
                items.forEach(item -> {
                    Repository repo = new Repository();
                    repo.setRepositoryId(Long.valueOf(item.get("id").toString()));
                    repo.setFullName((String) item.get("full_name"));
                    repo.setHtmlUrl((String) item.get("html_url"));
                    repositories.add(repo);
                });
                page++;
            }
        }

        return repositories;
    }

    @Override
    public List<Repository> getOrgRepositories(String orgName) {
        List<Repository> repositories = new ArrayList<>();
        int page = 1;
        boolean hasMore = true;

        while (hasMore) {
            String url = UriComponentsBuilder.fromHttpUrl(giteeConfig.getBaseUrl() + "/orgs/" + orgName + "/repos")
                    .queryParam("access_token", giteeConfig.getAccessToken())
                    .queryParam("page", page)
                    .queryParam("per_page", giteeConfig.getPageSize())
                    .build()
                    .toUriString();

            List<Map<String, Object>> items = restTemplate.getForObject(url, List.class);
            
            if (items == null || items.isEmpty()) {
                hasMore = false;
            } else {
                items.forEach(item -> {
                    Repository repo = new Repository();
                    repo.setRepositoryId(Long.valueOf(item.get("id").toString()));
                    repo.setFullName((String) item.get("full_name"));
                    repo.setHtmlUrl((String) item.get("html_url"));
                    repositories.add(repo);
                });
                page++;
            }
        }

        return repositories;
    }

    @Override
    public String getRepositoryReadme(String owner, String repo) {
        String url = UriComponentsBuilder.fromHttpUrl(giteeConfig.getBaseUrl() + "/repos/" + owner + "/" + repo + "/readme")
                .queryParam("access_token", giteeConfig.getAccessToken())
                .build()
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        return new String(Base64.getDecoder().decode((String) response.get("content")));
    }
} 