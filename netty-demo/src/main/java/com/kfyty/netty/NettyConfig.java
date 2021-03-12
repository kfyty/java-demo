package com.kfyty.netty;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/21 14:12
 * @email kfyty725@hotmail.com
 */
public interface NettyConfig {
    String HOST = "127.0.0.1";

    int PORT = 666;

    String DELIMITER = "__$END$__";

    int DEFAULT_BUF = 16 * 1024;

    String PINT = "ping";

    String PONT = "pong";

    int RECONNECT = 5;

    int CLIENT_WRITE_TIME_OUT = 9;

    int SERVER_READ_WRITE_TIME_OUT = CLIENT_WRITE_TIME_OUT + 3;

    int CLIENT_READ_TIME_OUT = SERVER_READ_WRITE_TIME_OUT + 3;
}
