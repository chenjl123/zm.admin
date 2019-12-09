package com.zm.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * md5加密工具类
 */

public class Md5Util {
    private static Logger log = Logger.getLogger(Md5Util.class);

    private static String solt = "@#private$%";
    public static String createToken(String userCode){
        String token = DigestUtils.md5Hex(userCode + "sys");
        token = DigestUtils.md5Hex(token + solt);
        log.info("token:" + token);
        return token;
    };

    public static void main(String[] args){
        System.out.println(Md5Util.createToken("chenjl"));
    }
}
