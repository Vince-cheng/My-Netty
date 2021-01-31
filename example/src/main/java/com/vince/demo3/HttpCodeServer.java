package com.vince.demo3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

/**
 * @author vince
 * @since v1.0.0
 */
public class HttpCodeServer {

    public static void main(String[] args) throws Exception {
        new HttpCodeServer().start(8888);
    }

    private void start(int port) throws Exception {

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
                protected void initChannel(SocketChannel ch) {
                    // Http 解码器
                    ch.pipeline().addLast("codec", new HttpServerCodec());
                    // HttpContent 压缩
                    ch.pipeline().addLast("compressor", new HttpContentCompressor());
                    // Http 消息聚合
                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                    // 自定义业务逻辑处理器
                    ch.pipeline().addLast("handler", new HttpServerHandler());
                }
            });

            // 端口绑定
            ChannelFuture future = bootstrap.bind().sync();

            System.err.println("Http Server started， Listening on " + port);

            // 让线程进入 wait 状态 , 服务端可以一直处于运行状态
            future.channel().closeFuture().sync();

        } finally {

            // 释放资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
