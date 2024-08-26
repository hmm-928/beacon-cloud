package com.hmm.webmaster.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author hmm
 * @description
 */
@FeignClient(value = "beacon-search")
public interface SearchClient {


    /**
     * 去搜索模块查询短信记录
     * @param parameters
     * @return   Map，    total: 数据条数   rows：list集合，放着需要展示的数据
     */
    @PostMapping("/search/sms/list")
    Map<String,Object> findSmsByParameters(@RequestBody Map<String,Object> parameters);



}
