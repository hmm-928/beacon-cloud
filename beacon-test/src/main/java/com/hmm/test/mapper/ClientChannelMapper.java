package com.mashibing.test.mapper;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 16:37
 */

import com.mashibing.test.entity.Channel;
import com.mashibing.test.entity.ClientChannel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 16:37
 */

public interface ClientChannelMapper {
    @Select("select * from  client_channel where is_delete = 0")
    List<ClientChannel> findAll();
}
