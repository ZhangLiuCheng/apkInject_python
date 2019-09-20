package com.playin.hook;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;

import com.playin.util.SocketConnect;

import java.nio.ByteBuffer;
import java.util.Arrays;

@SuppressWarnings("deprecation")
public class HookJava extends AudioTrack {

    public HookJava(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode) throws IllegalArgumentException {
        super(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
        Log.e("HookAudioTrack", "HookAudioTrack  11111");
        SocketConnect.getInstance().sendAudioConfig(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
    }

    public HookJava(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode, int sessionId) throws IllegalArgumentException {
        super(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode, sessionId);
        Log.e("HookAudioTrack", "HookAudioTrack  22222");

        SocketConnect.getInstance().sendAudioConfig(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
    }

    public HookJava(AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int mode, int sessionId) throws IllegalArgumentException {
        super(attributes, format, bufferSizeInBytes, mode, sessionId);
        Log.e("HookAudioTrack", "HookAudioTrack  33333");
    }

    @Override
    public void play() throws IllegalStateException {
        super.play();
        Log.e("HookAudioTrack", "HookAudioTrack  play");
    }

    @Override
    public int write(byte[] audioData, int offsetInBytes, int sizeInBytes) {
//        Log.e("HookAudioTrack", "HookAudioTrack  write  111111  " + Arrays.toString(audioData));
        SocketConnect.getInstance().sendData(audioData);
        return super.write(audioData, offsetInBytes, sizeInBytes);
    }

    @Override
    public int write(byte[] audioData, int offsetInBytes, int sizeInBytes, int writeMode) {
//        Log.e("HookAudioTrack", "HookAudioTrack  write  22222  "  + Arrays.toString(audioData));
        SocketConnect.getInstance().sendData(audioData);
        return super.write(audioData, offsetInBytes, sizeInBytes, writeMode);
    }

    @Override
    public int write(short[] audioData, int offsetInShorts, int sizeInShorts) {
        Log.e("HookAudioTrack", "HookAudioTrack  write  33333  "  + Arrays.toString(audioData));
        return super.write(audioData, offsetInShorts, sizeInShorts);
    }

    @Override
    public int write(short[] audioData, int offsetInShorts, int sizeInShorts, int writeMode) {
        Log.e("HookAudioTrack", "HookAudioTrack  write  444444  "  + Arrays.toString(audioData));
        return super.write(audioData, offsetInShorts, sizeInShorts, writeMode);
    }

    @Override
    public int write(float[] audioData, int offsetInFloats, int sizeInFloats, int writeMode) {
        Log.e("HookAudioTrack", "HookAudioTrack  write  555555  "  + Arrays.toString(audioData));
        return super.write(audioData, offsetInFloats, sizeInFloats, writeMode);
    }

    @Override
    public int write(ByteBuffer audioData, int sizeInBytes, int writeMode) {
        Log.e("HookAudioTrack", "HookAudioTrack  write  666666  "  + Arrays.toString(conver(audioData)));
        return super.write(audioData, sizeInBytes, writeMode);
    }

    @Override
    public int write(ByteBuffer audioData, int sizeInBytes, int writeMode, long timestamp) {
        Log.e("HookAudioTrack", "HookAudioTrack  write  777777  "  + Arrays.toString(conver(audioData)));
        return super.write(audioData, sizeInBytes, writeMode, timestamp);
    }

    public byte[] conver(ByteBuffer byteBuffer){
        int len = byteBuffer.limit() - byteBuffer.position();
        byte[] bytes = new byte[len];

        if(byteBuffer.isReadOnly()){
            return null;
        }else {
            byteBuffer.get(bytes);
        }
        return bytes;
    }
}
