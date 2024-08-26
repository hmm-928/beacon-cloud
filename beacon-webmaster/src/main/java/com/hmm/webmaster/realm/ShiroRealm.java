package com.hmm.webmaster.realm;

import com.hmm.webmaster.entity.SmsUser;
import com.hmm.webmaster.service.SmsUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义Realm
 * @author hmm
 * @description
 */
@Component
public class ShiroRealm extends AuthorizingRealm {




    {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        this.setCredentialsMatcher(credentialsMatcher);
    }

    @Autowired
    private SmsUserService userService;

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1、基于token拿到用户名
        String username = (String) token.getPrincipal();

        //2、基于用户名获取用户信息(模拟数据库操作)
        SmsUser smsUser = userService.findByUsername(username);

        //3、查询完毕后，查看用户是否为null，为null就直接返回即可
        if(smsUser == null){
            // 用户名错误
            return null;
        }

        //4、不为null，说明用户名正确，封装AuthenticationInfo返回即可,设置密码加密方式和信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(smsUser,smsUser.getPassword(),"shiroRealm");
        info.setCredentialsSalt(ByteSource.Util.bytes(smsUser.getSalt()));

        //5、返回
        return info;
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}
