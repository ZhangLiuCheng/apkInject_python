package com.playin.hook;

import android.content.Context;

public class AudioHook {

    public static void init(Context context) {
        Test test = new Test(context, "AudioHook Test");
        test.print();
    }
}
