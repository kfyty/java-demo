package com.kfyty.security.service;

import com.kfyty.security.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
}
