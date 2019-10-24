package com.playin.hook;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.playin.util.SocketConnect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class AudioHook {

    private final static String TAG = "AUDIO_HOOK";

    public static void init(Application application) {
        Log.e(TAG, "AudioHook     init   " + application);


        AutoContorl.start(application);
        // 通过HoolJava方式获取音频，需要启动服务
//        SocketConnect.getInstance().startServer();

        // apk
//        wayApk();

        // 注入
//        wayInject(application.getBaseContext());

        /*
        File file = application.getExternalFilesDir("audio");
        File f = new File(file, "helix_crush.pcm");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SocketConnect.getInstance().setSaveFile(f);
         */
    }

    private static void wayApk() {
        new HookNative().doHook();
    }

    private static void wayInject(final Context context) {
        Log.e(TAG, "context  " + context);
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final File apkFile = copyAssetsFile(context, "audio_hook.apk", context.getExternalFilesDir("hook"));
                Log.e(TAG, "apkFile " + apkFile);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (apkFile == null) {
                            Toast.makeText(context, "拷贝audio_hook.apk失败", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!invokeHook(context, apkFile)) {
                                Toast.makeText(context, "调用Hook方法失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private static boolean invokeHook(Context context, File apkFile) {
        try {
            ClassLoader classLoader = context.getClassLoader();
            DexClassLoader dexClassLoader = new DexClassLoader(apkFile.getAbsolutePath(), context.getCodeCacheDir().getAbsolutePath(), null, classLoader);
            Class<?> hookItem = Class.forName("com.playin.hook.HookNative", true, dexClassLoader);
            Object t = hookItem.newInstance();
            for (Method method : hookItem.getDeclaredMethods()) {
                if (method.getName().contains("doHook")) {
                    method.invoke(t, classLoader);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static File copyAssetsFile(Context context, String fileName, File path) {
        InputStream is = null;
        FileOutputStream os = null;
        if (!path.exists()) path.mkdirs();
        File apkFile = new File(path + File.separator + "audio_hook.apk");
        if (apkFile.exists()) {
            return apkFile;
        }
        try {
            is = context.getAssets().open(fileName);
            apkFile.createNewFile();
            Log.e(TAG, "开始拷贝apk");
            os = new FileOutputStream(apkFile);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = is.read(buf)) > 0) {
                os.write(buf, 0, i);
            }
            Log.e(TAG, "拷贝apk完毕");
        } catch (Exception e) {
            apkFile.delete();
            return null;
        } finally {
            try {
                if (is != null) is.close();
                if (os != null) os.close();
            } catch (Exception ex) {
            }
        }
        return apkFile;
    }
}
