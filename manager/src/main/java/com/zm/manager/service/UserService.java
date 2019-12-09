package com.zm.manager.service;

import com.jfinal.plugin.activerecord.Record;
import com.zm.manager.domain.User;

/**
 * 用户相关
 */
public interface UserService {
    /**
     * 通用户编码获取用户
     * @param user_code
     * @return
     */
    Record findUserByCode(String user_code);

    void addUser(User user);
}
