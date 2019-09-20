package com.playin.util;

import android.net.LocalSocket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketConnect implements Runnable {

    private LinkedBlockingQueue<byte[]> voiceQueue = new LinkedBlockingQueue(100);

    private static SocketConnect sInstance = new SocketConnect();
    public static SocketConnect getInstance() {
        return sInstance;
    }

    private List<Thread> mWriteThreads = new ArrayList<>();
    private Thread mCurThread;
    private boolean mRunning;

    private File saveFile;

    private SocketConnect() {
    }

    public void setSaveFile(File file) {
        LogUtil.e("saveFile    " + file.getAbsolutePath());
        this.saveFile = file;
    }

    public void sendAudioConfig(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode) {
        LogUtil.e("streamType: " + streamType);
        LogUtil.e("sampleRateInHz: " + sampleRateInHz);
        LogUtil.e("channelConfig: " + channelConfig);
        LogUtil.e("audioFormat: " + audioFormat);
        LogUtil.e("bufferSizeInBytes: " + bufferSizeInBytes);

    }

    public void sendData(byte[] buf) {
//        LogUtil.e("音频数据 ----> " + Arrays.toString(buf));

        boolean flag = voiceQueue.offer(buf);

        LogUtil.e("音频数据添加到队列 ----> " + flag);
    }

    public void startServer() {
        LogUtil.e("SocketConnect ----> startServer");

        mRunning = true;
        mCurThread = new Thread(this);
        mCurThread.start();
    }

    public void stopServer() {
        LogUtil.e("SocketConnect ----> stopServer");
        mRunning = false;
        mCurThread.interrupt();
        for (Thread thread: mWriteThreads) {
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            LogUtil.e("SocketConnect ----> 启动服务");
//            LocalServerSocket server = new LocalServerSocket("com.playin.audio.localsocket");

            OutputStream os = null;
            while (mRunning) {
//                LocalSocket localSocket = server.accept();
//                LogUtil.e("SocketConnect ----> client连接成功");
//                Thread thread = new WriteThread(localSocket);
//                thread.start();
//                mWriteThreads.add(thread);

                if (null != saveFile && os == null) {
                    os = new FileOutputStream(saveFile);
                }
                if (null != os) {
                    LogUtil.e("音频数据 ----> 写入成功");
                    os.write(voiceQueue.take());
                    os.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class WriteThread extends Thread {

        private LocalSocket localSocket;

        public WriteThread(LocalSocket localSocket) {
            this.localSocket = localSocket;
        }

        @Override
        public void run() {
            OutputStream os = null;
            try {
                os = localSocket.getOutputStream();
                while (!isInterrupted()) {
                    os.write(voiceQueue.take());
                }
            } catch (Exception e) {
                LogUtil.e("SocketConnect::WriteThread Exception: " + e);
            } finally {
                try {
                    os.close();
                    localSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
