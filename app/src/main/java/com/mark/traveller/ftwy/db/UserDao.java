package com.mark.traveller.ftwy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mark.traveller.ftwy.bean.User;
import com.mark.traveller.ftwy.utils.DataConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 对数据库操作的封装
 * Created by Mark on 2016/11/15 0015.
 */

public class UserDao {
    private DBHelper mDBHelper;

    public UserDao(Context context) {
        mDBHelper = new DBHelper(context, DataConstants.DBNAME, null, 1);
    }

    // 声明一个当前类的对象
    private static UserDao sUserDao;

    // 提供一个静态方法,如果当前类的对象为空,创建一个新的
    public static UserDao getInstance(Context context) {
        if (sUserDao == null) {
            sUserDao = new UserDao(context);
        }
        return sUserDao;
    }

    /**
     * 插入字段
     *
     * @param user
     */
    public void insert(User user) {
        // 获取到数据库的操作对象
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        // 第一种方法
//        String sql = "insert into user(_id,phone,password,name) values(?,?,?,?))";
//        db.execSQL(sql, new Object[]{user.getId(), user.getPhone(), user.getPassword(), user.getName()});
//        db.setTransactionSuccessful();
//        db.endTransaction();

        // 第二种方法
        ContentValues values = new ContentValues();
        values.put("_id", user.getId());
        values.put("phone", user.getPhone());
//        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        db.insert("user", null, values);

        db.close();
    }

    /**
     * 根据phone删除该行
     *
     * @param phone
     */
    public void delete(String phone) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

//        String sql = "delete from user where phone=?";
//        db.execSQL(sql, new Object[]{phone});

        // 第二种方法
        db.delete(DataConstants.DBTABLE_NAME, "phone = ?", new String[]{phone});

        db.close();
    }

    /**
     * 改
     *
     * @param user
     */
    public void update(User user) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

//        String sql = "update user set phone=?,password=? where " + "phone=?";
//        db.execSQL(sql, new Object[]{user.getPhone(), user.getPassword(), user.getId()});

        // 第二种方法

        ContentValues values = new ContentValues();
        values.put("_id", user.getId());
        values.put("phone", user.getPhone());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        db.update(DataConstants.DBTABLE_NAME, values, "phone = ?", new String[]{user.getPhone()});

        db.close();
    }

    /**
     * 根据phone查找到记录
     *
     * @param phone
     * @return
     */
    public User find(String phone) {
        User user = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String sql = "select * from user where phone=?";
        Cursor cursor = db.rawQuery(sql, new String[]{phone});
        if (cursor.moveToFirst()) {
            // 依次取出
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
//            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
        }

        db.close();
        return user;
    }

    /**
     * 查询所有的记录
     *
     * @return
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        User user = null;

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user", null);
        while (cursor.moveToNext()) {
            // 依次取出
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
//            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));

            users.add(user);
        }

        cursor.close();
        db.close();
        return users;
    }

    /**
     * 统计所有记录数量
     *
     * @return
     */
    public long getCount() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from user ", null);
        cursor.moveToFirst();

        db.close();
        return cursor.getLong(0);
    }

}
