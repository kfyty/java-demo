package com.kfyty.security.constant;

import java.util.function.Function;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 15:57
 * @email kfyty725@hotmail.com
 */
public interface Constant {

    Function<String, String> REDIS_USER_KEY = token -> "user:" + token;

    String REDIS_USER_ID_KEY = "id";

    String TOKEN = "token";
}
