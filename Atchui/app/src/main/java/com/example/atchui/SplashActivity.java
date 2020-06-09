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
    private static final String SUHWA_ID = "f6BuMPhLS0e0x_7qo3xiSV:APA91bEHmNdudrlML8LI3RTC6CMMVA6HrYUh_kuY3fMb6gErnehDZ7eE5Prcbr4Sb-fP1DT0yl0WXr9GGF_T3E2r2GNbbsOfEMfXIc9Ofd4XnNX93sgndpttv0ZWYyZbC9u_pFN4EW89";
    private static final String SUJIN_ID = "f-kxekfORqiYp48UZC3T9-:APA91bEv4kDG5OeTlB67O44c4NEaLmF9ARE4i6Jmw7CB67A9MOc1Dj9Kh_B1SIwwDvrQd9ZRyzrkmbPLVRtaFET2rO1HN0uMcN-jmJdC-HINhSx3NazMHxhPDXSI6DQYFeJogP4F7xeR";
    private static final String TAEHUN_ID = "f1A-eWZPCs0:APA91bGLKPT2cqbH6rvO55skVLYhlXMUwYQoJOM8WwfzmJ_kQSOT9_Obb6WxoRJZ4sQAP7yvESkgocrkvqDbAGOjALAziEqjaffmP0eGb7vAvqAXUMRmoTQglzANGojdVL7LezDEc9yX";


    // Sever
    public ServiceAPI service;
    // Firebase Token
    public String m_DeviceID = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            LoadServerData();
            Thread.sleep(10000);
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
        //m_DeviceID = SUJIN_ID;

        //////////////////////
        // Server 연동
        service = RetrofitClient.getClient().create(ServiceAPI.class);
        DataManager.getInstance().Initialize(service);

        //고유 ID(FireBase토큰) DB 입력 및 서버에 userID 설정
//        DataManager.getInstance().SetUserID(m_DeviceID);
        DataManager.getInstance().SetUserID(m_DeviceID);
        SendDeviceIDToServer();

        // 설정 가져와달라고 서버에 요청
        DataManager.getInstance().GetUserOption();

        // 확진자 경로 가져오기
        DataManager.getInstance().GetPatientRoutes();

        // 과거 경로 기반 분석
        DataManager.getInstance().AnalPastRoute();

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