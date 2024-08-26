package com.mashibing.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应前端数据的基本结构
 * @author hmm
 * @description
 */
@Data
@NoArgsConstructor
public class ResultVO {

    private Integer code;

    private String msg;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Object data;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Long total;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Object rows;


    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
