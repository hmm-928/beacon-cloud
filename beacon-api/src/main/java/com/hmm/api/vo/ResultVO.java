package com.mashibing.api.vo;

import lombok.Data;

/**
 * @author zjw
 * @description
 */
@Data
public class ResultVO {

    private Integer code;

    private String msg;

    private Integer count;

    private Long fee;

    private String uid;

    private String sid;
}
