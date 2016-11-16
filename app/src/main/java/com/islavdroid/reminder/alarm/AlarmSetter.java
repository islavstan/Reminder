package com.islavdroid.reminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.islavdroid.reminder.alarm.AlarmHelper;
import com.islavdroid.reminder.database.DBHelper;
import com.islavdroid.reminder.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by islav on 16.11.2016.
 */

//этот класс необходим для восстановления оповещений после перезагрузки устройства
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper =new DBHelper(context);

        AlarmHelper.getInstance().init(context);
        AlarmHelper alarmHelper =AlarmHelper.getInstance();

        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.query().getTasks(DBHelper.SELECTION_STATUS+ " OR "+DBHelper.SELECTION_STATUS,
                new String[]{Integer.toString(ModelTask.STATUS_CURRENT),Integer.toString(ModelTask.STATUS_OVERDUE)},
                DBHelper.DATE_COLUMN));

        for(ModelTask task:tasks){
            if(task.getDate()!=0){
                alarmHelper.setAlarm(task);
            }
        }
    }
}