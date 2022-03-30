package com.example.test.common.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.test.TestContext;
import com.example.test.common.constant.ServerConstant;
import com.example.test.model.Order;
import com.example.test.model.Product;
import com.example.test.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DatabaseUtils {

    //获取用户订单
    public static ArrayList<Order> getOrderList() {
        ArrayList<Order> details = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            sql = "select * from "+ ServerConstant.TBL_ORDER+" where who ='"+TestContext.getInstance().getCurrentUser().get_id()+"'";
            cursor = db.rawQuery(sql,null);
            if(cursor.getCount() > 0 ){
                Gson gson = new Gson();
                while(cursor.moveToNext()){
                    Order detail = new Order();
                    detail.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                    detail.setOrder_name(cursor.getString(cursor.getColumnIndex("order_name")));
                    detail.setTime(cursor.getString(cursor.getColumnIndex("time")));
                    detail.setProductStr(cursor.getString(cursor.getColumnIndex("productStr")));
                    Product product = gson.fromJson(detail.getProductStr(),Product.class);
                    detail.setProduct(product);
                    details.add(detail);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return details;
        }finally{
            if(cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return details;
    }

    //保存用户订单
    public static long saveOrder(String order_name, String productStr, String time){
        long result = -1;
        Cursor cursor = null;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("order_name",order_name);
            cv.put("productStr",productStr);
            cv.put("time",time);
            cv.put("who",TestContext.getInstance().getCurrentUser().get_id());
            result = db.insert(ServerConstant.TBL_ORDER, null, cv);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //删除用户订单
    public static long deleteOrder(int _id){
        long result = -1;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            result = db.delete(ServerConstant.TBL_ORDER,"_id = ?",new String[]{String.valueOf(_id)});
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    //获取二手商品信息
    public static ArrayList<Product> getProductInfoList() {
        ArrayList<Product> details = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            sql = "select * from "+ ServerConstant.TBL_PRODUCT+" where who ='"+TestContext.getInstance().getCurrentUser().get_id()+"'";
            cursor = db.rawQuery(sql,null);
            if(cursor.getCount() > 0 ){
                while(cursor.moveToNext()){
                    Product detail = new Product();
                    detail.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                    detail.setName(cursor.getString(cursor.getColumnIndex("name")));
                    detail.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                    detail.setNew_price(cursor.getString(cursor.getColumnIndex("old_price")));
                    detail.setOld_price(cursor.getString(cursor.getColumnIndex("new_price")));
                    details.add(detail);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return details;
        }finally{
            if(cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return details;
    }

    public static ArrayList<Product> getProductInfoList2() {
        ArrayList<Product> details = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            sql = "select * from "+ ServerConstant.TBL_PRODUCT;
            cursor = db.rawQuery(sql,null);
            if(cursor.getCount() > 0 ){
                while(cursor.moveToNext()){
                    Product detail = new Product();
                    detail.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                    detail.setName(cursor.getString(cursor.getColumnIndex("name")));
                    detail.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                    detail.setNew_price(cursor.getString(cursor.getColumnIndex("old_price")));
                    detail.setOld_price(cursor.getString(cursor.getColumnIndex("new_price")));
                    details.add(detail);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return details;
        }finally{
            if(cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return details;
    }

    //保存二手商品信息
    public static long saveProduct(String name, String describe, String old_price, String new_price){
        long result = -1;
        Cursor cursor = null;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("name",name);
            cv.put("describe",describe);
            cv.put("old_price",old_price);
            cv.put("new_price",new_price);
            cv.put("who",TestContext.getInstance().getCurrentUser().get_id());
            result = db.insert(ServerConstant.TBL_PRODUCT, null, cv);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //删除二手商品信息
    public static long deleteProduct(int _id){
        long result = -1;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            result = db.delete(ServerConstant.TBL_PRODUCT,"_id = ?",new String[]{String.valueOf(_id)});
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //编辑二手商品信息
    public static long saveProduct(Product device){
        long result = -1;
        Cursor cursor = null;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("name",device.getName());
            cv.put("describe",device.getDescribe());
            cv.put("old_price",device.getNew_price());
            cv.put("new_price",device.getOld_price());
            result = db.update(ServerConstant.TBL_PRODUCT,cv,"_id = ?",new String[]{String.valueOf(device.get_id())});
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }



    //登录时，获取用户信息，进行校验
    public static User getUserInfo(String user_name, String password) {
        User detail = null;
        Cursor cursor = null;
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            sql = "select * from "+ ServerConstant.TBL_USER+" where  user_name = '"+user_name+"' and password = '"+ password+"'";
            cursor = db.rawQuery(sql,null);
            if(cursor.getCount() > 0 ){
                while(cursor.moveToNext()){
                    detail = new User();
                    detail.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                    detail.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
                    detail.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return detail;
        }finally{
            if(cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return detail;
    }

    //返回值   -2 ：代表用户名已经存在  -1：代表未知错误  其他值代表保存成功
    public static long saveUser(String user_name, String password){
        long result = -1;
        Cursor cursor = null;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            sql = "select * from "+ ServerConstant.TBL_USER+" where  user_name = '"+user_name+"'";
            cursor = db.rawQuery(sql,null);
            if(cursor.getCount() > 0 ){
                return -2;
            }
            ContentValues cv = new ContentValues();
            cv.put("user_name",user_name);
            cv.put("password",password);
            result = db.insert(ServerConstant.TBL_USER, null, cv);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static long deleteUser(int _id){
        long result = -1;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            String sql = null;
            result = db.delete(ServerConstant.TBL_USER,"_id = ?",new String[]{String.valueOf(_id)});
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static long updateUser(String password,int id){
        long result = -1;
        Cursor cursor = null;
        //数据库缓存
        SQLiteDatabase db = TestContext.getInstance().dbHelper.getReadableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("password",password);
            result = db.update(ServerConstant.TBL_USER,cv,"_id = ?",new String[]{String.valueOf(id)});
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
