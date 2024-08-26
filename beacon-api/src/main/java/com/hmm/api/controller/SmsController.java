package com.mashibing.api.controller;

import com.mashibing.api.filter.CheckFilterContext;
import com.mashibing.api.form.SingleSendForm;
import com.mashibing.api.util.R;
import com.mashibing.api.vo.ResultVO;
import com.mashibing.common.constant.RabbitMQConstants;
import com.mashibing.common.enums.ExceptionEnums;
import com.mashibing.common.model.StandardSubmit;
import com.mashibing.common.util.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author zjw
 * @description
 */
@RestController
@RequestMapping("/sms")
@Slf4j
@RefreshScope
public class SmsController {

    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 客户端IP地址的请求头信息，多个用','隔开。
     */
    @Value("${headers}")
    private String headers;

    /**
     * 基于请求头获取信息时，可能获取到的未知信息
     */
    private final String UNKNOW = "unknow";

    /**
     * 如果是当前请求头获取IP地址，需要截取到第一个','未知
     */
    private final String X_FORWARDED_FOR = "x-forwarded-for";


    @Autowired
    private CheckFilterContext checkFilterContext;

    /**
     * 单条验证短信接口
     * @param singleSendForm
     * @param bindingResult
     * @param req
     * @return
     */
    @PostMapping(value = "/single_send",produces = "application/json;charset=utf-8")
    public ResultVO singleSend(@RequestBody @Validated SingleSendForm singleSendForm, BindingResult bindingResult, HttpServletRequest req){
        //1. 校验参数
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.info("【接口模块-单条短信Controller】 参数不合法 msg = {}",msg);
            return R.error(ExceptionEnums.PARAMETER_ERROR.getCode(),msg);
        }
        //=========================获取真实的IP地址=========================================
        String ip = this.getRealIP(req);

        //=========================构建StandardSubmit，各种封装校验=========================================
        StandardSubmit submit = new StandardSubmit();
        submit.setRealIP(ip);
        submit.setApikey(singleSendForm.getApikey());
        submit.setMobile(singleSendForm.getMobile());
        submit.setText(singleSendForm.getText());
        submit.setState(singleSendForm.getState());
        submit.setUid(singleSendForm.getUid());

        //=========================调用策略模式的校验链=========================================
        checkFilterContext.check(submit);

        //========================基于雪花算法生成唯一id，并添加到StandardSubmit对象中，并设置发送时间=========================================
        submit.setSequenceId(snowFlakeUtil.nextId());
        submit.setSendTime(LocalDateTime.now());

        //=========================发送到MQ，交给策略模块处理=========================================
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PRE_SEND,submit,new CorrelationData(submit.getSequenceId().toString()));

        // =====================没有问题，返回接收成功===============================
        return R.ok();
    }

    /**
     * 获取客户端真实的IP地址
     * @param req
     * @return
     */
    private String getRealIP(HttpServletRequest req) {
        //1. 声明返回的ip地址
        String ip;

        //2. 遍历请求头，并且通过req获取ip地址
        for (String header : headers.split(",")) {
            // 健壮性校验
            if (!StringUtils.isEmpty(header)) {
                // 基于req获取ip地址
                ip = req.getHeader(header);
                // 如果获取到的ip不为null，不为空串，并且不为unknow，就可以返回
                if (!StringUtils.isEmpty(ip) && !UNKNOW.equalsIgnoreCase(ip)) {
                    // 判断请求头是否是x-forwarded-for
                    if (X_FORWARDED_FOR.equalsIgnoreCase(header) && ip.indexOf(",") > 0) {
                        ip = ip.substring(0,ip.indexOf(","));
                    }
                    // 返回IP地址
                    return ip;
                }
            }
        }

        //3. 如果请求头都没有获取到IP地址，直接基于传统的方式获取一个IP
        return req.getRemoteAddr();
    }


}
