package com.lotte.android_notepad.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyNoteDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "my_notepad1";//表名
    public static final String ID = "_id";//id
    public static final String TIME = "time";//时间
    public static final String TITLE = "title";//标题
    public static final String AUTHOR = "author";//作者
    public static final String CONTENT = "content";//文字内容
    public static final String IMAGE_PATH = "image_path";//图片路径


    public MyNoteDBHelper(Context context) {
        //context name cursorFactory version
        super(context, "writableDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TIME + " TEXT NOT NULL," +
                TITLE + " TEXT NOT NULL,"+
                AUTHOR+ " TEXT NOT NULL ," +
                CONTENT + " TEXT NOT NULL," +
                IMAGE_PATH + " TEXT" +
                ")";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
