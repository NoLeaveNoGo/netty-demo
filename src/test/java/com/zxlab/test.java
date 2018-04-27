package com.zxlab;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        String s = "$_#_";
        System.out.println(Arrays.toString(s.getBytes()));
        int value = byteArrayToInt(s.getBytes());
        System.out.println(value);
        System.out.println(Integer.toBinaryString(value));

    }

    /* public static int byteArrayToInt(byte[] bytes) {
         return (bytes[0]&0xff) << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3];
     }*/
    public static int byteArrayToInt(byte[] bytes) {
        int result = 0;
        if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 24;//说明二
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }
}
