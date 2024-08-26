package com.mashibing.api.advice;

import com.mashibing.api.util.R;
import com.mashibing.api.vo.ResultVO;
import com.mashibing.common.exception.ApiException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zjw
 * @description
 */
@RestControllerAdvice
public class ApiExceptionHandler{

    @ExceptionHandler(ApiException.class)
    public ResultVO apiException(ApiException ex){
        return R.error(ex);
    }


}
