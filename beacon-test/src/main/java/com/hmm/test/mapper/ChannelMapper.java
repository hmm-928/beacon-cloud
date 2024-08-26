package com.mashibing.test.mapper;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 16:37
 */

import com.mashibing.test.entity.Channel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 16:37
 */

public interface ChannelMapper {
    @Select("select * from  channel where is_delete = 0")
    List<Channel> findAll();
}
