package com.hmm.webmaster.service.impl;

import com.hmm.webmaster.entity.ClientBusiness;
import com.hmm.webmaster.service.ClientBusinessService;
import com.hmm.webmaster.entity.ClientBusinessExample;
import com.hmm.webmaster.mapper.ClientBusinessMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hmm
 * @description
 */
@Service
public class ClientBusinessServiceImpl implements ClientBusinessService {

    @Resource
    private ClientBusinessMapper clientBusinessMapper;

    @Override
    public List<ClientBusiness> findAll() {
        List<ClientBusiness> list = clientBusinessMapper.selectByExample(null);
        return list;
    }

    @Override
    public List<ClientBusiness> findByUserId(Integer userId) {
        ClientBusinessExample example = new ClientBusinessExample();
        example.createCriteria().andExtend1EqualTo(userId + "");
        List<ClientBusiness> list = clientBusinessMapper.selectByExample(example);
        return list;
    }
}
