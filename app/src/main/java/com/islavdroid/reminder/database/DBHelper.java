package com.islavdroid.reminder.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.islavdroid.reminder.model.ModelTask;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "reminder_database";
    public static final String TABLE ="tasks_table";

    public static final String TITLE_COLUMN ="title";
    public static final String DATE_COLUMN ="date";
    public static final String PRIORITY_COLUMN ="priority";
    public static final String STATUS_COLUMN ="status";
    //БУДЕТ СОЗДАВАТЬСЯ ПРИ СОЗДАНИИ ЦЕЛИ И БЕРЕТ ЗА ОСНОВУ ТЕКУЩУЮ ДАТУ
    public static final String TIME_STAMP_COLUMN ="time_stamp";
    public static final String SELECTION_TIME_STAMP = TIME_STAMP_COLUMN +" = ?";


    public static final String TABLE_CREATE_SCRIPT ="CREATE TABLE "+TABLE + " ("+ BaseColumns._ID+
            " INTEGER PRIMARY KEY AUTOINCREMENT, "+ TITLE_COLUMN+ " TEXT NOT NULL, "+DATE_COLUMN+" LONG, "+
            PRIORITY_COLUMN+" INTEGER, "+ STATUS_COLUMN+" INTEGER, "+TIME_STAMP_COLUMN+" LONG);";
public static final String SELECTION_STATUS = DBHelper.STATUS_COLUMN+ " = ?";
    private DBQueryManager dbQueryManager;
    private DBUpdateManager updateManager;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbQueryManager =new DBQueryManager(getReadableDatabase());
        updateManager =new DBUpdateManager(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       //создаём таблицу
        db.execSQL(TABLE_CREATE_SCRIPT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     //удаляем таблицу
        db.execSQL("DROP TABLE "+TABLE);
        //пересоздаём
        onCreate(db);
    }

    public void saveTask(ModelTask task){
        ContentValues newValues = new ContentValues();
        newValues.put(TITLE_COLUMN,task.getTitle());
        newValues.put(DATE_COLUMN,task.getDate());
        newValues.put(STATUS_COLUMN,task.getStatus());
        newValues.put(PRIORITY_COLUMN,task.getPriority());
        newValues.put(TIME_STAMP_COLUMN,task.getTimeStamp());
        getWritableDatabase().insert(TABLE,null,newValues);




    }

    public DBQueryManager query(){
        return dbQueryManager;
    }
    public DBUpdateManager update(){
        return updateManager;
    }

    public void removeTask(long timeStamp){
        getWritableDatabase().delete(TABLE,SELECTION_TIME_STAMP,new String[]{Long.toString(timeStamp)});


    }
}
