package com.kfyty.netty.handler.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kfyty.netty.handler.model.BaseHandlerModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 描述: netty 控制器模型解码器
 *
 * @author kfyty725
 * @date 2021/4/30 15:03
 * @email kfyty725@hotmail.com
 */
public class BaseHandlerModelDecoder extends MessageToMessageDecoder<String> {
    private final ObjectMapper objectMapper;

    public BaseHandlerModelDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, String json, List<Object> out) throws Exception {
        if(StringUtils.isEmpty(json)) {
            return;
        }
        if(!json.contains(BaseHandlerModel.NETTY_HANDLER_PATH_MARK)) {
            out.add(json);
            return;
        }
        BaseHandlerModel model = this.objectMapper.readValue(json, BaseHandlerModel.class);
        model.setOriginJson(json);
        out.add(model);
    }
}
