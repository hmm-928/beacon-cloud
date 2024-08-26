package com.mashibing.test.mapper;

import com.mashibing.test.entity.ClientBusiness;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 14:56
 */
public interface ClientBusinessMapper {

    @Select("select * from client_business where id = #{id}")
    ClientBusiness findById(@Param("id") Long id);

}