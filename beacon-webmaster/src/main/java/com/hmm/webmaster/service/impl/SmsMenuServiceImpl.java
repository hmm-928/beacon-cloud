package com.hmm.webmaster.service.impl;

import com.hmm.webmaster.mapper.SmsMenuMapper;
import com.hmm.webmaster.service.SmsMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author hmm
 * @description
 */
@Service
public class SmsMenuServiceImpl implements SmsMenuService {

    @Resource
    private SmsMenuMapper menuMapper;


    @Override
    public List<Map<String, Object>> findUserMenu(Integer id) {
        //1、将多表查询的结果直接映射，只查询type为0和type为1的数据， 查询到的结果顺序，是type正序排序
        List<Map<String, Object>> list = menuMapper.findMenuByUserId(id);

        //2、封装外层的父级菜单封装到当前的List集合
        List<Map<String, Object>> data = new ArrayList<>();
        //3、使用迭代器遍历所有的菜单信息，封装父级菜单
        ListIterator<Map<String, Object>> parentIterator = list.listIterator();
        while (parentIterator.hasNext()) {
            Map<String, Object> menu = parentIterator.next();
            if ((int) menu.get("type") == 0) {
                // 是父级菜单
                data.add(menu);
                parentIterator.remove();
            } else {
                break;
            }
        }
        //4、存放二级菜单
        for (Map<String, Object> parentMenu : data) {
            List<Map<String, Object>> sonMenuList = new ArrayList<>();
            ListIterator<Map<String, Object>> sonIterator = list.listIterator();
            while (sonIterator.hasNext()) {
                Map<String, Object> sonMenu = sonIterator.next();
                if (Long.valueOf(parentMenu.get("id").toString()).equals(Long.valueOf(sonMenu.get("parentId").toString()))){
                    sonMenuList.add(sonMenu);
                    sonIterator.remove();
                }
            }
            parentMenu.put("list", sonMenuList);
        }
        //5、返回data
        return data;
    }
}
