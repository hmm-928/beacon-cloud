package com.mashibing.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.ClientSign;
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
 * Create by 2024/7/11 19:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientSignMapperTest {

    @Autowired
    private ClientSignMapper mapper;
    @Autowired
    private CacheClient cacheClient;
    @Test
    public void findByClientId() {
        List<ClientSign> list = mapper.findByClientId(1L);
        for (ClientSign clientSign : list) {
            System.out.println(clientSign);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> value = list.stream().map(cs -> {
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(cs), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        cacheClient.sadd("client_sign:1",value.toArray(new Map[]{}));
    }
}