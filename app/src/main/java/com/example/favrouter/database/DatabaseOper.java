package com.example.favrouter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.favrouter.backendRecycler.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOper {
    private DatabaseConnect databaseConnect;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private ArrayList<Long> Aid;
    public DatabaseOper(Context context){
       databaseConnect = new DatabaseConnect(context);
    }

    public void createData(DataProvider dataProvider){
        sqLiteDatabase = databaseConnect.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("location",dataProvider.getLocation());
        contentValues.put("latitude",dataProvider.getLatitude());
        contentValues.put("longitude",dataProvider.getLongitude());

        sqLiteDatabase.insert(DatabaseConnect.tb_name,null,contentValues);

    }

    public List<DataProvider> readAllData(){
        List<DataProvider> dataProviderList = new ArrayList<>();
        sqLiteDatabase = databaseConnect.getReadableDatabase();
        Aid = new ArrayList<>();
        cursor = sqLiteDatabase.query(DatabaseConnect.tb_name,null,null,null,null,null,null,null);

        try {
            while (cursor.moveToNext()){
                Aid.add(cursor.getLong(0));
                dataProviderList.add(new DataProvider(cursor.getLong(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3)));
            }
            cursor.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return dataProviderList;

    }

    public ArrayList<Long> getAid(){
        return Aid;
    }

    public void updateData(DataProvider dataProvider){
        ContentValues values = new ContentValues();
        sqLiteDatabase = databaseConnect.getWritableDatabase();
        values.put("location",dataProvider.getLocation());
        values.put("latitude",dataProvider.getLatitude());
        values.put("longitude",dataProvider.getLongitude());


        sqLiteDatabase.update(DatabaseConnect.tb_name,values,"id_location = ?",new String[]{String.valueOf(dataProvider.getId())});
    }

    public void deleteData(long id){
        sqLiteDatabase = databaseConnect.getWritableDatabase();
        sqLiteDatabase.delete(DatabaseConnect.tb_name,"id_location = ?",new String[]{String.valueOf(id)});
    }
}
