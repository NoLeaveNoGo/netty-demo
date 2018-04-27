package com.zxlab.netty.encoder;

import com.zxlab.netty.protocol.TransformProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义协议的编码器
 */
public class TransformProtocolEncoder extends MessageToByteEncoder<TransformProtocol> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          TransformProtocol msg, ByteBuf out) throws Exception {
        // 写出消息头
        out.writeInt(msg.getHeadData());
        // 消息的内容长度
        out.writeInt(msg.getLength());
        // 写出消息的内容
        out.writeBytes(msg.getContent());
    }
}
