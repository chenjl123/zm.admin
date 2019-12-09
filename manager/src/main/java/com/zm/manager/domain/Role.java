package com.zm.manager.domain;

import com.jfinal.plugin.activerecord.Model;
import lombok.Data;

@Data
public class Role {
    private long id;
    private String role_code;
    private String role_name;
    private String remark;
    private Long create_user;
    private Long create_time;
}
