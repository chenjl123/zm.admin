package com.zm.common.utils;

import lombok.Data;

/**
 * 服务端返回工具类
 */
@Data
public class ResponseResult {
    private int code;
    private String msg;
    private Object data;
    public ResponseResult() {
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
