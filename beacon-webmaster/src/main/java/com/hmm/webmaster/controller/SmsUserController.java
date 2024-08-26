package com.hmm.webmaster.controller;

import com.hmm.common.constant.WebMasterConstants;
import com.hmm.common.enums.ExceptionEnums;
import com.hmm.common.util.R;
import com.hmm.common.vo.ResultVO;
import com.hmm.webmaster.dto.UserDTO;
import com.hmm.webmaster.entity.SmsUser;
import com.hmm.webmaster.service.SmsMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证，注册等基于用户的操作接口
 *
 * @author hmm
 * @description
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsUserController {

    @Autowired
    private SmsMenuService menuService;

    /**
     * 登录功能
     *
     * @param userDTO       接收用户登录信息
     * @param bindingResult 校验参数
     * @return
     */
    @PostMapping("/login")
    public ResultVO login(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
//        * 1、请求参数的非空校验
        if (bindingResult.hasErrors()) {
            // 参数不合法，响应对应的JSON信息
            log.info("【认证操作】参数不合法，userDTO = {}", userDTO);
            return R.error(ExceptionEnums.PARAMETER_ERROR);
        }
//        * 2、基于验证码校验请求是否合理
        String realKaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(WebMasterConstants.KAPTCHA);
        if (!userDTO.getCaptcha().equalsIgnoreCase(realKaptcha)) {
            log.info("【认证操作】验证码不正确，kapacha = {}，realKaptcha = {}", userDTO.getCaptcha(), realKaptcha);
            return R.error(ExceptionEnums.KAPACHA_ERROR);
        }
//        * 3、基于用户名和密码做Shiro的认证操作
        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(), userDTO.getPassword());
        token.setRememberMe(userDTO.getRememberMe());
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
//        * 4、根据Shiro的认证，返回响应信息
            log.info("【认证操作】用户名或密码错误，ex = {}", e.getMessage());
            return R.error(ExceptionEnums.AUTHEN_ERROR);
        }
        // 到这，代表认证成功
        return R.ok();
    }


    /**
     * 查询登录用户的信息
     *
     * @return
     */
    @GetMapping("/user/info")
    public ResultVO info() {
        //1、基于SecurityUtils获取用户信息
        Subject subject = SecurityUtils.getSubject();
        SmsUser smsUser = (SmsUser) subject.getPrincipal();
        if (smsUser == null) {
            log.info("【获取登录用户信息】 用户未登录！！");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }

        //2、封装结果返回
        Map<String, Object> data = new HashMap<>();
        data.put("nickname", smsUser.getNickname());
        data.put("username", smsUser.getUsername());
        return R.ok(data);
    }

    /**
     * 查询当前登录用户的菜单信息
     * @return
     */
    @GetMapping("/menu/user")
    public ResultVO menuUser() {
        // 基于用户的id，根据角色表信息查询到菜单表中的详细内容
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        if (smsUser == null) {
            log.info("【获取用户菜单信息】 用户未登录！！");
            return R.error(ExceptionEnums.NOT_LOGIN);
        }
        // 封装为具体的下述的这种结构
        List<Map<String, Object>> data = menuService.findUserMenu(smsUser.getId());
        if (data == null){
            log.error("【获取用户菜单信息】 查询用户菜单失败！！ id = {}",smsUser.getId());
            return R.error(ExceptionEnums.USER_MENU_ERROR);
        }
        // 返回结果
        return R.ok(data);
    }
}
