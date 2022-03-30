package com.example.test;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

public class TestApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        //注册消息类型的时候判断当前的进程是否在主进程
        if(isMainProcess()){
            TestContext.init(this);
        }
    }

    //判断是否是主进程
    public boolean isMainProcess(){
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos =  am.getRunningAppProcesses();
        int myPid = android.os.Process.myPid();
        String mainProcessName = getPackageName();
        for(ActivityManager.RunningAppProcessInfo info :processInfos){
            if(myPid == info.pid&&mainProcessName.equals(info.processName)){
                return true;
            }
        }
        return false;
    }
}
