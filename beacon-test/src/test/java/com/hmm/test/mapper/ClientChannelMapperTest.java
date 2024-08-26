package com.mashibing.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.Channel;
import com.mashibing.test.entity.ClientChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientChannelMapperTest {
    @Autowired
    private ClientChannelMapper mapper;
    @Autowired
    private CacheClient cacheClient;
    @Test
    public void findAll() throws JsonProcessingException {
        List<ClientChannel> clientChannelList = mapper.findAll();
        for (ClientChannel clientChannel : clientChannelList ) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientChannel), Map.class);
            cacheClient.sadd("client_channel:" + clientChannel.getClientId(),map);

        }
    }
}