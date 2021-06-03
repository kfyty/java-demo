package com.kfyty.netty.server.codec;

import com.kfyty.netty.model.BaseMessage;
import com.kfyty.support.utils.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 描述: json 解码为基础消息对象
 *
 * @author kfyty
 * @date 2021/1/8 17:49
 * @email kfyty725@hotmail.com
 */
public class MessageModelDecoder extends MessageToMessageDecoder<String> {

    @Override
    protected void decode(ChannelHandlerContext ctx, String json, List<Object> out) throws Exception {
        if(StringUtils.isEmpty(json)) {
            return;
        }
        if(!json.contains("\"msgType\"")) {
            out.add(json);
            return;
        }
        BaseMessage baseMessage = JsonUtil.toObject(json, BaseMessage.class);
        baseMessage.setOriginJson(json);
        out.add(baseMessage);
    }
}
