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

public class SettingPreferenceFragment extends PreferenceFragment {
    private static final String TAG = "SettingPreference";
    SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
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
                        boolean now_value = true; //DB테스트를 위해 임시로 담아놓음
                    }
                }
                //비동의
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.alert_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: Alert off일 때 동작
                        boolean now_value = false; //DB테스트를 위해 임시로 담아놓음
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
                        boolean now_value = true;//DB테스트를 위해 임시로 담아놓음
                    }
                }
                //사용안함(진동)
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.sound_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: sound off일 때 동작
                        boolean now_value = false;//DB테스트를 위해 임시로 담아놓음
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

                }
            }

            //기간 설정
            if(key.equals("range")){
                if(getActivity() != null){
                    int now_value = sharedPreferences.getInt(key,0); //현재 값 받아옴
                    Resources res = getResources();
                    String new_summary = String.format(res.getString(R.string.period_summary),now_value); //format의 %d에 현재 값 넣기

                    Preference connectionPref = findPreference(key);
                    //summary에 사용자 설정 값 Set
                    connectionPref.setSummary(new_summary);

                }
            }

            //GPS 수집 동의
            if(key.equals("gps")){
                //동의
                if(prefs.getBoolean("vibration", false)){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), getString(R.string.gps_message_y), Toast.LENGTH_SHORT).show();
                        //TODO: gps on일 때 동작
                        boolean now_value = true;//DB테스트를 위해 임시로 담아놓음
                    }
                }
                //비동의
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.gps_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: gps off일 때 동작
                        boolean now_value = false; //DB테스트를 위해 임시로 담아놓음
                    }
                }
            }
        }
    };
}
