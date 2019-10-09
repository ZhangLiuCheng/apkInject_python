package com.playin.util;

import android.net.LocalServerSocket;
import android.net.LocalSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        LogUtil.e("SocketConnect::音频参数 ----> streamType: " + streamType + "  " + "sampleRateInHz: " + sampleRateInHz + "  " + "channelConfig: " + channelConfig +
                "  " + "audioFormat: " + audioFormat + "   " + "bufferSizeInBytes: " + bufferSizeInBytes);
        JSONObject obj = new JSONObject();
        try {
            obj.put("streamType", streamType);
            obj.put("sampleRateInHz", sampleRateInHz);
            obj.put("channelConfig", channelConfig);
            obj.put("audioFormat", audioFormat);
            obj.put("bufferSizeInBytes", bufferSizeInBytes);
            voiceQueue.offer(getSendData(0, obj.toString().getBytes()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    byte[] printBuf = new byte[5];
    public void sendData(byte[] buf, int offsetInBytes, int sizeInBytes) {
        if (buf == null || buf.length < 5 || (buf[0] == 0 && buf[1] == 0 && buf[2] == 0 && buf[3] == 0 && buf[4] == 0)) {
            return;
        }
//        if (voiceQueue.size() > 90) {
//            try {
//                voiceQueue.take();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        System.arraycopy(buf, 0, printBuf, 0, printBuf.length);
        LogUtil.i("SocketConnect::音频前5个数据 ----> " + Arrays.toString(printBuf));
        boolean flag = voiceQueue.offer(getSendData(1, buf));
        if (flag == false) {
            LogUtil.e("SocketConnect::发送队列已满，丢弃音频数据 " + voiceQueue.size());
        }
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
        for (Thread thread : mWriteThreads) {
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            LogUtil.e("SocketConnect:: run ----> 启动服务");
            testCommonSocket();
            LocalServerSocket server = new LocalServerSocket("com.playin.audio.localsocket");
            while (mRunning) {
                LocalSocket localSocket = server.accept();
                LogUtil.e("SocketConnect ----> client连接成功  ");
                interruptOtherThread();
                Thread thread = new LocalSocketWriteThread(localSocket);
                thread.start();
                mWriteThreads.add(thread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testCommonSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    if (null != saveFile && os == null) {
//                        os = new FileOutputStream(saveFile);
//                    }
//                    if (null != os) {
//                        LogUtil.e("音频数据 ----> 写入成功");
//                        os.write(voiceQueue.take());
//                        os.flush();
//                    }


                    ServerSocket server = new ServerSocket(55555);
                    while (mRunning) {
                        Socket socket = server.accept();
                        socket.setKeepAlive(true);
                        socket.setSendBufferSize(1024*30);
                        socket.setTcpNoDelay(true);
                        interruptOtherThread();
                        Thread thread = new CommonSocketWriteThread(socket);
                        thread.start();
                        mWriteThreads.add(thread);
                    }


//                    Socket socket = new Socket("172.20.10.3", 55555);
//                    socket.setSoTimeout(0);
//                    socket.setReceiveBufferSize(1024*30);
//                    socket.setTcpNoDelay(true);
//                    Thread thread = new CommonSocketWriteThread(socket);
//                    thread.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    // 只能有一个连接
    private void interruptOtherThread() {
        for (Thread thread : mWriteThreads) {
            thread.interrupt();
        }
        mWriteThreads.clear();
    }

    private class LocalSocketWriteThread extends Thread {
        private LocalSocket localSocket;

        public LocalSocketWriteThread(LocalSocket localSocket) {
            this.localSocket = localSocket;
        }

        @Override
        public void run() {
            OutputStream os = null;
            LogUtil.e("SocketConnect::LocalSocketWriteThread  ---->  run ");

            try {
                os = localSocket.getOutputStream();
                while (true) {
                    byte[] buf = voiceQueue.take();
                    LogUtil.e("SocketConnect::LocalSocketWriteThread  ---->  音频数据发送成功 " + buf.length);
                    os.write(buf);
                }
            } catch (Exception e) {
                LogUtil.e("SocketConnect::LocalSocketWriteThread  ---->  Exception: " + e);
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

    private class CommonSocketWriteThread extends Thread {
        private Socket socket;

        public CommonSocketWriteThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            OutputStream os = null;
            LogUtil.e("SocketConnect::CommonSocketWriteThread  ---->  run ");
            try {
                os = socket.getOutputStream();
                while (true) {
                    byte[] buf = voiceQueue.take();
                    LogUtil.e("SocketConnect::CommonSocketWriteThread  ---->  音频数据发送成功 " + buf.length);
                    os.write(buf);
                }
            } catch (Exception e) {
                LogUtil.e("SocketConnect::CommonSocketWriteThread  ---->  Exception: " + e);
            } finally {
                try {
                    os.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] getSendData(int type, byte[] src) {
        byte[] buf = new byte[4 + 1 + src.length];
        byte[] lenBuf = intToBytes(1 + src.length);
        System.arraycopy(lenBuf, 0, buf, 0, lenBuf.length);
        buf[4] = (byte) type;
        System.arraycopy(src, 0, buf, 5, src.length);
        return buf;
    }

    private byte[] intToBytes(int num) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(num).array();
    }
}
