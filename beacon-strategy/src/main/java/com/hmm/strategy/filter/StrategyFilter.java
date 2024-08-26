package com.mashibing.strategy.filter;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/14 22:43
 */

import com.hmm.common.model.StandardSubmit;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/14 22:43
 */

public interface StrategyFilter {

    /**
     * 校验！！！！
     * @param submit
     */
    void strategy(StandardSubmit submit);
}
