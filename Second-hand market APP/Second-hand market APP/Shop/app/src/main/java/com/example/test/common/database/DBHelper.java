package com.example.test.common.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper { //实现数据库 继承

    //二手商品信息表
    private String productSql = "create table tbl_product (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "describe TEXT, " +
            "old_price TEXT, " +
            "new_price TEXT, " +
            "who TEXT, " +
            "ext TEXT"+
            ")";

    //用户表
    private String userSql = "create table tbl_user (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_name TEXT, " +
            "password TEXT, " +
            "ext TEXT"+
            ")";

    //订单表
    private String orderSql = "create table tbl_order (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "order_name TEXT, " +
            "productStr TEXT, " +
            "time TEXT, " +
            "who TEXT, " +
            "ext TEXT"+
            ")";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(userSql);
        db.execSQL(productSql);
        db.execSQL(orderSql);
    }


    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        // TODO Auto-generated method stub
        return super.getReadableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        // TODO Auto-generated method stub
        return super.getWritableDatabase();
    }

    /**
     * 数据库升级
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        /*for(int j = oldVersion + 1;j <= newVersion ;j++){
            switch (j) {
                case 2:
                    db.execSQL("DROP TABLE IF EXISTS "+ServerConstant.TBL_NOTICE+";");
                    db.execSQL(noticeSql);
                    break;
                default:
                    break;
            }
        }*/
    }


    /**
     * 数据库降级
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}
