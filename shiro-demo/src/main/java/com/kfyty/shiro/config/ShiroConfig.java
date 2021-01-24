package com.kfyty.shiro.config;

import com.kfyty.shiro.realm.UserRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ShiroConfig {
    public static final int HASH_ITERATIONS = 3;

    @Autowired(required = false)
    protected RolePermissionResolver rolePermissionResolver;

    @Autowired(required = false)
    protected PermissionResolver permissionResolver;

    /**
     * 配置凭据匹配器
     * @return CredentialsMatcher
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(UserRealm userRealm) {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashIterations(HASH_ITERATIONS);
        credentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return credentialsMatcher;
    }

    @Bean("authorizer")
    public Authorizer authenticator() {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        if (permissionResolver != null) {
            authorizer.setPermissionResolver(permissionResolver);
        }
        if (rolePermissionResolver != null) {
            authorizer.setRolePermissionResolver(rolePermissionResolver);
        }
        return authorizer;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 配置过滤链
     * @return ShiroFilterChainDefinition
     */
    @Bean
    public ShiroFilterChainDefinition filterChainDefinition() {
        DefaultShiroFilterChainDefinition filterChainDefinition = new DefaultShiroFilterChainDefinition();
        filterChainDefinition.addPathDefinition("/*.ico", DefaultFilter.anon.name());
        filterChainDefinition.addPathDefinition("/index.html", DefaultFilter.anon.name());
        filterChainDefinition.addPathDefinition("/refuse.html", DefaultFilter.anon.name());
        filterChainDefinition.addPathDefinition("/passport/login", DefaultFilter.anon.name());
        filterChainDefinition.addPathDefinition("/user/register", DefaultFilter.anon.name());
        filterChainDefinition.addPathDefinition("/lib/**", DefaultFilter.anon.name());
        filterChainDefinition.addPathDefinition("/logout", DefaultFilter.logout.name());
        filterChainDefinition.addPathDefinition("/**", DefaultFilter.authc.name());
        return filterChainDefinition;
    }
}
