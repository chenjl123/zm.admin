package com.zm.manager.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordGenerateUtil {
    /**
     * md5生成密码
     * @param password  密码
     * @param salt 加密盐
     * @param hashTimes 加密次数
     * @return
     */
    public static String getPassword(String password,String salt,int hashTimes){
        Md5Hash md5Hash = new Md5Hash(password,salt,hashTimes);
        return md5Hash.toString();
    }
}
