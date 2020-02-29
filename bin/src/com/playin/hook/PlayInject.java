package com.playin.hook;

import android.content.Context;

import com.playin.util.LogUtil;

public class PlayInject {

    public static void init(Context context) {
        LogUtil.e("PlayInject::init ==============>  " + context);
        if (context == null) {
            return;
        }

        // 自动点击游戏
        AutoContorl.start(context);

        // 通过HoolJava方式获取音频，需要启动服务
//        SocketConnect.getInstance().startServer();

        // 通过FastHook获取声音
//      new HookNative().doHook();
    }
}
