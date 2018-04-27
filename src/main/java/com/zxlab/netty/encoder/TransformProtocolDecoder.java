package com.zxlab.netty.encoder;

import com.zxlab.netty.protocol.ConstantValue;
import com.zxlab.netty.protocol.TransformProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 协议解码器
 */
public class TransformProtocolDecoder extends ByteToMessageDecoder {

    public final int BASE_LENGTH = 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {
        // 可读长度必须大于基本长度
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            System.out.println("可读长度大于4");
            if (byteBuf.readableBytes() >= 1024 * 1024) {
                //数据过长抛弃该数据
                byteBuf.skipBytes(byteBuf.readableBytes());
            }
            // 记录包头开始的index
            int beginReader;
            while (true) {
                //获取包头开始的index
                beginReader = byteBuf.readerIndex();
                //标记包头开始的index
                byteBuf.markReaderIndex();
                // 读到了协议的开始标志，结束while循环
                int head = byteBuf.readInt();
                if (head == ConstantValue.HEAD_DATA) {
                    // 读取到了开始标志
                    break;
                }
                byteBuf.resetReaderIndex();
                // 未读到开头标志，往后移动一位，继续下次循环
                byteBuf.readByte();
                // 可读长度不够，不处理，下次发送数据过来
                if (byteBuf.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }
            // 消息的长度
          /*  byte[] lengthByte = new byte[4];
            byteBuf.readBytes(lengthByte);
            String lengthStr = new String(lengthByte);
            System.out.println(lengthStr);
            int length = Integer.valueOf(lengthStr);*/
            int length = byteBuf.readInt();
            // 判断请求数据包是否到齐
            if (byteBuf.readableBytes() < length) {
                // 还原指针
                byteBuf.readerIndex(beginReader);
                return;
            }
            // 读取data数据
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            TransformProtocol p = new TransformProtocol(data);
            list.add(p);
        }
    }
}
