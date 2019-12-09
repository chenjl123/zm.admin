package com.zm.manager.domain;

import com.jfinal.plugin.activerecord.Model;

public class UserRole extends Model<UserRole> {
    private Long id;
    private Long user_id;
    private Long role_id;
    private Long create_user;
    private Long create_time;
}
