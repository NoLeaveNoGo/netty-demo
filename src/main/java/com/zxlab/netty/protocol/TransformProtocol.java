package com.zxlab.netty.protocol;

import java.util.Arrays;

public class TransformProtocol {
    /**
     * 消息的开头的信息标志
     */
    private final int headData = ConstantValue.HEAD_DATA;

    /**
     * 消息的长度
     */
    private int length;
    /**
     * 消息的内容
     */
    private byte[] content;

    public TransformProtocol(byte[] content) {
        this.length = content.length;
        this.content = content;
    }

    public int getHeadData() {
        return headData;
    }

    public int getLength() {
        return length;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "TransformProtocol{" +
                "headData=" + headData +
                ", length=" + length +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
