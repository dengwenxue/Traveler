package com.mark.traveller.ftwy.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库的helper
 * Created by Mark on 2016/11/15 0015.
 */

public class DBHelper extends SQLiteOpenHelper {


    /**
     * 上下文
     *
     * @param context 数据库名
     * @param name    可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标。默认为null。
     * @param factory 数据库版本号
     * @param version
     */
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 上下文
     *
     * @param context      数据库名
     * @param name         可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标。默认为null。
     * @param factory      数据库版本号
     * @param version
     * @param errorHandler
     */
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * 第一次创建数据库的时候调用，而且是得到readableDatabase或者writeDatabase时才执行
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table user(_id integer primary key autoincrement , phone varchar(20), password varchar(20), name varchar(20))";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
