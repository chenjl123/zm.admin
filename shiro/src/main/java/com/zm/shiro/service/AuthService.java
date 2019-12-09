package com.zm.shiro.service;

import com.zm.common.domain.manager.Permission;
import com.zm.common.domain.manager.Role;
import com.zm.common.domain.manager.RolePermission;
import com.zm.common.domain.manager.User;

import java.util.List;

/**
 *权限管理类
 */
public interface AuthService {

	/**
	 * 关联查询权限树列表
	 * @return
	 */
	List<Permission> findPerms();

	/**
	 * 添加角色
	 * @param role--
	 * @return
	 */
	String addRole(Role role);

	/**
	 * 更新角色--
	 * @param role
	 * @return
	 */
	String updateRole(Role role);

	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	String delRole(int id);

	/**
	 * 根据用户获取角色列表
	 * @param userId
	 * @return
	 */
	List<Role> getRoleByUser(Long userId);

	/**
	 * 根据角色id获取权限数据
	 * @param id
	 * @return
	 */
	List<Permission> findPermsByRoleId(Long id);
	
	/**
	 * 根据用户id获取权限数据
	 * @param id
	 * @return
	 */
	List<Permission> getUserPerms(Long id);
	
	/**
	 * 查询所有角色 --
	 * @return
	 */
	List<Role> allRoles();
	
	/**
	 * 添加用户角色
	 * @param roles
	 * @param uid
	 */
	void addUserRoles(Integer[] roles, Integer uid);
	
	/**
	 * 通过角色回去权限
	 * @param role_id
	 * @return
	 */
	List<RolePermission> listByRole(Integer role_id);
	
	/**
	 * 添加角色权限
	 * @param perms
	 * @param rid
	 */
	void addRolePerms(Integer[] perms, Integer rid);

	/**
	 * 通编码获取用户
	 * @param userCode
	 * @return
	 */
	User getByUserCode(String userCode);

}
