package com.playin.hook;

import android.util.Log;

import pers.turing.technician.fasthook.FastHookCallback;
import pers.turing.technician.fasthook.FastHookManager;
import pers.turing.technician.fasthook.FastHookParam;

public class HookNative implements FastHookCallback {

    private final static String TAG = "AUDIO_HOOK";

    public void doHook() {
        Log.e(TAG, "HookNative:  -------> doHook  begin");
        String methodSig = "([BII)I";
        FastHookManager.doHook("android.media.AudioTrack", null, "write",
                methodSig, this, FastHookManager.MODE_REWRITE, true);
        Log.e(TAG, "HookNative:  -------> doHook   end");
    }

    @Override
    public void beforeHookedMethod(FastHookParam param) {
        Log.e(TAG, "HookNative:  -------> beforeHookedMethod");
    }

    @Override
    public void afterHookedMethod(FastHookParam param) {
        Log.e(TAG, "HookNative:  -------> afterHookedMethod");

        try {
            byte[] audioData = (byte[]) param.args[0];
            int offsetInBytes = (int) param.args[1];
            int sizeInBytes = (int) param.args[2];
//            Log.e(TAG, "AudioTrackHook:  " + Arrays.toString(audioData));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
