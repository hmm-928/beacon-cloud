package com.mashibing.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 状态报告推送等操作时的类
 * @author
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardReport implements Serializable {

    /**
     * 针对当前短信的唯一标识，雪花算法（保留）
     */
    private Long sequenceId;

    /**
     * 客户端ID，基于apikey查询缓存模块得到客户的ID
     */
    private Long clientId;


    /**
     * 客户业务内的uid，客户请求传递的
     */
    private String uid;

    /**
     * 目标手机号，客户请求传递的
     */
    private String mobile;

    /**
     * 短信的发送时间，当前系统时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    /**
     * 短信的发送状态， 0-等待/发送ing，1-成功，2-失败 ，默认情况就是0
     */
    private int reportState;

    /**
     * 短信发送失败的原因是什么，记录在当前属性
     */
    private String errorMsg;

    /**
     *  回调的信息  0需要推送  1不需要推送
     */
    private Integer isCallback;
    private String callbackUrl;
    //失败消息推送失败次数
    private Integer resendCount;
    //为了在网关运营商二次回调时候  方便获取客户状态报告的推送信息
    private String apikey;
    //第二次修改为true
    private Boolean reUpdate;

}