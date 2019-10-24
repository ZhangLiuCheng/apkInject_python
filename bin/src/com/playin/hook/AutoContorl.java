package com.playin.hook;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.playin.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AutoContorl {

    public static void start(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    File controlFile = new File(file, "control.txt");
                    String meg = controlFile.exists() ? " 存在 " : "不存在";
                    LogUtil.e("============> " + meg + "  " + controlFile.getAbsolutePath());

                    if (!controlFile.exists()) {
                        return;
                    }
                    Instrumentation inst = new Instrumentation();
                    BufferedReader br = new BufferedReader(new FileReader(controlFile));
                    String line = br.readLine();
                    while (null != line && !line.isEmpty()) {
                        process(inst, line);
                        line = br.readLine();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private static void process(Instrumentation inst, String line) throws InterruptedException {
        String[] timeCoord = line.split("-");
        int time = Integer.parseInt(timeCoord[0]);
        String[] coord = timeCoord[1].split(":");
        Thread.sleep(time);
        int offetX = Integer.parseInt(coord[0]);
        int offetY = Integer.parseInt(coord[1]);
        LogUtil.e(" ========>  time: " + time + "  offetX: " + offetX + "  offetY: " + offetY);
        MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, offetX, offetY, 0);
        inst.sendPointerSync(event);
        MotionEvent event1 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP, offetX, offetY, 0);
        inst.sendPointerSync(event1);
    }
}
