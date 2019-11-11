package com.playin.hook;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.playin.util.LogUtil;
import com.playin.util.SimilarImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;

public class AutoContorl {

    public static void start(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateStatus(context, "PREPARE");
                try {
                    File rootFile = new File(getSaveFile(context), "control");
                    String meg = rootFile.exists() ? " 存在 " : "不存在";
                    LogUtil.e("========> " + meg + "  " + rootFile.getAbsolutePath());
                    if (!rootFile.exists()) return;
                    JSONObject configObj = getControlStrs(rootFile);
                    if (null == configObj) {
                        LogUtil.e("========>  解析配置文件失败，请检查配置文件是否是标准的JSON格式");
                        return;
                    }
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime <= configObj.optInt("duration")) {
                        boolean result = processControl(context, rootFile, configObj);
                        if (result) {
                            updateStatus(context, "READY");
                            break;
                        }
                        Thread.sleep(configObj.optInt("interval"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @param status "PREPARE" or "READY",
     */
    private static void updateStatus(Context context, String status) {
        LogUtil.e("========>  当前包名 " + context.getPackageName());

        File statusFile = new File(context.getFilesDir(), "STATUS");
        try {
            if (!statusFile.exists()) {
                statusFile.createNewFile();
            }
            JSONObject obj = new JSONObject();
            obj.put("game", context.getPackageName());
            obj.put("status", status);
            obj.put("timestamp", System.currentTimeMillis());
            FileWriter fileWriter = new FileWriter(statusFile, false);
            fileWriter.write(obj.toString());
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("========>  更新状态失败 " + e.toString());
        }
    }

    private static File getSaveFile(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }

    private static JSONObject getControlStrs(File rootFile) {
        try {
            File controlFile = new File(rootFile, "des.txt");
            if (!controlFile.exists()) return null;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(controlFile));
            String line = br.readLine();
            while (null != line && !line.isEmpty()) {
                sb.append(line);
                line = br.readLine();
            }
            return new JSONObject(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static boolean processControl(Context context, File rootFile, JSONObject configObj) {
        try {
            File capImgFile = screencap(context);
            if (capImgFile == null) return true;

            JSONArray gameScreenArray = configObj.optJSONArray("gameScreens");
            // 比较是否进入游戏
            if (null != gameScreenArray) {
                for (int i = 0; i < gameScreenArray.length(); i++) {
                    JSONObject gameScreenObj = gameScreenArray.optJSONObject(i);
                    File gameFile = new File(rootFile, gameScreenObj.optString("image"));
                    int gameResult = SimilarImage.compare(capImgFile, gameFile);
                    LogUtil.e("========>  游戏页面比较结果:  " + gameScreenObj.optString("image") + " : " + gameResult);
                    if (gameResult < gameScreenObj.optInt("level")) {
                        if (gameScreenObj.optInt("interrupt") > 0) {
                            return true;
                        }
                    }
                }
            }

            // 比较控制点击
            JSONArray ctrolArray = configObj.optJSONArray("controls");
            for (int i = 0; i < ctrolArray.length(); i++) {
                JSONObject ctrolObj = ctrolArray.optJSONObject(i);
                File oriImgFile = new File(rootFile, ctrolObj.optString("image"));
                int result = SimilarImage.compare(capImgFile, oriImgFile);
                LogUtil.e("========>  控制图片比较结果:  " + ctrolObj.optString("image") + " : " + result);
                if (result < ctrolObj.optInt("level")) {
                    Thread.sleep(ctrolObj.optInt("delay"));
                    perfomClick(ctrolObj.optInt("x"), ctrolObj.optInt("y"));
                    if (ctrolObj.optInt("interrupt") > 0) {
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
            LogUtil.e("========>  perfomClick  offetX: " + offetX + "  offetY: " + offetY);
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
        File tmpImg = new File(getSaveFile(context), "tmp.jpeg");
        try {
            Process sh = Runtime.getRuntime().exec("su", null,null);
            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + tmpImg.getAbsolutePath()).getBytes("ASCII"));
            os.flush();
            os.close();
            sh.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("========>  截图失败");
            return null;
        }
        LogUtil.e(" ========>  截图成功 " + tmpImg.getAbsolutePath());
        return tmpImg;
    }
}
