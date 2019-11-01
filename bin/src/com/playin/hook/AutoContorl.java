package com.playin.hook;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.playin.util.LogUtil;
import com.playin.util.SimilarImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AutoContorl {

    public static void start(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File rootFile = getControlFile(context);
                    String meg = rootFile.exists() ? " 存在 " : "不存在";
                    LogUtil.e("============> " + meg + "  " + rootFile.getAbsolutePath());
                    if (!rootFile.exists()) return;
                    List<String> ctrolStrs = getControlStrs(rootFile);
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime <= 60000 * 2) {
                        boolean result = processControl(context, rootFile, ctrolStrs);
                        if (result) break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private static File getControlFile(Context context) {
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File controlFile = new File(file, "control");
        return controlFile;
    }

    private static List<String> getControlStrs(File rootFile) {
        try {
            File controlFile = new File(rootFile, "des.txt");
            if (!controlFile.exists()) return null;
            List<String> strs = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(controlFile));
            String line = br.readLine();
            while (null != line && !line.isEmpty()) {
                strs.add(line);
                line = br.readLine();
            }
            return strs;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static boolean processControl(Context context, File rootFile, List<String> ctrolStrs) {
        try {
            File capImgFile = screencap(context);
            if (capImgFile == null) return true;
            for (int i = 0; i < ctrolStrs.size(); i++) {
                String[] params = ctrolStrs.get(i).split("-");
                String[] imgPro = params[0].split(":");
                File oriImgFile = new File(rootFile, imgPro[0]);
                int result = SimilarImage.compare(capImgFile, oriImgFile);
                LogUtil.e("图片比较结果:  " + imgPro[0] + " : " + result);
                if (result < Integer.parseInt(imgPro[1])) {
                    String[] coord = params[1].split(":");
                    perfomClick(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
                    if (Integer.parseInt(params[2]) > 0) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static void perfomClick(int offetX, int offetY) {
        try {
            Instrumentation inst = new Instrumentation();
            LogUtil.e("perfomClick ========>    offetX: " + offetX + "  offetY: " + offetY);
            MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN, offetX, offetY, 0);
            inst.sendPointerSync(event);
            MotionEvent event1 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_UP, offetX, offetY, 0);
            inst.sendPointerSync(event1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static File screencap(Context context) {
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
//        File tmpImg = new File(file, "control_tmp" + System.currentTimeMillis() + ".jpeg");
        File tmpImg = new File(file, "tmp.jpeg");
        try {
            Process sh = Runtime.getRuntime().exec("su", null,null);
            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + tmpImg.getAbsolutePath()).getBytes("ASCII"));
            os.flush();
            os.close();
            sh.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(" ========>  截图失败");
            return null;
        }
        LogUtil.e(" ========>  截图成功 " + tmpImg.getAbsolutePath());
        return tmpImg;
    }
}
