package com.islavdroid.reminder;

import android.app.Application;


public class MyApplication extends Application {
    //в этом классе выясняем приложение запущено или работает в фоне
    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }
    public static void activityResumed(){
        activityVisible =true;
    }
    public static void activityPaused(){
        activityVisible=false;
    }

}
