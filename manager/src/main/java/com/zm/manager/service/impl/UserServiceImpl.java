package com.zm.manager.service.impl;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zm.manager.domain.User;
import com.zm.manager.service.UserService;
import com.zm.manager.utils.PasswordGenerateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Record findUserByCode(String user_code) {
        Record record = Db.findFirst("select * from s_user where user_code = ?", user_code);
        return record;
    }

    @Override
    public void addUser(User user) {
        //user.setCreate_time(new Date());
        user.setSex("1");
        user.setUser_type("1");
        user.setPwd(PasswordGenerateUtil.getPassword(user.getPwd(),user.getUser_code(),1));
        Record record = new Record().setColumns(user.castBeanToMap(user));
        Db.save("s_user", record);
    }
}
