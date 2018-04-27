package com.zxlab.netty.handler;

import com.zxlab.netty.protocol.TransformProtocol;
import com.zxlab.netty.rpc.RpcExecuter;
import com.zxlab.netty.rpc.RpcResponse;
import com.zxlab.netty.serializer.SerializationUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.ConcurrentHashMap;

public class ClientChannelMessageHandler extends ChannelInboundHandlerAdapter {

    private ConcurrentHashMap<String, RpcExecuter> executeMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receiced service message");
        TransformProtocol p = (TransformProtocol) msg;
        RpcResponse response = SerializationUtil.deserialize(p.getContent(), RpcResponse.class);
        RpcExecuter executer = executeMap.get(response.getId());
        executer.setResponse(response);
    }

    public ConcurrentHashMap<String, RpcExecuter> getExecuteMap() {
        return executeMap;
    }
}
