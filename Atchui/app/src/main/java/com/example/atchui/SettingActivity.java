package com.example.atchui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

// DB 테스트
import com.example.atchui.database.SettingData;
import com.example.atchui.database.SettingResponse;
import com.example.atchui.network.RetrofitClient;
import com.example.atchui.network.ServiceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    public ServiceAPI m_Service; // db 테스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // db 테스트를 위한 ServiceAPI 객체 생성
        m_Service = RetrofitClient.getClient().create(ServiceAPI.class);
    }

    public void sendToServer(SettingData data)
    {
       m_Service.userOption(data).enqueue(new Callback<SettingResponse>() {
           @Override
           public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response)
           {
                SettingResponse result = response.body();
                Toast.makeText(SettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if(result.getCode() == 200)
                    finish();
           }

           @Override
           public void onFailure(Call<SettingResponse> call, Throwable t)
           {
                Toast.makeText(SettingActivity.this, "옵션 데이터 베이스 넣기 실패", Toast.LENGTH_SHORT).show();
                Log.e("옵션 데이터 베이스 넣기 실패", t.getMessage());
           }
       });
    }
}
