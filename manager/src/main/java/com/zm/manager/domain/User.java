package com.zm.manager.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User extends BaseDomain{
    private Long id;
    private String user_code;
    private String user_name;
    private String sex;
    private String pwd;
    private String user_type;
    private String head_url;
    private String we_chat;
    private String mobile;
    private String email;
    private String qq;
    private Long create_user;
    private Date create_time;
}
