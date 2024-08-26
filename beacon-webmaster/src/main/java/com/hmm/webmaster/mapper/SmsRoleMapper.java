package com.hmm.webmaster.mapper;

import com.hmm.webmaster.entity.SmsRole;
import com.hmm.webmaster.entity.SmsRoleExample;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SmsRoleMapper {
    long countByExample(SmsRoleExample example);

    int deleteByExample(SmsRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsRole row);

    int insertSelective(SmsRole row);

    List<SmsRole> selectByExample(SmsRoleExample example);

    SmsRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SmsRole row, @Param("example") SmsRoleExample example);

    int updateByExample(@Param("row") SmsRole row, @Param("example") SmsRoleExample example);

    int updateByPrimaryKeySelective(SmsRole row);

    int updateByPrimaryKey(SmsRole row);

    @Select("select \n" +
            "\tname\n" +
            "from \n" +
            "\tsms_role sr\n" +
            "inner join \n" +
            "\tsms_user_role sur\n" +
            "on\n" +
            "\tsr.id = sur.user_id\n" +
            "where \n" +
            "  sur.user_id = #{userId}")
    Set<String> findRoleNameByUserId(@Param("userId") Integer userId);
}