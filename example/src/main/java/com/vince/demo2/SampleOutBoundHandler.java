package com.vince.demo2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author vince
 * @since v1.0.0
 */
public class SampleOutBoundHandler extends ChannelOutboundHandlerAdapter {
    private final String name;

    public SampleOutBoundHandler(String name) {
        this.name = name;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.err.println("OutBoundHandler: " + name);
        super.write(ctx, msg, promise);
    }
}
