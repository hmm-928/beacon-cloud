package com.mashibing.test.mapper;

import com.mashibing.test.entity.MobileArea;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MobileDirtyWordMapper {

    @Select("select dirtyword  from mobile_dirtyword ")
    List<String> findDirtyword();

}