package com.example.atchui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";
    public MyFirebaseMessagingService(){
    }

    //새로운 메시지를 받았을 때 호출되는 메소드
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //받은 메시지를 출력합니다.
        Log.e(TAG,"onMessagedReceived 호출됨"+ remoteMessage);

        //받은 데이터 중, 내용만 가지고 와 출력하는 메소드(파이어베이스 홈페이지에서 보내면 데이터는 값이 없을 수 있음)
        String from = remoteMessage.getFrom();
        Log.d(TAG,
                "title: " + remoteMessage.getNotification().getTitle()
                + ", body: " + remoteMessage.getNotification().getBody()
                + ",data: " + remoteMessage.getData()
        );
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