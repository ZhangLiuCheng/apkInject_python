package com.playin.aac;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.text.TextUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static com.playin.aac.Constant.BIT_RATE;
import static com.playin.aac.Constant.CHANNEL_COUNT;
import static com.playin.aac.Constant.MAX_INPUT_SIZE;
import static com.playin.aac.Constant.MIME_TYPE;
import static com.playin.aac.Constant.SAMPLE_RATE;

/**
 * pcm编码aac
 */
public class AacEncoder {

    private MediaCodec mCodec;
    private String mCodecName;

    public interface EncoderListener {
        void aacData(byte[] buf);
    }
    private EncoderListener listener;


    public AacEncoder(EncoderListener listener) {
        this.listener = listener;
        init();
    }

    public void init() {
        for (int i = 0; i < MediaCodecList.getCodecCount(); i++) {
            MediaCodecInfo mediaCodecInfo = MediaCodecList.getCodecInfoAt(i);
            for (String type : mediaCodecInfo.getSupportedTypes()) {
                if (TextUtils.equals(type, MIME_TYPE) && mediaCodecInfo.isEncoder()) {
                    mCodecName = mediaCodecInfo.getName();
                    break;
                }
            }
            if (null != mCodecName) {
                break;
            }
        }
        try {
            mCodec = MediaCodec.createByCodecName(mCodecName);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat(MIME_TYPE, SAMPLE_RATE, CHANNEL_COUNT);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
            mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE,MAX_INPUT_SIZE);
            mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encoderAAC(byte[] buffer) {
        ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
        ByteBuffer[] outputBuffers = mCodec.getOutputBuffers();
        int inputBufferIndex = mCodec.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(buffer);
            mCodec.queueInputBuffer(inputBufferIndex, 0, buffer.length, System.nanoTime(), 0);
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 0);
        while (outputBufferIndex >= 0) {
            ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
            byte[] outData = new byte[bufferInfo.size];
            outputBuffer.get(outData);
            listener.aacData(outData);
            mCodec.releaseOutputBuffer(outputBufferIndex, false);
            outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 0);
        }
    }
}
