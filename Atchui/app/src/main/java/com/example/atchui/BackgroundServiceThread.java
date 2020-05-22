package com.example.atchui;


import android.os.Handler;

public class BackgroundServiceThread extends Thread {
    //서버와 연결될 것이므로 thread로 구현
    Handler handler;
    boolean isRun = true;

    //생성자
    public BackgroundServiceThread(Handler handler){
        this.handler = handler;
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
            handler.sendEmptyMessage(0); //thread에 있는 핸들러에게 메시지 보냄
            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch(Exception e){ }

        }
    }
}
