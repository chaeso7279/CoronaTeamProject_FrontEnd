package com.example.atchui;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.atchui.database.SettingData;

public class SettingPreferenceFragment extends PreferenceFragment {
    private static final String TAG = "SettingPreference";
    SharedPreferences prefs;

    private SettingData m_Data = null; // DB 테스트용

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        if(m_Data == null)
            m_Data = new SettingData();
    }

    //내장 DB가 변하는 것을 catch하는 리스너
    SharedPreferences.OnSharedPreferenceChangeListener prefListener  = new SharedPreferences.OnSharedPreferenceChangeListener(){
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //내장 DB가 변했을 때, 변한 내용의 key에 대한 각각의 value에 따른 조건문

            //푸시알림동의
            if(key.equals("alert")){
                //동의
                if(prefs.getBoolean("alert", false)){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), getString(R.string.alert_message_y), Toast.LENGTH_SHORT).show();
                        //TODO: Alert on일 때 동작
                        m_Data.m_bPushAgreement = true; //DB 테스트
                    }
                }
                //비동의
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.alert_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: Alert off일 때 동작
                        m_Data.m_bPushAgreement = false; //DB 테스트
                    }
                }
            }

            //푸시 알림 사운드 설정
            if(key.equals("sound")){
                //사용함(사운드)
                if(prefs.getBoolean("sound", false)){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), getString(R.string.sound_message_y), Toast.LENGTH_SHORT).show();
                        //TODO: sound on일 때 동작
                        m_Data.m_bOnSound = true;//DB 테스트
                    }
                }
                //사용안함(진동)
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.sound_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: sound off일 때 동작
                        m_Data.m_bOnSound = false;//DB 테스트
                    }
                }
            }

            //반경 설정
            if(key.equals("range")){
                if(getActivity() != null){
                    int now_value = sharedPreferences.getInt(key,0); //현재 값 받아옴
                    Resources res = getResources();
                    String new_summary = String.format(res.getString(R.string.range_summary),now_value); //format의 %d에 현재 값 넣기

                    Preference connectionPref = findPreference(key);
                    //summary에 사용자 설정 값 Set
                    connectionPref.setSummary(new_summary);

                    m_Data.m_iRadius = now_value;//DB 테스트
                }
            }

            //기간 설정
            if(key.equals("period")){
                if(getActivity() != null){
                    int now_value = sharedPreferences.getInt(key,0); //현재 값 받아옴
                    Resources res = getResources();
                    String new_summary = String.format(res.getString(R.string.period_summary),now_value); //format의 %d에 현재 값 넣기

                    Preference connectionPref = findPreference(key);
                    //summary에 사용자 설정 값 Set
                    connectionPref.setSummary(new_summary);

                    m_Data.m_iPeriod = now_value; // DB 테스트

                }
            }

            //GPS 수집 동의
            if(key.equals("gps")){
                //동의
                if(prefs.getBoolean("gps", false)){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), getString(R.string.gps_message_y), Toast.LENGTH_SHORT).show();
                        //TODO: gps on일 때 동작
                        m_Data.m_bGPSAgreement = true;//DB 테스트
                    }
                }
                //비동의
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.gps_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: gps off일 때 동작
                        m_Data.m_bGPSAgreement = false; //DB 테스트
                    }
                }
            }

           // if(getActivity() != null)
            //    ((SettingActivity)getActivity()).sendToServer(m_Data);
        }
    };
}
