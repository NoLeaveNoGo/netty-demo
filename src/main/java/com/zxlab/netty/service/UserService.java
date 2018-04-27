package com.zxlab.netty.service;

import java.util.Random;

public class UserService {

    public int createRanomNumber(int params) {
        System.out.println("receive prams:" + params);
        Random random = new Random();
        int result = random.nextInt(params);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("result:" + result);
        return result;
    }
}
