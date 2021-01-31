package com.vince.demo5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author vince
 * @since v1.0.0
 */
public class ResponseSampleEncoder extends MessageToByteEncoder<ResponseSample> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseSample msg, ByteBuf out) {
        if (msg != null) {
            out.writeBytes(msg.getCode().getBytes());
            out.writeBytes(msg.getData().getBytes());
            out.writeLong(msg.getTimestamp());
        }
    }
}

