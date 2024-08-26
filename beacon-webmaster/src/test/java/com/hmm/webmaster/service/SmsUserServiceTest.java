package com.hmm.webmaster.service;

import com.hmm.webmaster.entity.SmsUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUserServiceTest {

    @Autowired
    private SmsUserService smsUserService;

    @Test
    public void findByUsername() {
        SmsUser smsUser = smsUserService.findByUsername("admin");
        System.out.println(smsUser);
    }
}