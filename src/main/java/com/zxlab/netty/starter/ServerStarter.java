package com.zxlab.netty.starter;

import io.netty.channel.nio.NioEventLoopGroup;

public class ServerStarter {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(2);
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        Server server = new Server(boss, worker);
        server.bind(6888);
        server.closeWait();
    }
}
