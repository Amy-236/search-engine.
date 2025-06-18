package com.example.searchengine;

import com.example.searchengine.api.GiteeApi;
import com.example.searchengine.model.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SearchEngineApplication.class)
public class GiteeApiTest {

    @Autowired
    private GiteeApi giteeApi;

    /**
     * 搜索用户
     */
    @Test
    public void searchUsers() {
        giteeApi.searchUsers("bite").forEach(name -> System.out.println(name));
    }

    /**
     * 获取用户的仓库列表
     */
    @Test
    public void getUserRepositories() {
        giteeApi.getUserRepositories("CornPitcher").forEach(repository -> {
            System.out.print(repository.getFullName()+","+repository.getHtmlUrl());
            System.out.println();
        });
    }

    /**
     * 获取组织的仓库列表
     */
    @Test
    public void getOrgRepositories() {
        giteeApi.getOrgRepositories("dromara").forEach(repository -> {
            System.out.print(repository.getFullName()+","+repository.getHtmlUrl());
            System.out.println();
        });
    }

    /**
     * 获取仓库的README信息
     */
    @Test
    public void getRepositoryReadme() {
        System.out.println(giteeApi.getRepositoryReadme("dromara", "warm-flow-test"));
    }
}
