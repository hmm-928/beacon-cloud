package com.mashibing.api.filter.impl;

import com.mashibing.api.filter.CheckFilter;
import com.mashibing.api.util.PhoneFormatCheckUtil;
import com.mashibing.common.enums.ExceptionEnums;
import com.mashibing.common.exception.ApiException;
import com.mashibing.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author zjw
 * @description  校验手机号的格式合法性
 */
@Service(value = "mobile")
@Slf4j
public class MobileCheckFilter implements CheckFilter {


    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验手机号】   校验ing…………");
        String mobile = submit.getMobile();
        if(!StringUtils.isEmpty(mobile) && PhoneFormatCheckUtil.isChinaPhone(mobile)){
            // 如果校验进来，代表手机号么得问题
            log.info("【接口模块-校验手机号】   手机号格式合法 mobile = {}",mobile);
            return;
        }
        log.info("【接口模块-校验手机号】   手机号格式不正确 mobile = {}",mobile);
        throw new ApiException(ExceptionEnums.ERROR_MOBILE);
    }
}
