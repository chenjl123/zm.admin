package com.zm.manager.service;

import com.zm.manager.domain.Role;
import com.zm.manager.domain.RoleResource;

import java.util.List;

/**
 *权限管理类
 */
public interface AuthService {
	/**
	 * 通过用户ID获取角色列表
	 * @param userId
	 * @return
	 */
	List<Role> getByUserId(long userId);

	/**
	 * 通过角色ID获取资源列表
	 * @param roleId
	 * @return
	 */
	List<RoleResource> getByRoleId(long roleId);
}
