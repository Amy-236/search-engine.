package com.example.searchengine;

import com.example.searchengine.entity.RepositoryEntity;
import com.example.searchengine.mapper.RepositoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SearchEngineApplication.class)
public class MysqlTest {

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Test
    public void insert() {
        RepositoryEntity repository = new RepositoryEntity();
        repository.setFullName("bite/java");
        repository.setRepositoryId(10000L);
        repository.setHtmlUrl("www.bite.com");
        repository.setReadme("hello bite!");
        repositoryMapper.insert(repository);
    }
}
