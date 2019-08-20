package com.playin.hook;

import android.app.Application;

public class AudioHook {

    public static void init(Application application) {
        Test test = new Test(application.getApplicationContext(), "AudioHook Test");
        test.print();
    }
}
