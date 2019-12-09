package com.zm.manager.controller;

import com.jfinal.plugin.activerecord.Record;
import com.zm.common.utils.RedisUtil;
import com.zm.common.utils.ResponseResult;
import com.zm.common.utils.StatusMessage;
import com.zm.manager.domain.User;
import com.zm.manager.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HomeController {
    private static Logger log = Logger.getLogger(HomeController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("home")
    public String home(){
        return "index";
    }

    @GetMapping("login")
    public String login(){
        return "user/login";
    }

    @GetMapping("reg")
    public String reg(){
        return "user/reg";
    }


    @GetMapping("user/list")
    @RequiresPermissions(value = "user/list")
    public String list(){
        return "user/user/list";
    }

    @RequiresRoles(value = "user")
    @GetMapping("user/userform")
    public String userform(){
        return "user/user/userform";
    }

    /**
     * 未授权时跳转的界面
     * @return
     */
    @GetMapping("unauthorized")
    public String unauthorized(){
        return "";
    }

    /**
     * 登录
     * @param user
     * @param rememberMe
     * @return
     */
    @ResponseBody
    @PostMapping(value = "ajax/login")
    public ResponseResult login(@RequestBody @Valid User user,
                                @RequestParam(value = "rememberMe", required = false) boolean rememberMe, BindingResult result) {
        if(result.hasErrors()){
            return new ResponseResult(2001, result.getFieldError().getDefaultMessage());
        }else{
            ResponseResult responseResult = new ResponseResult();
            responseResult.setCode(StatusMessage.SystemStatus.ERROR.getCode());

            //用户是否存在
            Record existUser = this.userService.findUserByCode(user.getUser_code());
            if (existUser == null) {
                responseResult.setMsg("用户不存在");
                return responseResult;
            }

            // 用户登录
            try {
                // 1、 封装用户名、密码、是否记住我到token令牌对象
                AuthenticationToken token = new UsernamePasswordToken(user.getUser_code(), user.getPwd(), rememberMe);
                // 2、 Subject调用login
                Subject subject = SecurityUtils.getSubject();
                // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
                // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
                // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
                subject.login(token);
                responseResult.setCode(StatusMessage.SystemStatus.SUCCESS.getCode());
            } catch (UnknownAccountException uae) {
                log.error("用户登录，用户验证未通过：未知用户！user=" + user.getUser_code(), uae);
                responseResult.setMsg("该用户不存在，请您联系管理员");
            } catch (IncorrectCredentialsException ice) {
                log.error("用户登录，用户验证未通过：错误的凭证，密码输入错误！user=" + user.getUser_code(),ice);
                responseResult.setMsg("用户名或密码不正确");
            } catch (LockedAccountException lae) {
                log.error("用户登录，用户验证未通过：账户已锁定！user=" + user.getUser_code(), lae);
                responseResult.setMsg("账户已锁定");
            } catch (ExcessiveAttemptsException eae) {
                log.error("用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user=.getUser_code()" + user, eae);
                responseResult.setMsg("用户名或密码错误次数大于5次,账户已锁定!</br><span style='color:red;font-weight:bold; '>2分钟后可再次登录，或联系管理员解锁</span>");
            } catch (DisabledAccountException sae){
                log.error("用户登录，用户验证未通过：帐号已经禁止登录！user=" + user.getUser_code(),sae);
                responseResult.setCode(StatusMessage.SystemStatus.ERROR.getCode());
                responseResult.setMsg("帐号已经禁止登录");
            }catch (AuthenticationException ae) {
                // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
                log.error("用户登录，用户验证未通过：认证异常，异常信息如下！user=" + user.getUser_code(),ae);
                responseResult.setMsg("用户名或密码不正确");
            } catch (Exception e) {
                log.error("用户登录，用户验证未通过：操作异常，异常信息如下！user=" + user.getUser_code(), e);
                responseResult.setMsg("用户登录失败，请您稍后再试");
            }

            Object retryCount = redisUtil.get(existUser.getStr("user_code"));
            if (null != retryCount) {
                int retryNum = (int) retryCount;
                log.info("输错次数：" + retryNum);
                if (retryNum > 0 && retryNum < 6) {
                    responseResult.setMsg("用户名或密码错误" + retryNum + "次,再输错" + (6 - retryNum) + "次账号将锁定");
                }
            }
            log.info("用户登录，user=" + user.getUser_code() + ",登录结果=responseResult:" + responseResult);
            return responseResult;
        }
    }

    @PostMapping("register")
    @ResponseBody
    public ResponseResult register(@RequestBody User user){
        userService.addUser(user);
        ResponseResult responseResult = new ResponseResult(StatusMessage.SystemStatus.SUCCESS.getCode(), "注册成功");
        return responseResult;
    }

}


//    new Record().setColumns(model);
//    new User()._setAttrs(record.getColumns());
//    new User().put(record);
