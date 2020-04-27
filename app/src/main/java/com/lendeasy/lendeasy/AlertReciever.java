package com.lendeasy.lendeasy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReciever extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        setUpNotifications();
    }

    public void setUpNotifications() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        android.app.Notification notification = new NotificationCompat.Builder(context, com.lendeasy.lendeasy.Notification.CHANNEL_ID)
                .setContentTitle("Lend Easy")
                .setContentText("Item due")
                .setSmallIcon(R.drawable.lend)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();
        notificationManagerCompat.notify(1, notification);
    }
}
