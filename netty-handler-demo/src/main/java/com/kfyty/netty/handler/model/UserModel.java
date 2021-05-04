package com.kfyty.netty.handler.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserModel extends BaseHandlerModel {
    private String userName;
}
