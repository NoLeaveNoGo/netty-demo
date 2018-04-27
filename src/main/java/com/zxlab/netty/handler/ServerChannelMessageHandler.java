package com.zxlab.netty.handler;

import com.zxlab.netty.protocol.TransformProtocol;
import com.zxlab.netty.rpc.RpcRequest;
import com.zxlab.netty.rpc.RpcResponse;
import com.zxlab.netty.serializer.SerializationUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.lang.reflect.Method;

public class ServerChannelMessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " connect!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TransformProtocol p = (TransformProtocol) msg;
        RpcRequest request = SerializationUtil.deserialize(p.getContent(), RpcRequest.class);
        RpcResponse response = createResponse(request);
        p = new TransformProtocol(SerializationUtil.serialize(response));
        ctx.writeAndFlush(p);
    }

    private RpcResponse createResponse(RpcRequest request) {
        RpcResponse resp = new RpcResponse();
        resp.setId(request.getId());
        System.out.println("respId" + resp.getId());
        try {
            Object result = invokeMethod(request.getClassName(), request.getMethodName(), request.getParamTypes(), request.getParamValues());
            resp.setResult(result);
        } catch (Exception e) {
            resp.setErrorMessage(e.getMessage());
        }
        return resp;
    }

    private Object invokeMethod(String className, String methodName, Class<?>[] paramTypes, Object[] paramValues) throws Exception {
        System.out.println("execute target method");
        Class<?> targetClazz = Class.forName(className);
        Object targetObj = targetClazz.newInstance();
        Method targetMethod = targetClazz.getDeclaredMethod(methodName, paramTypes);
        return targetMethod.invoke(targetObj, paramValues);
    }
}
