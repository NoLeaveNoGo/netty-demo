package com.zxlab.netty.starter;

import com.zxlab.netty.protocol.TransformProtocol;
import com.zxlab.netty.rpc.RpcExecuter;
import com.zxlab.netty.rpc.RpcRequest;
import com.zxlab.netty.serializer.SerializationUtil;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientStarter {

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup(2);

        Client client = new Client(worker);
        client.connect("127.0.0.1", 6888);
        RpcRequest request = new RpcRequest();
        String id = UUID.randomUUID().toString();
        request.setId(id);
        System.out.println("request ID" + id);
        request.setClassName("com.zxlab.netty.service.UserService");
        request.setMethodName("createRanomNumber");
        request.setParamTypes(new Class[]{int.class});
        request.setParamValues(new Object[]{23});
        TransformProtocol p = new TransformProtocol(SerializationUtil.serialize(request));
        ConcurrentHashMap<String, RpcExecuter> map = client.getClientHandler().getExecuteMap();
        RpcExecuter executer = new RpcExecuter(request);
        map.put(request.getId(), executer);
        client.getClientChannel().writeAndFlush(p);
        System.out.println(executer.getResponse().getResult());
        client.close();
    }
}
