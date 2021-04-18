package com.kfyty.security.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PermissionDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            if(configAttribute.toString().equals("authenticated")) {
                this.decide(authentication, object, configAttribute);
            }
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    private void decide(Authentication authentication, Object object, ConfigAttribute configAttribute) throws AccessDeniedException, InsufficientAuthenticationException {
        FilterInvocation invocation = (FilterInvocation) object;
        if(!(authentication.getPrincipal() instanceof UserDetail)) {
            throw new InsufficientAuthenticationException("anonymousUser");
        }
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        if(!userDetail.getPermissions().contains(invocation.getRequestUrl())) {
            throw new AccessDeniedException(HttpStatus.UNAUTHORIZED.toString());
        }
    }
}
