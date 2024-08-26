package com.mashibing.test.mapper;

import com.mashibing.test.client.CacheClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 15:54
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MobileDirtyWordMapperTest {
    @Autowired
    private MobileDirtyWordMapper mapper;

    @Autowired
    private CacheClient cacheClient;
    @Test
    public void findDirtyword() {
        List<String> dirtywords = mapper.findDirtyword();
        cacheClient.saddStr("dirty_word:",dirtywords.toArray(new String[]{}));
    }
}