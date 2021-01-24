package com.vince.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.net.URI;


/**
 * @author vince
 * @since v1.0.0
 */
public class ChannelHandlerClient {
    public static void main(String[] args) throws Exception {
        ChannelHandlerClient client = new ChannelHandlerClient();
        client.connect("127.0.0.1", 8088);
    }

    private void connect(String host, int port) throws Exception {
        // 配置线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();

            // 绑定线程池
            bootstrap.group(group);

            // 设置 channel 类型
            bootstrap.channel(NioSocketChannel.class);

            // 设置 channel 参数
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            // 注册 ChannelHandler
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new ClientChannelHandler());
                }
            });

            // 端口连接
            ChannelFuture future = bootstrap.connect(host, port).sync();

            // 请求路径
            URI uri = new URI("http://127.0.0.1:8888");

            // 设置请求
            String content = "hello";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, uri.toASCIIString(),
                    Unpooled.wrappedBuffer(content.getBytes(CharsetUtil.UTF_8)));

            // 请求参数
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

            // 将请求参数写入 channel
            future.channel().write(request);
            future.channel().flush();
            future.channel().closeFuture().sync();

        } finally {

            // 释放资源
            group.shutdownGracefully();
        }
    }
}
