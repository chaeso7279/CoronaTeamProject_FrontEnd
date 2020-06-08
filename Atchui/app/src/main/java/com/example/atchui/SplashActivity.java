package com.example.atchui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.atchui.network.DataManager;
import com.example.atchui.network.RetrofitClient;
import com.example.atchui.network.ServiceAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";

    // Sever
    public ServiceAPI service;
    // Firebase Token
    public String m_DeviceID = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            LoadServerData();
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    LoadServerData();
                    finish();
                }
            }, 4000);
        }

    private void LoadServerData(){

        ///////////////////////////
        /*firebase 푸시알림*/
        //토큰이 등록되는 시점에 호출되는 메소드입니다.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>(){
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.d(TAG, "새토큰"+newToken);
                    }
                });

        //저장된 토큰을 가지고 오는 메소드
        String savedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "등록되어 있는 토큰ID:"+  savedToken);

        m_DeviceID = savedToken;

        //////////////////////
        // Server 연동
        service = RetrofitClient.getClient().create(ServiceAPI.class);
        DataManager.getInstance().Initialize(service);

        //고유 ID(FireBase토큰) DB 입력 및 서버에 userID 설정
//        DataManager.getInstance().SetUserID(m_DeviceID);
        DataManager.getInstance().SetUserID("cuTKxqcHm5s:APA91bHsAnLoevnS4j0dMeixleE5pg1nMAwRqDC_mm799NDKfm6uNqpShdJZoSP8glZKtkN0WQMK2MVg3Whi_ftDsX-RmkRhy5Ewoo_WPpGnw9XuHVcssX2VbLdsfdB18Fm_r4lo10I3");
        SendDeviceIDToServer();


        // 설정 가져와달라고 서버에 요청
        DataManager.getInstance().GetUserOption();

        // 확진자 경로 가져오기
        DataManager.getInstance().GetPatientRoutes();

        // Notification_list 관련 정보 가져오기
        DataManager.getInstance().GetAnalysisList();

        DataManager.getInstance().AnalPresentRoute(1);

        ////////////////////////////////////////
        Toast.makeText(SplashActivity.this, "서버 데이터 불러오기 완료", Toast.LENGTH_SHORT).show();
    }

    /* 서버 관련 함수 */
    /* 안드로이드 고유 아이디 (UUID) 초기화 및 사용자옵션 데이터베이스에서 가져옴 */
    private void SendDeviceIDToServer(){
        // DB 에 정보 저장, 이미 같은 ID 존재하면(최초 접속 아니면) 알아서 걸러질거라고 생각함
        DataManager.getInstance().SendUserOption(m_DeviceID, 100, 14);
    }
}