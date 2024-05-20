package com.xyzla.common.util;

public class ByteUtil {
    public static byte[] longToBytes8(long i) {
        byte bytes[] = new byte[8];
        bytes[7] = (byte) (0xffL & i);
        bytes[6] = (byte) ((0xff00L & i) >> 8);
        bytes[5] = (byte) ((0xff0000L & i) >> 16);
        bytes[4] = (byte) ((0xff000000L & i) >> 24);
        bytes[3] = (byte) ((0xff00000000L & i) >> 32);
        bytes[2] = (byte) ((0xff0000000000L & i) >> 40);
        bytes[1] = (byte) ((0xff000000000000L & i) >> 48);
        bytes[0] = (byte) ((0xff00000000000000L & i) >> 56);
        return bytes;
    }
}
