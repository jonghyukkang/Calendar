package com.example.kangjonghyuk.calendar_0603;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kangjonghyuk on 2016. 6. 7..
 */
public class myDB extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "memo.db";
    static final int DATABASE_VERSION = 1;

    public myDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table memo("
                        + "date integer primary key,"
                        + "story char(500)"
                        + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS memo";
        db.execSQL(sql);
        onCreate(db);
    }
}
