package com.example.atchui.network;

import android.util.Log;
import android.widget.Toast;

import com.example.atchui.SettingActivity;
import com.example.atchui.database.AnalData;
import com.example.atchui.database.AnalResponse;
import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.PatientRouteResponse;
import com.example.atchui.database.SettingData;
import com.example.atchui.database.SettingResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManager {
    public static final int VAL_RADIUS = 0;
    public static final int VAL_PERIOD = 1;

    public ServiceAPI service;
    public PatientRouteResponse patientRouteResponse;

    public ArrayList<AnalData> lstAnal;
    public ArrayList<PatientRouteData> lstPatientRoute;

    public String user_id = "";
    private boolean bInit = false;

   public SettingData Option;

    // 이벤트 리스너
    private DataEventListener eventListener;

    public void Initialize(ServiceAPI service) {
        this.service = service;
        bInit = true;

        patientRouteResponse = new PatientRouteResponse();

        lstAnal = new ArrayList<AnalData>();
        lstPatientRoute = new ArrayList<PatientRouteData>();

    }

    // 리스너 이벤트 등록 함수
    public void SetOnReceivedEvent(DataEventListener listener){
        eventListener = listener;
    }

    public void SetUserID(String user_id) { this.user_id = user_id; }

    // 확진자 정보 관련
    public void GetPatientRoutes(){
        if(!bInit)
            return;

        PatientRouteData data = new PatientRouteData();
        service.cnfPatientRoute(data).enqueue(new Callback<PatientRouteResponse>() {
            @Override
            public void onResponse(Call<PatientRouteResponse> call, Response<PatientRouteResponse> response) {
                if(!lstPatientRoute.isEmpty())
                    lstPatientRoute.clear();

                try {
                    response.body().ConvertToData(lstPatientRoute);
                    Log.e("성공", "확진자 정보 가져오기");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PatientRouteResponse> call, Throwable t) {
                Log.e("실패", "확진자 정보 가져오기");
            }
        });
    }

    // 설정 관련
    public void SendUserOption(String user_id, int radius, int period) {
        if(!bInit)
            return;

        SettingData data = new SettingData(user_id, radius, period);
        service.userOption(data).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                SettingResponse result = response.body();

                Log.e("성공",result.getMessage());
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                Log.e("실패", t.getMessage());
            }
        });
    }

    public void GetUserOption(){
        /* 옵션 가져옴 */
        SettingData data = new SettingData();
        data.m_strUserID = user_id;
        service.GetUserOption(data).enqueue(new Callback<SettingData>() {
            @Override
            public void onResponse(Call<SettingData> call, Response<SettingData> response) {
                Log.e("성공", "옵션 가져오기");
                Option = new SettingData();
                Option = response.body();
            }

            @Override
            public void onFailure(Call<SettingData> call, Throwable t) {
                Log.e("실패", "옵션 가져오기");
            }
        });
    }

    public void UpdateUserOption(int VALUE_ID, int value) {
        if(!bInit)
            return;

        SettingData data = new SettingData();
        data.m_strUserID = user_id;

        switch (VALUE_ID){
            case VAL_RADIUS:
                data.m_iRadius = value;
                service.userOptionUpdateRad(data).enqueue(new Callback<SettingResponse>() {
                    @Override
                    public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                        SettingResponse result = response.body();
                        Log.e("Rad Update 성공",result.getMessage());
                    }

                    @Override
                    public void onFailure(Call<SettingResponse> call, Throwable t) {
                        Log.e("Rad Update 실패", t.getMessage());
                    }
                });
                break;
            case VAL_PERIOD:
                data.m_iPeriod = value;
                service.userOptionUpdatePeriod(data).enqueue(new Callback<SettingResponse>() {
                    @Override
                    public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                        SettingResponse result = response.body();
                        Log.e("Period Update 성공",result.getMessage());
                    }

                    @Override
                    public void onFailure(Call<SettingResponse> call, Throwable t) {
                        Log.e("Period Update 실패", t.getMessage());
                    }
                });
                break;
        }
    }

    // 분석결과(알림목록) 가져오기
    public void GetAnalysisList() {
        if(!bInit)
            return;

        AnalData data = new AnalData();
        service.GetAnalList(data).enqueue(new Callback<AnalResponse>() {
            @Override
            public void onResponse(Call<AnalResponse> call, Response<AnalResponse> response) {
                if(!lstAnal.isEmpty())
                    lstAnal.clear();

                Log.e("성공", response.body().strArray);
                try {
                    response.body().ConvertToData(lstAnal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AnalResponse> call, Throwable t) {
                Log.e("실패", "AnalList 가져오기");
            }
        });
    }

    // 분석결과 읽음 안읽음 업데이트
    public void UpdateAnalIsRead(int analID, boolean bIsRead) {
        if(!bInit)
            return;

        AnalData data = new AnalData();
        data.m_analID = analID;
        data.m_IsRead = bIsRead? 1 : 0;

        service.UpdateAnalIsRead(data).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                Log.e("성공", "Anal Update IsRead");
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                Log.e("실패", "Anal Update IsRead");
            }
        });
    }

    // 싱글톤
    private static DataManager instance = null;

    public static synchronized DataManager getInstance() {
        if(null == instance)
            instance = new DataManager();
        return instance;
    }
}
