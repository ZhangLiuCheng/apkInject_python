package com.tech.playinsdk.webrtc.touch;

import android.app.Instrumentation;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tech.playinsdk.webrtc.R;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ServerActivity extends AppCompatActivity {

    private static final String TAG = "SERVER";

    private Instrumentation inst = new Instrumentation();
    private TextView message;

    private ServerSocket serverSocket;
    private Thread connectThread;
    private boolean connectFlag;

    private ReadThread readThread;

    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        message = findViewById(R.id.message);
        message.setMovementMethod(ScrollingMovementMethod.getInstance());

        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
    }

    public void startServer(View view) {
        appendMessage("启动ServerSocket\n");

        connectFlag = true;
        connectThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8080);
                while (connectFlag) {
                    Socket socket = serverSocket.accept();
                    appendMessage("新的socket接入\n");
                    socket.setSoTimeout(0);
                    socket.setReceiveBufferSize(1024 * 1024);
                    socket.setTcpNoDelay(true);
                    processSocket(socket);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        connectThread.start();
    }

    public void stopServer(View view) {
        appendMessage("停止ServerSocket");
        try {
            serverSocket.close();
            connectFlag = false;
            connectThread.interrupt();
            closeReadSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendMessage(String msg) {
        runOnUiThread(() -> {
            message.append(msg);
        });
    }

    private void processSocket(Socket socket) {
        closeReadSocket();
        readThread = new ReadThread(socket);
        readThread.startSocket();
    }

    private void closeReadSocket() {
        if (null != readThread) {
            readThread.stopSocket();
        }
    }

    private void restoreEvent(String eventStr) {
        MotionEvent event = InjectEvents.parseMotionEventsMultipoint(width, height, eventStr);
        inst.sendPointerSync(event);
    }

    private class ReadThread extends Thread {

        private Socket socket;
        private boolean flag;

        public ReadThread(Socket socket) {
            this.socket = socket;
        }

        public void startSocket() {
            flag = true;
            start();
        }

        public void stopSocket() {
           flag = false;
           interrupt();
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                byte[] lenBuf = new byte[4];
                while (flag) {
                    dis.read(lenBuf);
                    int touchLen = Util.bytesToInt(lenBuf);
                    byte[] touchBuf = new byte[touchLen];
                    dis.read(touchBuf);

                    appendMessage("收到新消息:" + touchBuf.length + "\n");
                    Log.e(TAG, Arrays.toString(touchBuf));

                    restoreEvent(new String(touchBuf));
                }
                dis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
