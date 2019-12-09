package com.zm.geteway.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {
    @GetMapping("hystrixTimeout")
    public JSONObject hystrixTimeout(){
    JSONObject json = new JSONObject();
        json.put("code", 2001);
        json.put("msg", "请求超时，请刷新页面重试");
        return json;
}
}
