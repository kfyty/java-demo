package com.kfyty.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class PermissionsAuthorizationFilter extends org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        return subject.isPermittedAll(servletRequest.getRequestURI());
    }
}
