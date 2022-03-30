package com.example.test;

import android.content.Context;

import com.example.test.common.database.DBHelper;
import com.example.test.model.Product;
import com.example.test.model.User;

public class TestContext {
    private static TestContext mDemoContext;
    public Context mContext;
    private User currentUser;
    public DBHelper dbHelper ;
    private TestApplication application;

    public static TestContext getInstance() {
        return mDemoContext;
    }

    public static void init(Context context) {
        if (mDemoContext == null) {
            mDemoContext = new TestContext(context);
        }
    }

    public Context getmContext() {
        return mContext;
    }

    private TestContext(Context context) {
        mContext = context;
        application = (TestApplication) context.getApplicationContext();
        dbHelper = new DBHelper(mContext, "calculator.db3", null, 1);
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public TestApplication getApplication() {
        return application;
    }

    public void setApplication(TestApplication application) {
        this.application = application;
    }

    //获取登录用户信息
    public User getCurrentUser() {
        return currentUser;
    }


    //缓存登录用户信息
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
