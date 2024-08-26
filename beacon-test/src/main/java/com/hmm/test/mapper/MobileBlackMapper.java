package com.mashibing.test.mapper;

import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.MobileBlack;
import org.apache.ibatis.annotations.Select;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

public interface MobileBlackMapper {
    @Select("select black_number,client_id from mobile_black where is_delete = 0")
    List<MobileBlack> findAll();
    // =========================================

}