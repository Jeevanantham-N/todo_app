package com.jeeva.todo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        id = Integer.parseInt(intent.getStringExtra("id"));
        PendingIntent contentIntent = PendingIntent.getActivity(context, id,
                new Intent(context, TodoActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setColor(Color.parseColor("#CC6540"))
                .setContentTitle("todo")
                .setContentText(intent.getStringExtra("note"))
                .setPriority(NotificationCompat.DEFAULT_SOUND)
                .setPriority(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.FLAG_SHOW_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(100,builder.build());
    }
}