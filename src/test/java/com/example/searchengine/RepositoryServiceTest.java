package com.example.searchengine;

import com.example.searchengine.api.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SearchEngineApplication.class)
public class RepositoryServiceTest {

    @Autowired
    private RepositoryService repositoryService;


    @Test
    public void fetchAndSaveRepositories() {
        repositoryService.fetchAndSaveRepositories("xiaojianzi");
    }

}
