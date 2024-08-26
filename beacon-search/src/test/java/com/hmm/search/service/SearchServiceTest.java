package com.mashibing.search.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 20:21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchServiceTest {
    @Autowired
    private  SearchService searchService;
    @Test
    public void index() throws IOException {
        searchService.index("sms_submit_log_2024","1","{\"clientId\":2}");
    }
}