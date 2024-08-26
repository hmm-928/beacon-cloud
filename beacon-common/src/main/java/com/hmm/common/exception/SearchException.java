package com.mashibing.common.exception;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:48
 */

import com.mashibing.common.enums.ExceptionEnums;
import lombok.Getter;

/**
 * 搜索模块异常类
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:48
 */
@Getter
public class SearchException extends RuntimeException {

    private Integer code;

    public SearchException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public SearchException(ExceptionEnums enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
    }

}
