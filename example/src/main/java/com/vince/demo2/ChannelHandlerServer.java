package com.vince.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author vince
 * @since v1.0.0
 */
public class ChannelHandlerServer {
    public void start(int port) throws Exception {
        // 配置线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 绑定线程池
            bootstrap.group(bossGroup, workerGroup);

            // 设置端口号
            bootstrap.localAddress(new InetSocketAddress(port));

            // 设置 Channel 类型
            bootstrap.channel(NioServerSocketChannel.class);

            // 设置 Channel 参数
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            // 注册 ChannelHandler
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline()
                            .addLast(new SampleInBoundHandler("SampleInBoundHandlerA", false))
                            .addLast(new SampleInBoundHandler("SampleInBoundHandlerB", false))
                            .addLast(new SampleInBoundHandler("SampleInBoundHandlerC", true));
                    ch.pipeline()
                            .addLast(new SampleOutBoundHandler("SampleOutBoundHandlerA"))
                            .addLast(new SampleOutBoundHandler("SampleOutBoundHandlerB"))
                            .addLast(new SampleOutBoundHandler("SampleOutBoundHandlerC"));
                    ch.pipeline().addLast(new ExceptionHandler());
                }
            });
            // 端口绑定
            ChannelFuture future = bootstrap.bind().sync();

            System.err.println("Http Server started， Listening on " + port);

            // 让线程进入 wait 状态 , 服务端可以一直处于运行状态
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChannelHandlerServer().start(8088);
    }

}
