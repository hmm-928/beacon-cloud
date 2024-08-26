package com.hmm.webmaster.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsMenuServiceTest {

    @Autowired
    private SmsMenuService menuService;

    @Test
    public void findUserMenu() {
        List<Map<String, Object>> data = menuService.findUserMenu(1);
        for (Map<String, Object> parentMenu : data) {
            System.out.println(parentMenu.get("name"));
            List<Map<String, Object>> sonMenuList = (List<Map<String, Object>>) parentMenu.get("list");
            for (Map<String, Object> sonMenu : sonMenuList) {
                System.out.println(sonMenu);
            }
            System.out.println("======================");
        }
    }
}