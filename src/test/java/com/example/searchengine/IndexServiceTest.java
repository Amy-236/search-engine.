package com.example.searchengine;

import com.example.searchengine.service.IndexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SearchEngineApplication.class)
public class IndexServiceTest {

    @Autowired
    private IndexService indexService;

    @Test
    public void buildIndex() {
        indexService.buildIndex();
    }

    @Test
    public void loadIndex() {
        indexService.loadIndex();
    }

    @Test
    public void search() {
        indexService.loadIndex();
        indexService.search("项目介绍").forEach(repositoryEntity -> {
            System.out.print(repositoryEntity.getId()+","+repositoryEntity.getFullName());
            System.out.println();
        });
    }
}
