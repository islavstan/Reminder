package com.islavdroid.reminder.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.ModelTask;

public class DBQueryManager {
    private SQLiteDatabase sqLiteDatabase;

    DBQueryManager(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase=sqLiteDatabase;
    }

   /* columns – список полей, которые мы хотим получить
    selection – строка условия WHERE
    selectionArgs – массив аргументов для selection. В selection можно использовать знаки ?, которые будут заменены этими значениями.
    groupBy - группировка
    having – использование условий для агрегатных функций
    orderBy - сортировка*/
    public List<ModelTask>getTasks(String selectoin,String[] selectionArgs,String orderBy){
        List<ModelTask>tasks =new ArrayList<>();
        Cursor c = sqLiteDatabase.query(DBHelper.TABLE,null,selectoin,selectionArgs,
             null,null,orderBy );
        if(c.moveToFirst()){
            do{
             String title = c.getString(c.getColumnIndex(DBHelper.TITLE_COLUMN));
                long date =c.getLong(c.getColumnIndex(DBHelper.DATE_COLUMN));
                int priority = c.getInt(c.getColumnIndex(DBHelper.PRIORITY_COLUMN));
                int status =c.getInt(c.getColumnIndex(DBHelper.STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(DBHelper.TIME_STAMP_COLUMN));
                ModelTask modelTask = new ModelTask(title,date,priority,status,timeStamp);
                tasks.add(modelTask);
            }while(c.moveToNext());
        }
        c.close();
    return tasks;
    }
}
