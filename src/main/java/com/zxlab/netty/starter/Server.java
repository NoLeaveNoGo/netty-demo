package com.zxlab.netty.starter;

import com.zxlab.netty.encoder.TransformProtocolDecoder;
import com.zxlab.netty.encoder.TransformProtocolEncoder;
import com.zxlab.netty.handler.ChannelMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

    private final NioEventLoopGroup boss;
    private final NioEventLoopGroup worker;
    private ServerBootstrap bootstrap;
    private Channel serverChannel;

    public Server(NioEventLoopGroup boss, NioEventLoopGroup worker) {
        this.bootstrap = new ServerBootstrap();
        this.boss = boss;
        this.worker = worker;
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new TransformProtocolDecoder())
                                .addLast(new TransformProtocolEncoder())
                                .addLast(new ChannelMessageHandler());
                    }
                });
    }

    public void bind(int port) {
        try {
            ChannelFuture channelFuture = this.bootstrap.bind(port).sync();
            this.serverChannel = channelFuture.channel();
            System.out.println("bind success:" + port);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeWait() {
        try {
            this.serverChannel.closeFuture().sync();
            this.boss.shutdownGracefully().sync();
            this.worker.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
