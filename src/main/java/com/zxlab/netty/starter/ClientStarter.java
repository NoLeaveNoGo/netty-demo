package com.zxlab.netty.starter;

import com.zxlab.netty.protocol.TransformProtocol;
import io.netty.channel.nio.NioEventLoopGroup;

public class ClientStarter {

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup(2);

        Client client = new Client(worker);
        client.connect("127.0.0.1", 6888);
        TransformProtocol p = new TransformProtocol("hello,world!".getBytes());
        client.getClientChannel().writeAndFlush(p);
        client.close();
    }
}
