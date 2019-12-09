package com.zm.manager.domain;

import com.jfinal.plugin.activerecord.Model;

public class Resource extends Model<Resource> {
    private Long id;
    private Long pid;
    private String name;
    private String url;
    private int seq;
    private String type;
    private Long create_user;
    private Long create_time;
}
