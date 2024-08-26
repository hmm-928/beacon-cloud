package com.hmm.webmaster.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsRoleMapperTest {

    @Autowired
    private SmsRoleMapper roleMapper;

    @Test
    public void findRoleNameByUserId() {
        Set<String> set = roleMapper.findRoleNameByUserId(1);
        System.out.println(set);
    }
}