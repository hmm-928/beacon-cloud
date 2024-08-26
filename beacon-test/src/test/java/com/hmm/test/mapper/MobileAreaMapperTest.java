package com.mashibing.test.mapper;

import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.MobileArea;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 10:43
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MobileAreaMapperTest {
    @Autowired
    private MobileAreaMapper mapper;

    @Autowired
    private CacheClient cacheClient;
    @Test
    public void findAll() {
        List<MobileArea> list = mapper.findAll();
        Map map = new HashMap(list.size());
        for (MobileArea mobileArea : list) {
            map.put("phase:" + mobileArea.getMobileNumber(),mobileArea.getMobileArea() + "," + mobileArea.getMobileType());
        }
        cacheClient.pipelineString(map);
    }
}