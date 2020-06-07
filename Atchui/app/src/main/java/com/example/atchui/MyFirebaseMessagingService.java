package com.example.atchui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";
    public MyFirebaseMessagingService(){
    }

    //새로운 메시지를 받았을 때 호출되는 메소드
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        Log.d("UTF-8","여기야");
    }
    private void showNotification(String title, String message){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Log.d("UTF-8","들어옴");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try{
            title =  URLDecoder.decode(title, "utf-8");
            message =  URLDecoder.decode(message, "utf-8");
            Log.d("UTF-8","변환 실패");
        }catch(Exception e){
            Log.d("UTF-8","변환 실패");
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
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