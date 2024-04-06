package com.example.khabrii;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        final String channelId = "foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId,channelId, NotificationManager.IMPORTANCE_LOW);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this,channelId)
                .setContentText("Foreground service is running")
                .setContentTitle("This is title");

        startForeground(1001,notification.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        stopSelf();
        return super.stopService(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
