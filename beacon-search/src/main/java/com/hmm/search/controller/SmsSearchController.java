package com.mashibing.search.controller;

import com.mashibing.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author zjw
 * @description
 */
@RestController
public class SmsSearchController {

    @Autowired
    private SearchService searchService;


    @PostMapping("/search/sms/list")
    public Map<String,Object> findSmsByParameters(@RequestBody Map<String,Object> parameters) throws IOException {
        //1、调用搜索模块完成查询,因为ES在查询数据时，会返回总条数
        Map<String,Object> result = searchService.findSmsByParameters(parameters);

        //2、返回数据
        return result;
    }
}
