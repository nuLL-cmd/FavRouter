package com.example.favrouter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseConnect extends SQLiteOpenHelper {
    public static String tb_name = "tb_locationas";
    private static String db_name = "db_locations";
    private static int db_version = 1;

    public DatabaseConnect(@Nullable Context context) {
        super(context, tb_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + tb_name + "(id_location integer not null primary key autoincrement,\n" +
                "location varchar not null,\n" +
                "latitude double not null,\n" +
                "longitude double not null)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
