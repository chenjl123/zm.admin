package com.zm.manager.domain;

import com.jfinal.plugin.activerecord.Model;
import lombok.Data;

@Data
public class RoleResource extends Model<RoleResource> {
    private Long id;
    private String res_url;
    private Long role_id;
    private Long create_user;
    private Long create_time;
}
