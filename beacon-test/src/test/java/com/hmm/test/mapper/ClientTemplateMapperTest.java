package com.mashibing.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.ClientTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientTemplateMapperTest {

    @Autowired
    private ClientTemplateMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    public void findBySignId() {
        List<ClientTemplate> ct1 = mapper.findBySignId(15L);
        List<ClientTemplate> ct2 = mapper.findBySignId(24L);
        for (ClientTemplate clientTemplate : ct1) {
            System.out.println(clientTemplate);
        }
        //  ct2在现有的库中没有数据
        System.out.println(ct2);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> value = ct1.stream().map(ct -> {
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(ct), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());


        cacheClient.sadd("client_template:15",value.toArray(new Map[]{}));
    }
}