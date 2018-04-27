package com.zxlab.netty.rpc;

import java.util.concurrent.CountDownLatch;

public class RpcExecuter {

    private RpcRequest request;

    private RpcResponse response;

    private CountDownLatch latch = new CountDownLatch(1);

    public RpcExecuter(RpcRequest request) {
        this.request = request;
    }

    public RpcRequest getRequest() {
        return request;
    }

    public void setRequest(RpcRequest request) {
        this.request = request;
    }

    public RpcResponse getResponse() {
        if (response == null) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        latch.countDown();
    }
}
