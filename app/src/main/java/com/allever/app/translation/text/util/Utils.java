//package com.allever.app.translation.assistant.util;
//
//import android.annotation.TargetApi;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//
//import com.allever.lib.common.app.App;
//
//import static android.content.Context.NOTIFICATION_SERVICE;
//
//class Utils {
//    @TargetApi(Build.VERSION_CODES.O)
//    private void createNotificationChannel(String channelId, String channelName, int importance) {
//        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//        NotificationManager notificationManager = (NotificationManager) App.context.getSystemService(
//                NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(channel);
//    }
//
//    protected void onCreate() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId = "translation";
//            String channelName = "翻译";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            createNotificationChannel(channelId, channelName, importance);
//        }
//    }
//
//    private void function() {
//        Notification notification = new NotificationCompat.Builder(this, "chat")
//
//    }
//
//}
