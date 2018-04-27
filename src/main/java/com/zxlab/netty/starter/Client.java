package com.zxlab.netty.starter;

import com.zxlab.netty.encoder.TransformProtocolDecoder;
import com.zxlab.netty.encoder.TransformProtocolEncoder;
import com.zxlab.netty.handler.ChannelMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    private final NioEventLoopGroup worker;
    private Bootstrap bootstrap;
    private Channel clientChannel;

    public Client(NioEventLoopGroup worker) {
        this.worker = worker;
        this.bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(this.worker)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new TransformProtocolEncoder())
                                .addLast(new TransformProtocolDecoder())
                                .addLast(new ChannelMessageHandler());
                    }
                });
    }

    public void connect(String host, int port) {
        try {
            ChannelFuture channelFuture = this.bootstrap.connect(host, port).sync();
            this.clientChannel = channelFuture.channel();
            System.out.println("connect success");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public void close() {
        try {
            this.clientChannel.closeFuture().sync();
            this.worker.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
