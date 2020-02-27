package com.tech.playinsdk.webrtc.touch;

public class Util {

    public static byte[] intToBytes(int value){
//        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();

        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToInt(byte[] src){
//        return ByteBuffer.wrap(src).order(ByteOrder.LITTLE_ENDIAN).getInt();

        int value = (int) ((src[0] & 0xFF)
                | ((src[1] & 0xFF) << 8)
                | ((src[2] & 0xFF) << 16)
                | ((src[3] & 0xFF) << 24));
        return value;
    }
}
