package com.mashibing.common.util;

import com.mashibing.common.enums.ExceptionEnums;
import com.mashibing.common.vo.ResultVO;

import javax.xml.transform.Result;

/**
 * 封装ResultVO的工具
 * @author hmm
 * @description
 */
public class R {

    /**
     * 成功，无数据
     * @return
     */
    public static ResultVO ok(){
        return new ResultVO(0,"");
    }

    /**
     * 成功，有数据
     * @return
     */
    public static ResultVO ok(Object data){
        ResultVO vo = ok();
        vo.setData(data);
        return vo;
    }

    /**
     * 成功，有数据
     * @return
     */
    public static ResultVO ok(Long total ,Object rows){
        ResultVO vo = ok();
        vo.setTotal(total);
        vo.setRows(rows);
        return vo;
    }

    /**
     * 失败，指定错误信息
     * @param enums
     * @return
     */
    public static ResultVO error(ExceptionEnums enums){
        return new ResultVO(enums.getCode(),enums.getMsg());
    }



}
