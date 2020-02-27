package com.tech.playinsdk.webrtc.touch;

import android.os.SystemClock;
import android.view.InputDevice;
import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;

public class InjectEvents {

    /**
     * 将服务器触摸手势转成MotionEvents,包括多点触控功能。
     * @param destWidth
     * @param destHeight
     * @param controlStr
     * @return MotionEvent。如果格式不标准，有可能返回null。
     */
    public static MotionEvent parseMotionEventsMultipoint(float destWidth, float destHeight, String controlStr) {
        try {
            List<Touch> touches = controlStrToList(destWidth, destHeight, controlStr);
            if (touches.size() > 1) {
                if (controlStr.contains("0_0_0")) {
                    int index = indexTouches(touches, 0);
                    int action = index << 8 | ACTION_POINTER_DOWN;
                    modifyTouchAction(touches, action);
                } else if (controlStr.contains("2_0_0")) {
                    int index = indexTouches(touches, 2);
                    int action = index << 8 | ACTION_POINTER_UP;
                    modifyTouchAction(touches, action);
                }
            }
            return touchesToEvent(touches);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 查询action在touches的位置
    private static int indexTouches(List<Touch> touches, int action) {
        int index = 0;
        for (int i = 0; i < touches.size(); i++) {
            if (touches.get(i).action == action) {
                index = i;
                break;
            }
        }
        return index;
    }


    // 控制转List
    private static List<Touch> controlStrToList(float destWidth, float destHeight, String controlStr) throws JSONException {
        List<Touch> touches = new ArrayList<>();
        JSONObject obj = new JSONObject(controlStr);
        Iterator it = obj.keys();
        while (it.hasNext()) {
            String key = it.next().toString();
            String value = obj.optString(key);
            String[] commands = value.split("_");
            float x = Float.parseFloat(commands[0]) * destWidth;
            float y = Float.parseFloat(commands[1]) * destHeight;
            int action = Integer.parseInt(commands[2]);
            touches.add(new Touch(key, x, y, action));
        }
        return touches;
    }

    // touches 转 motionEvent
    private static MotionEvent touchesToEvent(List<Touch> touches) {
        int pointerCount = touches.size();
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[pointerCount];
        MotionEvent.PointerCoords[] coords = new MotionEvent.PointerCoords[pointerCount];
        for (int i = 0; i < pointerCount; i++) {
            Touch touch = touches.get(i);
            MotionEvent.PointerProperties pp = new MotionEvent.PointerProperties();
            pp.id = Integer.parseInt(touch.fId);
            properties[i] = pp;
            MotionEvent.PointerCoords pc = new MotionEvent.PointerCoords();
            pc.x = touch.coordX;
            pc.y = touch.coordY;
            coords[i] = pc;
        }
        int action = touches.get(0).action;
        if (action == 1) {
            action = 2;
        } else if (action == 2) {
            action = 1;
        }
        return MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, pointerCount, properties,
                coords, 0, 0, 1, 1, 0, 0, InputDevice.SOURCE_TOUCHSCREEN, 0);
    }

    private static void modifyTouchAction(List<Touch> touches, int action) {
        for (int i = 0; i < touches.size(); i++) {
            touches.get(i).action = action;
        }
    }

    private static class Touch {
        String fId;
        float coordX;
        float coordY;
        int action;

        public Touch(String fId, float coordX, float coordY, int action) {
            this.fId = fId;
            this.coordX = coordX;
            this.coordY = coordY;
            this.action = action;
        }
    }
}