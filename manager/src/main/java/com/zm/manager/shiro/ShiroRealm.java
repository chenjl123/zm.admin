package com.zm.manager.shiro;

import com.jfinal.plugin.activerecord.Record;
import com.zm.manager.domain.Role;
import com.zm.manager.domain.RoleResource;
import com.zm.manager.domain.User;
import com.zm.manager.service.AuthService;
import com.zm.manager.service.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class ShiroRealm extends AuthorizingRealm {
	private static Logger log = Logger.getLogger(ShiroRealm.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;
	
	/**
	 * 权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//授权
		log.info("授予角色和权限");
		// 添加权限 和 角色信息
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 获取当前登陆用户
		Subject subject = SecurityUtils.getSubject();
		Record user = (Record) subject.getPrincipal();
		if (user.getStr("user_code").equals("admin")) {
			// 超级管理员，添加所有角色、添加所有权限
			authorizationInfo.addRole("admin");
			authorizationInfo.addStringPermission("user/list");
		} else {
			// 普通用户，查询用户的角色，根据角色查询权限
			authorizationInfo.addRole("user");
			authorizationInfo.addStringPermission("user/list");
//			Long userId = user.getId();
//			List<Role> roles = this.authService.getByUserId(userId);
//			if (null != roles && roles.size() > 0) {
//				for (Role role : roles) {
//					authorizationInfo.addRole(role.getRole_code());
//					//角色对应的权限数据
//					List<RoleResource> perms = this.authService.getByRoleId(role.getId());
//					if (null != perms && perms.size() > 0) {
//						// 授权角色下所有权限
//						for (RoleResource perm : perms) {
//							authorizationInfo.addStringPermission(perm.getRes_url());
//						}
//					}
//				}
//			}
		}
		return authorizationInfo;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		//UsernamePasswordToken用于存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
		String user_code = token.getUsername();
		// 调用数据层
		Record user = userService.findUserByCode(user_code);
		if (user == null) {
			// 用户不存在
			return null;
		} else {
			// 密码存在
			// 第一个参数 ，登陆后，需要在session保存数据
			// 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
			// 第三个参数 ，realm名字
			return new SimpleAuthenticationInfo(user, user.getStr("pwd"), ByteSource.Util.bytes(user_code), getName()+":"+user_code);
		}
	}
	
	/**
	 * 清除所有缓存
	 */
	public void clearCachedAuth(){
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}
}
