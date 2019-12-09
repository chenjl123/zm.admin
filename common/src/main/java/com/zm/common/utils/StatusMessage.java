package com.zm.common.utils;

/**
 * 服务端返回，响应状态和信息
 */
public interface StatusMessage {
    int getCode();
    String getMessage();

    public enum SystemStatus implements StatusMessage{
        SUCCESS(0,"SUCCESS"), //请求成功
        ERROR(2001,"ERROR"),	   //请求失败
        PARAM_ERROR(2002,"PARAM_ERROR"), //请求参数有误
        NO_LOGIN(2100,"NO_LOGIN"), //未登录
        LOCK(2101,"LOCK"); //用户已被锁

        private int code;
        private String message;

        private SystemStatus(int code,String message){
            this.code = code;
            this.message = message;
        }

        @Override
        public int getCode() {
            return 0;
        }

        @Override
        public String getMessage() {
            return null;
        }
    }
}
