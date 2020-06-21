package com.example.atchui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

// DB 테스트
import com.example.atchui.database.GeneralResponse;
import com.example.atchui.database.SettingData;
import com.example.atchui.network.RetrofitClient;
import com.example.atchui.network.ServiceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    public ServiceAPI service; // db 테스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // db 테스트를 위한 ServiceAPI 객체 생성
        service = RetrofitClient.getClient().create(ServiceAPI.class);
    }
}
