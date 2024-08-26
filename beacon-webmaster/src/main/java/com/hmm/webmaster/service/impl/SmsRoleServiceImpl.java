package com.hmm.webmaster.service.impl;

import com.hmm.webmaster.mapper.SmsRoleMapper;
import com.hmm.webmaster.service.SmsRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author hmm
 * @description
 */
@Service
public class SmsRoleServiceImpl implements SmsRoleService {

    @Resource
    private SmsRoleMapper roleMapper;

    @Override
    public Set<String> getRoleName(Integer userId) {
        Set<String> roleNameSet = roleMapper.findRoleNameByUserId(userId);
        return roleNameSet;
    }
}
