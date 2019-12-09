package com.zm.shiro.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordGenerateUtil {
    /**
     * md5生成密码
     * @param userCoe 用户编码
     * @param password  密码
     * @param salt 加密盐
     * @param hashTimes 加密次数
     * @return
     */
    public static String getPassword(String userCoe,String password,String salt,int hashTimes){
        Md5Hash md5Hash = new Md5Hash(password,userCoe + salt,hashTimes);
        return md5Hash.toString();
    }
}
