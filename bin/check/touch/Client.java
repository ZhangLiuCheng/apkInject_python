package com.tech.playinsdk.webrtc.touch;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tech.playinsdk.webrtc.R;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientActivity extends AppCompatActivity {

    private ArrayBlockingQueue<byte[]> sendQueue = new ArrayBlockingQueue<>(100);

    private static final String TAG = "CLIENT";

    private Thread thread;
    private boolean flag;

    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String str = ConvertEvents.processTouchEvent(event, width, height);
        byte[] touchBuf = str.getBytes();
        int touchLen = touchBuf.length;
        byte[] wrap = new byte[4 + touchLen];
        byte[] lenBuf = Util.intToBytes(touchLen);
        System.arraycopy(lenBuf, 0, wrap, 0, lenBuf.length);
        System.arraycopy(touchBuf, 0, wrap, 4, touchBuf.length);
        sendQueue.offer(wrap);
        return true;
    }

    public void startConnect(View view) {
        flag = true;
        thread = new Thread(() -> {
            try {
                Log.e(TAG, "开始连接");
                Socket socket = new Socket("54.168.94.124", 8080);
                socket.setSoTimeout(0);
                socket.setSendBufferSize(1024 * 1024);
                socket.setTcpNoDelay(true);
                Log.e(TAG, "连接成功");

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                while (flag) {
                    byte[] buf = sendQueue.take();
                    dos.write(buf);
                    Log.e(TAG, "发送数据");
                }
                dos.close();
                os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    public void stopConnect(View view) {
        try {
            flag = false;
            thread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
