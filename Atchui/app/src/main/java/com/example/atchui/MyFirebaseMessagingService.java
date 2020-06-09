package com.example.atchui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String id = "my_channel_02";
    CharSequence name = "fcm_nt";
    String description = "push";
    int importance = NotificationManager.IMPORTANCE_LOW;

    private static final String TAG = "FCM";
    public MyFirebaseMessagingService(){
    }

    //새로운 메시지를 받았을 때 호출되는 메소드
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("푸시","onMessageReceived");
        if (remoteMessage.getNotification() != null) { //포그라운드
            Log.d("푸시","포그라운드");
            showNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());

        }
        else if (remoteMessage.getData().size() > 0) { //백그라운드
            Log.d("푸시","백그라운드");
            showNotification(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"));

        }
//
//        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
//        Log.d("UTF-8","여기야");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(String messageTitle, String messageBody){
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);

        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = 2;

        String CHANNEL_ID = "my_channel_02";

        try{
            Notification notification = new Notification.Builder(MyFirebaseMessagingService.this)
                    .setContentTitle(URLDecoder.decode(messageTitle, "UTF-8"))
                    .setContentText(URLDecoder.decode(messageBody, "UTF-8"))
                    .setSmallIcon(R.drawable.ic_logo)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();


            mNotificationManager.notify(notifyID, notification);
        }
        catch(Exception e){
            e.printStackTrace();
        }
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        Log.d("UTF-8","들어옴");
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //알림 소리설정
//
//
//        try{
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.ic_logo)
//                    .setContentTitle(URLDecoder.decode(messageTitle, "UTF-8"))
//                    .setContentText(URLDecoder.decode(messageBody, "UTF-8"))
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//            Log.d("UTF-8","변환 성공");
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0, notificationBuilder.build());
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            Log.d("UTF-8","변환 실패");
//        }
    }
    //메시지에 사용될 토근을 새로 발급받았을 때, 호출되는 메소드(이 토큰은 각각의 기기를 식별)
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        //토큰 정보를 출력합니다.
        Log.e(TAG, "onNewToken 호출됨: " + token);
    }

    private void sendToActivity(Context context, String from, String title, String body, String contents){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from",from);
        intent.putExtra("title",title);
        intent.putExtra("body",body);
        intent.putExtra("contents",contents);

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        context.startActivity(intent);
    }
}