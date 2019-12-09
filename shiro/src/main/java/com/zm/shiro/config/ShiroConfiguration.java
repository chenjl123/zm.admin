package com.zm.shiro.config;

import com.zm.shiro.shiro.RetryLimitHashedCredentialsMatcher;
import com.zm.shiro.shiro.ShiroRealm;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {
	private static Logger log = Logger.getLogger(ShiroConfiguration.class);

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		log.info("-----------------Shiro拦截器工厂类注入开始");
		// 配置shiro安全管理器 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 指定要求登录时的链接
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/home");
		// 未授权时跳转的界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

		//自定义拦截器
		//Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
		//限制同一帐号同时在线的个数。
		//filtersMap.put("kickout", kickoutSessionControlFilter());
		//shiroFilterFactoryBean.setFilters(filtersMap);

		// filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出过滤器,具体的退出代码Shiro已经实现
		filterChainDefinitionMap.put("/logout", "logout");
		// // 配置不会被拦截的链接 从上向下顺序判断
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/images/**", "anon");
		filterChainDefinitionMap.put("/layui/**", "anon");

		filterChainDefinitionMap.put("/user/sendMsg", "anon");
		filterChainDefinitionMap.put("/user/login", "anon");
		filterChainDefinitionMap.put("/home", "anon");
		// //add操作，该用户必须有【addOperation】权限
        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //自定义加载权限资源关系
//        List<Resources> resourcesList = resourcesService.queryAll();
//        for (Resources resources :resourcesList) {
//            if (StringUtil.isNotEmpty(resources.getResurl())){
//                String permission="perms["+resources.getResurl()+"]";
//                filterChainDefinitionMap.put(resources.getResurl(),permission);
//            }
//        }
        filterChainDefinitionMap.put("/**","authc");

		shiroFilterFactoryBean
				.setFilterChainDefinitionMap(filterChainDefinitionMap);
		log.debug("-----------------Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
    }

	/**
	 *  SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
	 * @return
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		// 注入Cookie记住我管理器
		securityManager.setRememberMeManager(rememberMeManager());
		//用户授权/认证信息Cache, 采用redis 缓存
		securityManager.setCacheManager(cacheManager());
		// 自定义session管理 使用redis
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，
	 * 负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
	 */
	@Bean(name = "shiroRealm")
	@DependsOn("lifecycleBeanPostProcessor")
	public ShiroRealm shiroRealm() {
		ShiroRealm realm = new ShiroRealm();
		realm.setCredentialsMatcher(hashedCredentialsMatcher());
		return realm;
	}

	/**
	 *  cacheManager 缓存 redis实现
	 *  使用的是shiro-redis开源插件
	 */
	public RedisCacheManager cacheManager(){
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}

	/**
	 * 设置记住我cookie过期时间
	 * @return
	 */
	@Bean
	public SimpleCookie remeberMeCookie() {
		log.debug("记住我，设置cookie过期时间！");
		SimpleCookie scookie = new SimpleCookie("rememberMe");
		// 记住我cookie生效时间30天 ,单位秒 [1小时]
		scookie.setMaxAge(3600);
		return scookie;
	}
	
    /**
	 * 配置cookie记住我管理器
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		log.debug("配置cookie记住我管理器！");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(remeberMeCookie());
		return cookieRememberMeManager;
	}


	/**
	 * 配置shiro redisManager
	 * 使用的是shiro-redis开源插件
	 *
	 * @return
	 */
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost("localhost");
		redisManager.setPort(6379);
		redisManager.setExpire(3600);// 配置缓存过期时间
		redisManager.setTimeout(5000);
		return redisManager;
	}

	/**
	 * Session Manager
	 * 使用的是shiro-redis开源插件
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		Collection<SessionListener> listeners = new ArrayList<SessionListener>();
		listeners.add(new MySessionListener());
		sessionManager.setSessionListeners(listeners);
		//设置session超时时间为1小时(单位毫秒)
		sessionManager.setGlobalSessionTimeout(180000);
		//sessionManager.setGlobalSessionTimeout(-1);//永不超时
		//设置redisSessionDao
		sessionManager.setSessionDAO(redisSessionDAO());
		return sessionManager;
	}

	//配置redisSessionDAO
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
	 *
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于 	// md5(md5(""));
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);       //存储16进制格式
		return hashedCredentialsMatcher;
	}
	
    /**
     *  DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }


	/**
	 * 限制同一账号登录同时登录人数控制
	 *
	 * @return
	 */
	//@Bean
//	public KickoutSessionControlFilter kickoutSessionControlFilter() {
//		KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
//		kickoutSessionControlFilter.setCacheManager(cacheManager());
//		kickoutSessionControlFilter.setSessionManager(sessionManager());
//		kickoutSessionControlFilter.setKickoutAfter(false);
//		kickoutSessionControlFilter.setMaxSession(1);
//		kickoutSessionControlFilter.setKickoutUrl("/auth/kickout");
//		return kickoutSessionControlFilter;
//	}
}
