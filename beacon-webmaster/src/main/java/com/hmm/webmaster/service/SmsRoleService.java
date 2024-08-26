package com.hmm.webmaster.service;

import java.util.Set;

/**
 * @author hmm
 * @description
 */
public interface SmsRoleService {
    /**
     * 根据用户id，查询角色名称
     * @param userId
     * @return
     */
    Set<String> getRoleName(Integer userId);
}
