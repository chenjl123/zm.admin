package com.zm.manager.service.impl;

import com.zm.manager.domain.Role;
import com.zm.manager.domain.RoleResource;
import com.zm.manager.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public List<Role> getByUserId(long userId) {
        return null;
    }

    @Override
    public List<RoleResource> getByRoleId(long roleId) {
        return null;
    }
}
