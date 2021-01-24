package com.vince.demo2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author vince
 * @since v1.0.0
 */
public class SampleInBoundHandler extends ChannelInboundHandlerAdapter {
    private final String name;
    private final boolean flush;

    public SampleInBoundHandler(String name, boolean flush) {
        this.name = name;
        this.flush = flush;
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("InBoundHandler: " + name);
//        if (flush) {
//            ctx.channel().writeAndFlush(msg);
//        } else {
//            super.channelRead(ctx, msg);
//        }
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.err.println("InBoundHandler: " + name);
        if (flush) {
            ctx.channel().writeAndFlush(msg);
        } else {
            throw new RuntimeException("InBoundHandler: " + name);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("InBoundHandlerException: " + name);
        ctx.fireExceptionCaught(cause);
    }

}
