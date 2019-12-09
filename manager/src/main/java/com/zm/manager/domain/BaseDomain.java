package com.zm.manager.domain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

public class BaseDomain {
    public Map<String, Object> castBeanToMap(Object bean){
        JSONObject obj = (JSONObject) JSONObject.toJSON(bean);
        return  JSONObject.parseObject(obj.toJSONString(), new TypeReference<Map<String, Object>>(){});
    }
}
