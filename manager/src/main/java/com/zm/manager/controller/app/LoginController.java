package com.zm.manager.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.zm.common.utils.Md5Util;
import com.zm.common.utils.RedisUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app")
/**
 * 手机登录
 */
public class LoginController {
    @Autowired
    private RedisUtil redisUtil;

    private static Logger log = Logger.getLogger(LoginController.class);
    /**
     * 登录
     * @return
     */
    @GetMapping("/login")
    public JSONObject login(String user_code, String pass_word){
        log.info("user_code" + user_code);
        String token = Md5Util.createToken(user_code);
        redisUtil.set(token, user_code, 1800);

        JSONObject bjson = new JSONObject();
        bjson.put("code", 0);
        bjson.put("msg", "success");
        bjson.put("token", token);
        return bjson;
    }
}
