package com.kfyty.security.config.security;

import com.kfyty.security.config.annotation.AnonymousMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

@Component
public class PermissionDecisionManager implements AccessDecisionManager {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            if ("authenticated".equals(configAttribute.toString())) {
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
        if (!(authentication.getPrincipal() instanceof UserDetail)) {
            if (this.hasAnonymousMapping(invocation.getRequest())) {
                return;
            }
            throw new InsufficientAuthenticationException("anonymousUser");
        }
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        if (!userDetail.hasPermission(invocation.getRequestUrl())) {
            throw new AccessDeniedException(HttpStatus.UNAUTHORIZED.toString());
        }
    }

    private boolean hasAnonymousMapping(HttpServletRequest request) {
        try {
            HandlerExecutionChain handlerChain = this.requestMappingHandlerMapping.getHandler(request);
            return Optional.ofNullable(handlerChain)
                    .map(HandlerExecutionChain::getHandler)
                    .filter(e -> e instanceof HandlerMethod)
                    .map(e -> (HandlerMethod) e)
                    .filter(method -> AnnotatedElementUtils.hasAnnotation(method.getBeanType(), AnonymousMapping.class) || AnnotatedElementUtils.hasAnnotation(method.getMethod(), AnonymousMapping.class))
                    .isPresent();
        } catch (Exception e) {
            return false;                                                                                               // 忽略这里的异常
        }
    }
}
