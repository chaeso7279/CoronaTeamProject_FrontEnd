package com.example.atchui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.Date;

public class BackgroundService extends Service {

    public static final int SEND_PAST_INFO = 0;
    public static final int SEND_PRESENT_INFO = 1;

    private static final long SLEEP_PAST = 600000; // 10분
    private static final long SLEEP_PRESENT = 43200000; // 12시간

    NotificationManager notificationManager;

    BackGroundThread_Past thread_past;
    BackGroundThread_Present thread_present;

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    //최초 생성되었을 때 한 번 실행됩니다
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //백그라운드에서 실행되는 동작들이 들어가는 곳입니다.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Notifi_M = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        backgroundServiceHandler handler = new backgroundServiceHandler();

        thread_past = new BackGroundThread_Past(handler, SLEEP_PAST);
        thread_present = new BackGroundThread_Present(handler, SLEEP_PRESENT);

        thread_past.start();
        thread_present.start();

        return START_STICKY; //서비스가 런타임에 의해 종료되어도 항상 재시작, 재시작될 때 마다 onstartCommand가 실행됨( 이 때 전달되는 intent는 null)
    }

    //서비스가 종료될 때 실행되는 함수들이 들어갑니다.
    @Override
    public void onDestroy() {
        backgroundServiceHandler handler = new backgroundServiceHandler();

        thread_past = new BackGroundThread_Past(handler, SLEEP_PAST);
        thread_present = new BackGroundThread_Present(handler, SLEEP_PRESENT);

        thread_past.start();
        thread_present.start();
    }

    class backgroundServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {

            //TODO: GPS 서버 전송, 동선 분석 및 결과 전송
            switch (msg.what){
                case SEND_PAST_INFO:
                    break;
                case SEND_PRESENT_INFO:
                    break;
            }
//            notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
//
//            Intent intent = new Intent(BackgroundService.this, MainActivity.class);
//            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP ); //
//
//            PendingIntent pendingIntent;//notification을 상태알림창에 띄울 intent
//            pendingIntent = PendingIntent.getActivity(BackgroundService.this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
//                String channelId = "my_notification";
//                String channelName = "n_channel";
//                String channelDescription = "description";
//
//                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//                channel.setDescription(channelDescription);
//
//                assert notificationManager != null;
//                notificationManager.createNotificationChannel(channel);
//
////                //채널에 대한 각종 설정
////                channel.enableLights(true);
////                channel.setLightColor(Color.RED);
////                channel.enableVibration(true);
////                channel.setVibrationPattern(new long[]{100, 200, 300});
////                Notifi_M.createNotificationChannel(channel);
//            }
//
//            //channel이 등록된 builder(notification 내용 설정 위해 Builder 만들어줌)
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(BackgroundService.this,"my_notification")
//                    .setSmallIcon(R.drawable.btn_notification) //아이콘
//                    .setContentTitle("Title") //알림 제목
//                    .setContentText("ContentText") //알림 내용
//                    .setTicker("알림!!")  //상태바에 표시될 한 줄 출력
//                    .setSound(soundUri)//소리추가
//                    .setColor(Color.parseColor("#ffffff"))
//                    .setContentIntent(pendingIntent)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setOnlyAlertOnce(true) //알림 소리를 한번만 내도록
//                    .setAutoCancel(true) //확인하면 자동으로 알림이 제거 되도록
////                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                    .setChannelId("my_notification")
//                    ;
//
//
//            int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
//            notificationManager.notify(m, notificationBuilder.build());

            //토스트 띄우기
            //Toast.makeText(BackgroundService.this, "안뇽", Toast.LENGTH_SHORT).show();
        }
    };
}

class BackGroundThread_Past extends Thread{
    Handler handler;

    long SleepTime = 0;
    boolean isRun = true;

    //생성자
    public BackGroundThread_Past(Handler handler, long SleepTime){
        this.handler = handler;
        this.SleepTime = SleepTime;
    }

    //쓰레드 종료할 메소드
    public void stopForever(){
        synchronized (this){
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            handler.sendEmptyMessage(BackgroundService.SEND_PAST_INFO); //thread에 있는 핸들러에게 메시지 보냄
            try{
                Thread.sleep(SleepTime);
            }catch(Exception e){ }

        }
    }
}

class BackGroundThread_Present extends Thread{
    Handler handler;

    long SleepTime = 0;
    boolean isRun = true;

    //생성자
    public BackGroundThread_Present(Handler handler, long SleepTime){
        this.handler = handler;
        this.SleepTime = SleepTime;
    }

    //쓰레드 종료할 메소드
    public void stopForever(){
        synchronized (this){
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            handler.sendEmptyMessage(BackgroundService.SEND_PRESENT_INFO); //thread에 있는 핸들러에게 메시지 보냄
            try{
                Thread.sleep(SleepTime); //10초씩 쉰다.
            }catch(Exception e){ }

        }
    }
}