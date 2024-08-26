package com.mashibing.common.exception;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:48
 */

import com.mashibing.common.enums.ExceptionEnums;
import lombok.Getter;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:48
 */

@Getter
public class ApiException extends RuntimeException {

    private Integer code;

    public ApiException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public ApiException(ExceptionEnums enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
    }

}
