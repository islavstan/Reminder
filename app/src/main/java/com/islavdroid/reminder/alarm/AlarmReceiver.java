package com.islavdroid.reminder.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.islavdroid.reminder.MainActivity;
import com.islavdroid.reminder.MyApplication;
import com.islavdroid.reminder.R;

/**
 * Created by islav on 15.11.2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //заголовок задачи
        String title = intent.getStringExtra("title");
        //время задания
        long timeStamp = intent.getLongExtra("time_stamp",0);
        //цвет, который определяет приоритет задачи
        int color = intent.getIntExtra("color",0);
        //интент, который запускает активити при нажатии на нотификацию
        Intent resultIntent = new Intent(context,MainActivity.class);

if(MyApplication.isActivityVisible()){
    //если активити видимо, resultIntent получает текущее активити и не пересоздаёт его
    resultIntent =intent;
}
        //если приложение закрыто то AlarmReceiver стартует новое активити
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
  //позволяет запустить хранящийся внутри него intent от имени того приложения в котором этот PendingIntent создавался
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                (int) timeStamp,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //устанавливаем заголовок нотификейшена
        builder.setContentTitle("Reminder");
        // и текст
        builder.setContentText(title);
        //цвет нотификейшена
        builder.setColor(context.getResources().getColor(color));
        builder.setSmallIcon(R.drawable.ic_check_white_48px);
     //какие свойства будут унаследованы по умолчанию, ставим всё по дефолту
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);

        Notification notification =builder.build();
      //отменяем уведомление по нажатию на него
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)timeStamp,notification);


    }

}
