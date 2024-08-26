package com.hmm.webmaster.mapper;

import com.hmm.webmaster.entity.SmsUser;
import com.hmm.webmaster.entity.SmsUserExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsUserMapper {

    long countByExample(SmsUserExample example);

    int deleteByExample(SmsUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsUser row);

    int insertSelective(SmsUser row);

    List<SmsUser> selectByExample(SmsUserExample example);

    SmsUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SmsUser row, @Param("example") SmsUserExample example);

    int updateByExample(@Param("row") SmsUser row, @Param("example") SmsUserExample example);

    int updateByPrimaryKeySelective(SmsUser row);

    int updateByPrimaryKey(SmsUser row);
}