package com.mashibing.strategy.filter;

import com.hmm.common.constant.CacheConstant;
import com.hmm.common.model.StandardSubmit;
import com.mashibing.strategy.client.BeaconCacheClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
//策略模块校验链的执行
@Component
@Slf4j
public class StrategyFilterContext {

    // 泛型注入，拿到所有的校验信息。
    @Autowired
    private Map<String,StrategyFilter> stringStrategyFilterMap;

    // 注入CacheClient
    @Autowired
    private BeaconCacheClient cacheClient;

    private final String CLIENT_FILTERS = "clientFilters";


    /**
     * 当前check方法用于管理校验链的顺序
     */
    public void strategy(StandardSubmit submit) {
        //1、 基于Redis获取客户对应的校验信息
        String filters = cacheClient.hget(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), CLIENT_FILTERS);

        //2、健壮性校验后，基于逗号分隔遍历
        String[] filterArray;
        if(filters != null && (filterArray = filters.split(",")).length > 0){
            // 到这，filterArray不为null，并且有数据
            for (String strategy : filterArray) {
                //3、 遍历时，从stringStrategyFilterMap中获取到需要执行的校验信息，执行
                StrategyFilter strategyFilter = stringStrategyFilterMap.get(strategy);
                if(strategyFilter != null){
                    strategyFilter.strategy(submit);
                }
            }
        }
    }

}