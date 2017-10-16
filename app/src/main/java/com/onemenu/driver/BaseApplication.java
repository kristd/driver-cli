package com.onemenu.driver;

import android.app.Application;
import android.util.Log;

public class BaseApplication extends Application {
    static String TAG = "BaseApplication";
    static MainActivity mainActivity = null;
    static BaseApplication application = null;

    public static void NewInst() {
        application = new BaseApplication();
    }

    private BaseApplication() {
        super();
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        BaseApplication.mainActivity = mainActivity;
    }
}
