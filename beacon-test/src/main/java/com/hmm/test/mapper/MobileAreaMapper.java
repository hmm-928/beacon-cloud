package com.mashibing.test.mapper;

import com.mashibing.test.entity.ClientBalance;
import com.mashibing.test.entity.MobileArea;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MobileAreaMapper {

    @Select("select mobile_number,mobile_area,mobile_type from mobile_area  ")
    List<MobileArea> findAll();

}