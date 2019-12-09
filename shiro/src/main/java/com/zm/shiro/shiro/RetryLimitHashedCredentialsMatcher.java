package com.zm.shiro.shiro;

import com.zm.common.utils.RedisUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * shiro之密码输入次数限制6次，并锁定2分钟
 * @author Administrator
 *
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 在回调方法doCredentialsMatch(AuthenticationToken token,AuthenticationInfo info)中进行身份认证的密码匹配，
     * </br>这里我们引入了redis用于保存用户登录次数，如果登录失败retryCount变量则会一直累加，如果登录成功，那么这个count就会从缓存中移除，
     * </br>从而实现了如果登录次数超出指定的值就锁定。
     * @param token
     * @param info
     * @return
     */
    @Override  
    public boolean doCredentialsMatch(AuthenticationToken token,  
            AuthenticationInfo info) {
        //获取登录用户名
        String username = (String) token.getPrincipal();  
        //从redis中获取密码输错次数
        // retryCount
        int retryCount = 0;
        if(redisUtil.hasKey(username)){
            //存在错误次数
            retryCount = (int) redisUtil.get(username);
        }else{
            redisUtil.set(username, retryCount);
        }

        if (retryCount > 5) {
            // if retry count > 5 throw  超过5次 锁定
            throw new ExcessiveAttemptsException("username:"+username+" tried to login more than 5 times in period");
        }  
        //否则走判断密码逻辑
        boolean matches = super.doCredentialsMatch(token, info);  
        if (matches) {  
            // clear retry count  清楚ehcache中的count次数缓存
            redisUtil.del(username);
        }  
        return matches;  
    }  
} 