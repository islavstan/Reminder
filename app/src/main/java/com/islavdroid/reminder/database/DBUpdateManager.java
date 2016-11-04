package com.islavdroid.reminder.database;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.islavdroid.reminder.model.ModelTask;
//урок 6 19 минут
//отвечает за обновление данных в нашей базе
public class DBUpdateManager {

    SQLiteDatabase database;
    DBUpdateManager(SQLiteDatabase database){
        this.database=database;

    }

    //пишем несколько методов для обновления отдельных столбцов и всего таска в целом

    //=================обновление для отдельных столбцов===================
    public void title(long timeStamp,String title){
        update(DBHelper.TITLE_COLUMN,timeStamp,title);
    }

    public void date(long timeStamp,long date){
        update(DBHelper.DATE_COLUMN,timeStamp,date);
    }

    public void priority(long timeStamp,int priority){
        update(DBHelper.PRIORITY_COLUMN,timeStamp,priority);
    }

    public void status(long timeStamp,int status){
        update(DBHelper.STATUS_COLUMN,timeStamp,status);
    }
//=================обновление для отдельных столбцов===================


    public void task(ModelTask task){
        title(task.getTimeStamp(),task.getTitle());
        date(task.getTimeStamp(),task.getDate());
        priority(task.getTimeStamp(),task.getPriority());
        status(task.getTimeStamp(),task.getStatus());

    }



    private void update(String column,long key,String value){
        ContentValues cv = new ContentValues();
        cv.put(column,value);
        database.update(DBHelper.TABLE,cv,DBHelper.TIME_STAMP_COLUMN+" = "+key,null);



    }
    private void update(String column,long key,long value){
        ContentValues cv =new ContentValues();
        cv.put(column,value);
        database.update(DBHelper.TABLE,cv,DBHelper.TIME_STAMP_COLUMN+" = "+key,null);
    }

}
