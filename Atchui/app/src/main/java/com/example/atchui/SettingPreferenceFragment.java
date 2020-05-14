package com.example.atchui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
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
            //내장 DB가 변했는데, 변한 내용의 key에 대한 value가 true, false일 경우
            if(key.equals("alert")){
                //사용함
                if(prefs.getBoolean("alert", false)){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), getString(R.string.alert_message_y), Toast.LENGTH_SHORT).show();
                        //TODO: Alert on일 때 동작
                    }
                }
                //사용안함
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.alert_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: Alert off일 때 동작
                    }
                }
            }

            if(key.equals("sound")){
                //사용함
                if(prefs.getBoolean("sound", false)){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), getString(R.string.sound_message_y), Toast.LENGTH_SHORT).show();
                        //TODO: Alert on일 때 동작
                    }
                }
                //사용안함
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.sound_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: Alert off일 때 동작
                    }
                }
            }


            if(key.equals("vibration")){
                //사용함
                if(prefs.getBoolean("vibration", false)){
                    if(getActivity() != null){
                        Toast.makeText(getActivity(), getString(R.string.vibration_message_y), Toast.LENGTH_SHORT).show();
                        //TODO: Alert on일 때 동작
                    }
                }
                //사용안함
                else{
                    if(getActivity() != null){
                        Toast.makeText(getActivity(),getString(R.string.vibration_message_n), Toast.LENGTH_SHORT).show();
                        //TODO: Alert off일 때 동작
                    }
                }
            }
        }
    };
}
