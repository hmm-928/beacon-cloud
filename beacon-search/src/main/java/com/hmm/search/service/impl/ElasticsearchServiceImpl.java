package com.mashibing.search.service.impl;


import com.mashibing.common.constant.RabbitMQConstants;
import com.mashibing.common.enums.ExceptionEnums;
import com.mashibing.common.exception.SearchException;
import com.mashibing.common.model.StandardReport;
import com.mashibing.search.service.SearchService;
import com.mashibing.search.utils.SearchUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author zjw
 * @description
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements SearchService {
    /**
     * 添加成功的result
     */
    private final String CREATED = "created";

    /**
     * 修改成功的result
     */
    private final String UPDATED = "updated";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void index(String index, String id, String json) throws IOException {
        //1、构建插入数据的Request
        IndexRequest request = new IndexRequest();

        //2、给request对象封装索引信息，文档id，以及文档内容
        request.index(index);
        request.id(id);
        request.source(json, XContentType.JSON);

        //3、将request信息发送给ES服务
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        //4、校验添加是否成功
        String result = response.getResult().getLowercase();
        if (!CREATED.equals(result)) {
            // 添加失败！！
            log.error("【搜索模块-写入数据失败】 index = {},id = {},json = {},result = {}", index, id, json, result);
            throw new SearchException(ExceptionEnums.SEARCH_INDEX_ERROR);
        }
        log.info("【搜索模块-写入数据成功】 索引添加成功index = {},id = {},json = {},result = {}", index, id, json, result);
    }

    @Override
    public boolean exists(String index, String id) throws IOException {
        // 构建GetRequest，查看索引是否存在
        GetRequest request = new GetRequest();

        // 指定索引信息，还有文档id
        request.index(index);
        request.id(id);

        // 基于restHighLevelClient将查询指定id的文档是否存在的请求投递过去。
        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);

        // 直接返回信息
        return exists;
    }

    @Override
    public void update(String index, String id, Map<String, Object> doc) throws IOException {
        //1、基于exists方法，查询当前文档是否存在
        boolean exists = exists(index, id);
        if (!exists) {
            // 当前文档不存在
            StandardReport report = SearchUtils.get();
            if (report.getReUpdate()) {
                // 第二次获取投递的消息，到这已经是延迟20s了。
                log.error("【搜索模块-修改日志】 修改日志失败，report = {}", report);
            } else {
                // 第一次投递，可以再次将消息仍会MQ中
                // 开始第二次消息的投递了
                report.setReUpdate(true);
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_GATEWAY_NORMAL_QUEUE, report);
            }
            SearchUtils.remove();
            return;
        }
        //2、到这，可以确认文档是存在的，直接做修改操作
        UpdateRequest request = new UpdateRequest();

        request.index(index);
        request.id(id);
        request.doc(doc);

        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        String result = update.getResult().getLowercase();
        if (!UPDATED.equals(result)) {
            // 添加失败！！
            log.error("【搜索模块-修改日志失败】 index = {},id = {},doc = {}", index, id, doc);
            throw new SearchException(ExceptionEnums.SEARCH_UPDATE_ERROR);
        }
        log.info("【搜索模块-修改日志成功】 文档修改成功index = {},id = {},doc = {}", index, id, doc);

    }

    @Override
    public Map<String, Object> findSmsByParameters(Map<String, Object> parameters) throws IOException {
        //1、声明SearchRequest(后期需要根据传递的时间指定查询哪些索引，如果没传，可以指定默认查询前三个月)
        SearchRequest request = new SearchRequest(SearchUtils.getCurrYearIndex(), "");

        //2、封装查询条件
        //2.1 参数全部取出来
        Object fromObj = parameters.get("from");
        Object sizeObj = parameters.get("size");
        Object contentObj = parameters.get("content");
        Object mobileObj = parameters.get("mobile");
        Object startTimeObj = parameters.get("starttime");
        Object stopTimeObj = parameters.get("stoptime");
        Object clientIDObj = parameters.get("clientID");

        //2.2 clientID需要单独操作一下。
        List<Long> clientIDList = null;
        if (clientIDObj instanceof List) {
            // 传递的是个集合
            clientIDList = (List) clientIDObj;
        } else if (!ObjectUtils.isEmpty(clientIDObj)) {
            clientIDList = Collections.singletonList(Long.parseLong(clientIDObj + ""));
        }
        //2.3 条件封装
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // ========================封装查询条件到boolQuery========================================
        //2.3.1、关键字
        if (!ObjectUtils.isEmpty(contentObj)) {
            boolQuery.must(QueryBuilders.matchQuery("text", contentObj));
            // 高亮。设置给sourceBuilder
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("text");
            highlightBuilder.preTags("<span style='color: red'>");
            highlightBuilder.postTags("</span>");
            highlightBuilder.fragmentSize(100);
            sourceBuilder.highlighter(highlightBuilder);
        }

        //2.3.2、手机号
        if (!ObjectUtils.isEmpty(mobileObj)) {
            boolQuery.must(QueryBuilders.prefixQuery("mobile", (String) mobileObj));
        }

        //2.3.3、开始时间
        if(!ObjectUtils.isEmpty(startTimeObj)){
            boolQuery.must(QueryBuilders.rangeQuery("sendTime").gte(startTimeObj));
        }

        //2.3.4、结束时间
        if(!ObjectUtils.isEmpty(stopTimeObj)){
            boolQuery.must(QueryBuilders.rangeQuery("sendTime").lte(stopTimeObj));
        }

        //2.3.5、客户id
        if(clientIDList != null){
            boolQuery.must(QueryBuilders.termsQuery("clientId",clientIDList.toArray(new Long[]{})));
        }

        //2.3.6 分页查询
        // 确保 fromObj 和 sizeObj 不是 null，并且是有效的数字
        int from = (fromObj != null) ? Integer.parseInt(fromObj.toString()) : 0; // 默认值为 0
        int size = (sizeObj != null) ? Integer.parseInt(sizeObj.toString()) : 10; // 默认值为 10

        sourceBuilder.from(from);
        sourceBuilder.size(size);


        // ========================封装查询条件到boolQuery========================================
        sourceBuilder.query(boolQuery);
        request.source(sourceBuilder);

        //3、执行查询
        SearchResponse resp = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        //4、封装数据
        long total = resp.getHits().getTotalHits().value;
        List<Map> rows = new ArrayList<>();
        for (SearchHit hit : resp.getHits().getHits()) {
            Map<String, Object> row = hit.getSourceAsMap();
            List<Object> sendTime = (List<Object>) row.get("sendTime");

            // 这里确保 sendTime 不为 null
            if (sendTime != null) {

                String sendTimeStr = listToDateString(sendTime);
                row.put("sendTimeStr", sendTimeStr);

            }

            row.put("corpname", row.get("sign"));

            // 高亮结果的处理
            HighlightField highlightField = hit.getHighlightFields().get("text");
            if(highlightField != null){
                String textHighLight = highlightField.getFragments()[0].toString();
                row.put("text",textHighLight);
            }
            rows.add(row);
        }
        //5、返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

    private String listToDateString(List<Object> sendTime) {


        // 确保第一个元素不为 null，并获取年份
        String year = (sendTime.get(0) != null) ? sendTime.get(0).toString() : "0000"; // 默认值为 "0000"

        // 获取其他时间元素，并进行类型检查
        Integer monthInt = (sendTime.get(1) instanceof Integer) ? (Integer) sendTime.get(1) : 0;
        Integer dayInt = (sendTime.get(2) instanceof Integer) ? (Integer) sendTime.get(2) : 0;
        Integer hourInt = (sendTime.get(3) instanceof Integer) ? (Integer) sendTime.get(3) : 0;
        Integer minuteInt = (sendTime.get(4) instanceof Integer) ? (Integer) sendTime.get(4) : 0;
        Integer secondInt = (sendTime.get(5) instanceof Integer) ? (Integer) sendTime.get(5) : 0;

        // 毫秒可选择性使用，如果不需要可以忽略
        Long milliInt = (sendTime.get(6) instanceof Long) ? (Long) sendTime.get(6) : 0L;

        // 格式化时间字符串
        String month = (monthInt < 10 ? "0" : "") + monthInt;
        String day = (dayInt < 10 ? "0" : "") + dayInt;
        String hour = (hourInt < 10 ? "0" : "") + hourInt;
        String minute = (minuteInt < 10 ? "0" : "") + minuteInt;
        String second = (secondInt < 10 ? "0" : "") + secondInt;

        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }


}
