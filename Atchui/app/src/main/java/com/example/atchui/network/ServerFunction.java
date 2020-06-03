package com.example.atchui.network;

import android.util.Log;
import android.widget.Toast;

import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.PatientRouteResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerFunction {
    public ServiceAPI service;
    public PatientRouteResponse patientRouteResponse;

    private boolean bInit = false;

    public void Initialize(ServiceAPI service) { this.service = service;  bInit = true; }

    public void GetLatestPatientRouteData() {
        if(!bInit)
            return;

        PatientRouteData data = new PatientRouteData();
        service.cnfPatientRoute(data).enqueue(new Callback<PatientRouteResponse>() {
            @Override
            public void onResponse(Call<PatientRouteResponse> call, Response<PatientRouteResponse> response) {
                getInstance().patientRouteResponse = response.body();
                Log.e("GET RESOPONSE", "데이터 가져오기 성공");

               // Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<PatientRouteResponse> call, Throwable t) {
                Log.e("데이터 베이스 가져오기 실패", t.getMessage());
            }
        });
    }

    // 싱글톤
    private static ServerFunction instance = null;

    public static synchronized ServerFunction getInstance() {
        if(null == instance)
            instance = new ServerFunction();
        return instance;
    }
}
