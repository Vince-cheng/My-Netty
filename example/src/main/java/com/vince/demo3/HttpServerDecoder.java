package com.vince.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author vince
 * @since v1.0.0
 */
public class HttpServerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 判断 ByteBuf 可读取字节
        if (in.readableBytes() < 14) {
            return;
        }
        in.markReaderIndex(); // 标记 ByteBuf 读指针位置
        in.skipBytes(2); // 跳过魔数
        in.skipBytes(1); // 跳过协议版本号
        byte serializeType = in.readByte();
        in.skipBytes(1); // 跳过报文类型
        in.skipBytes(1); // 跳过状态字段
        in.skipBytes(4); // 跳过保留字段
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex(); // 重置 ByteBuf 读指针位置
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
//        SerializeService serializeService = getSerializeServiceByType(serializeType);
//        Object obj = serializeService.deserialize(data);
//        if (obj != null) {
//            out.add(obj);
//        }
    }
}
