package com.playin.hook;

import android.content.Context;
import android.util.Log;

import com.playin.hook.util.Constants;

public class Test {

    private Context context;
    private String name;
    private AudioHook n;

    public Test(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    public void print() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(Constants.TAG,  System.currentTimeMillis() + " Test: context: " + context + "  name: " + name);

                }
            }
        }).start();
    }
}
