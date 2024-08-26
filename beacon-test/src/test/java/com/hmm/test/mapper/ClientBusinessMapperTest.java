package com.mashibing.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.ClientBusiness;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 18:42
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientBusinessMapperTest {


    @Autowired
    private ClientBusinessMapper mapper;
    @Autowired
    private CacheClient cacheClient;

/*- 客户信息：采用hash结构
- key：client_business:apikey
- value：用户信息的json*/

    @Test
    public void findById()  throws JsonProcessingException {
        ClientBusiness cb = mapper.findById(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(cb), Map.class);
        cacheClient.hmset("client_business:"+cb.getApikey(),map);
        System.out.println(cb);
    }
}