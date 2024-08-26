package com.hmm.webmaster.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hmm
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSmsVO {
    // ok 公司名称
    private String corpname;

    // 格式待定
    private String sendTimeStr;

    // 修改页面
    private Integer reportState;

    // 全网通改成未知
    private Integer operatorId;

    // 原errorCode
    private String errorMsg;

    // ok
    private String srcNumber;
    // ok
    private String mobile;
    // ok
    private String text;
}
