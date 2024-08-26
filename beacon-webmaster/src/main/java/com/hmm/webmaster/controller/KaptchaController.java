package com.hmm.webmaster.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.hmm.common.constant.WebMasterConstants;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author hmm
 * @description
 */
@Controller
public class KaptchaController {

    private final String JPG = "jpg";

    @Autowired
    private DefaultKaptcha kaptcha;

    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse resp){
        //1、验证码图片不需要做存储和缓存
        resp.setHeader("Cache-Control","no-store, no-cahe");
        //2、设置响应头信息
        resp.setContentType("image/jpg");
        //3、生成验证码文字
        String text = kaptcha.createText();
        // 认证需要验证验证码的准确性，基于Shiro将text做存储
        SecurityUtils.getSubject().getSession().setAttribute(WebMasterConstants.KAPTCHA,text);
        //4、基于文字生成对应的图片
        BufferedImage image = kaptcha.createImage(text);
        //5、写回验证码图片信息
        try {
            ServletOutputStream outputStream = resp.getOutputStream();
            ImageIO.write(image,JPG,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
