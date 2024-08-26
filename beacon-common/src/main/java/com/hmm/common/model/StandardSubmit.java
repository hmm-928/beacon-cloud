package com.mashibing.common.model;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/10 10:46
 */

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/10 10:46
 * 因为在接口模块只接收到了客户发送过来的基本信息，还需要去封装很多内容，比如短信是哪个客户发送的，
 * 费用是多少，选择的哪个通道等等信息，都需要封装出来。而且这个信息要从头到尾贯穿到运营商。
 * 后面在记录短信发送信息到Elasticsearch时，
 * 也需要使用当前的Submit作为构建Elasticsearch索引的主要模板。
 *
 * 为了保证整体内容采用同一个POJO类，构建了一个beacon-common模块，用于专门封装这种公共的信息。
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardSubmit implements Serializable {

    /**
     * 针对当前短信的唯一标识
     */
    private Long sequenceId;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 客户端的ip白名单  缓存
     */
    private List<String> ip;

    /**
     * 客户业务内的uid  客户传递的
     */
    private String uid;

    /**
     * 目标手机号 客户传递的
     */
    private String mobile;

    /**
     * 短信内容的签名
     */
    private String sign;

    /**
     * 短信内容
     */
    private String text;


    /**
     * 短信的发送时间，当前系统时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    /**
     * 当前短信的费用  计算文字  70字一条  超出 67一条
     */
    private Long fee;

    /**
     * 目标手机号的运营商  （策略模块）
     */
    private Integer operatorId;


    /**
     * 目标手机号的归属地区号  0451  0455 （策略模块）三方查询不到 忽略
     */
    private Integer areaCode;

    /**
     * 目标手机号的归属地  哈尔滨，  绥化~ （策略模块）
     */
    private String area;

    /**
     * 通道下发的源号码  106934985673485645（策略模块）
     */
    private String srcNumber;

    /**
     * 通道的id信息（策略模块）
     */
    private Long channelId;

    /**
     * 短信的发送状态， 0-等待ing，1-成功，2-失败 默认0
     */
    private int reportState;
    /**
     * 短信发送失败的原因
     */
    private String errorMsg;
    /**
     * 真实ip
     */
    private String realIP;
    /**
     * 客户端请求携带的apiKey
     */
    private String apikey;

    /**
     * 短信类型 0短信验证码 1通知类 2营销类
     */
    private int state;
    /**
     * 签名的id
     */
    private Long signId;
    //是否携号转网

    private Boolean isTransfer;
    /**
     * 一小时限流
     */
    private Long oneHourLimitMilli;

    // 后续再做封装~~~~


}