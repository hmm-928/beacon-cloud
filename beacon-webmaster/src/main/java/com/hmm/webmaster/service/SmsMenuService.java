package com.hmm.webmaster.service;

import java.util.List;
import java.util.Map;

/**
 * 菜单Service
 * @author hmm
 * @description
 */
public interface SmsMenuService {
    /**
     * 根据用户id查询用户菜单信息
     * @param id 用户Id
     * @return
     */
    List<Map<String, Object>> findUserMenu(Integer id);
}
