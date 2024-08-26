package com.hmm.webmaster.config;

import com.hmm.webmaster.realm.ShiroRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hmm
 * @description
 */
@Configuration
public class ShiroConfig {


    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm){
        //1、构建安全管理器
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //2、设置Realm进去
        securityManager.setRealm(shiroRealm);

        //3、返回安全管理器
        return securityManager;
    }

    /**
     * 设置过滤器链的规则。
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        //1、 构建ShiroFilterChainDefinition实现类
        DefaultShiroFilterChainDefinition shiroFilter = new DefaultShiroFilterChainDefinition();

        //2、配置上过滤器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // anon代表放行，使用的是AnonymousFilter
        filterChainDefinitionMap.put("/public/**","anon");
        filterChainDefinitionMap.put("/captcha.jpg","anon");
        filterChainDefinitionMap.put("/sys/login","anon");
        filterChainDefinitionMap.put("/index.html","anon");
        filterChainDefinitionMap.put("/login.html","anon");
        filterChainDefinitionMap.put("/logout","logout");
        filterChainDefinitionMap.put("/**","authc");
        // 设置
        shiroFilter.addPathDefinitions(filterChainDefinitionMap);

        //3、返回配置好的过滤器链
        return shiroFilter;
    }

}
