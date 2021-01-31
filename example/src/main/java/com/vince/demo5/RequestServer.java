package com.vince.demo5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * @author vince
 * @since v1.0.0
 */
public class RequestServer {

    public void startEchoServer(int port) throws Exception {
        // 配置线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            // 绑定线程池
            bootstrap.group(bossGroup, workerGroup);

            // 设置 Channel 类型
            bootstrap.channel(NioServerSocketChannel.class);

            // 注册 ChannelHandler
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                    ch.pipeline().addLast(new ResponseSampleEncoder());
                    ch.pipeline().addLast(new RequestSampleHandler());
                }
            });

            // 端口绑定
            ChannelFuture f = bootstrap.bind(port).sync();

            // 让线程进入 wait 状态 , 服务端可以一直处于运行状态
            f.channel().closeFuture().sync();
        } finally {

            // 释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        new RequestServer().startEchoServer(8088);
    }
}