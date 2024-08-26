package com.hmm.webmaster.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsMenuMapperTest {

    @Resource
    private SmsMenuMapper menuMapper;

    @Test
    public void findMenuByUserId() {
        List<Map<String, Object>> list = menuMapper.findMenuByUserId(1);
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
    }
}