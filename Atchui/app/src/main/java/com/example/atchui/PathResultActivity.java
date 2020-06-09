package com.example.atchui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atchui.network.DataManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.HEAD;

public class PathResultActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "PathResultActivity";

    TextView user_datetime;
    TextView cnf_location;
    TextView cnf_datetime;
    TextView info_infectcase;
    TextView info_cnfdate;
    TextView info_province;
    TextView info_isofacility;
    TextView info_cnfNum;

    /*서버 데이터 저장할 변수 선언*/
    String cnf_id; //확진자id
    double cnf_latitude;    //확진자위도
    double cnf_longitude;   //확진자경도
    double user_latitude;   //사용자위도
    double user_longitude;  //사용자경도

    //확진자정보
    String location_name;   //방문장소명
    int labelColor;         //라벨컬러
    String province;        //확진자 거주지
    String isofacility;     //확진자 격리시설
    String infectcase;      //확진자 감염경로
    String cnfDate;         //확진 날짜
    String cnf_time;        //확진자 방문시간

    //사용자정보
    String user_time;       //사용자 방문시간

    //Noti정보
    int lstIndex;              //서버 list 내 인덱스
    String anal_time;              //분석시간
    // pre-circle
    private Circle preCircle = null;


    static double current_lat;
    static double current_long;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_result);

        user_datetime = (TextView)findViewById(R.id.userDatetime);
        cnf_location = (TextView)findViewById(R.id.cnfLocation);
        cnf_datetime = (TextView)findViewById(R.id.cnfDatetime);
        info_infectcase = (TextView)findViewById(R.id.info_infectCase);
        info_cnfdate = (TextView)findViewById(R.id.info_cnfdate);
        info_province = (TextView)findViewById(R.id.info_province);
        info_isofacility = (TextView)findViewById(R.id.info_isoFacility);
        info_cnfNum = (TextView)findViewById(R.id.info_cnfNum);

        /*선택된 알림 정보 가져오기*/
        Intent intent = getIntent();

        lstIndex = intent.getExtras().getInt("lstIndex");
        getSelectedNotiData(lstIndex);  //선택된 Noti의 데이터 가져오기

        /*google map*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_pathResult);
                mapFragment.getMapAsync(PathResultActivity.this);

    }

    private void getSelectedNotiData(int i){
        /*서버의 데이터 가져오기*/
        //위치
        cnf_latitude = DataManager.getInstance().lstAnal.get(i).m_cnfLatitude;    //확진자위도
        cnf_longitude = DataManager.getInstance().lstAnal.get(i).m_cnfLongitude;   //확진자경도
        user_latitude = DataManager.getInstance().lstAnal.get(i).m_userLatitude;   //사용자위도
        user_longitude = DataManager.getInstance().lstAnal.get(i).m_userLongitude;  //사용자경도

        //확진자정보
        cnf_id = DataManager.getInstance().lstAnal.get(i).m_cnfID; //확진자id
        location_name = DataManager.getInstance().lstAnal.get(i).m_locationName;   //방문장소명
        labelColor =  DataManager.getInstance().lstAnal.get(i).m_color;    //라벨컬러
        province = DataManager.getInstance().lstAnal.get(i).m_cnfProvince;  //확진자거주지
        isofacility = DataManager.getInstance().lstAnal.get(i).m_cnfIsoFacility;  //확진자 격리시설
        infectcase = DataManager.getInstance().lstAnal.get(i).m_cnfInfectCase;  //확진자 감염 경로
        cnfDate = DataManager.getInstance().lstAnal.get(i).m_cnfDate;  //확진 날짜
        cnf_time = DataManager.getInstance().lstAnal.get(i).m_cnfVisitTime;  //확진자 방문시간

        //사용자정보
        user_time = DataManager.getInstance().lstAnal.get(i).m_userVisitTime;        //사용자방문시간
        //Noti정보
        anal_time = DataManager.getInstance().lstAnal.get(i).m_analTime;   //분석시간

        String user_timeStr = String.format(getResources().getString(R.string.noti_time)
                ,user_time.substring(0,10),user_time.substring(11,19));

        String cnf_timeStr = String.format(getResources().getString(R.string.noti_time)
                ,cnf_time.substring(0,10),cnf_time.substring(11,19));

        String cnfDateStr = cnfDate.substring(0,10);

        /*텍스트뷰에 집어넣기*/
        user_datetime.setText(user_timeStr);

        cnf_location.setText(location_name);
        cnf_datetime.setText(cnf_timeStr);

        info_cnfNum.setText(String.format(getResources().getString(R.string.result_cnfid),cnf_id));
        info_infectcase.setText(String.format(getResources().getString(R.string.result_infectcase),infectcase));
        info_cnfdate.setText(String.format(getResources().getString(R.string.result_cnfDate),cnfDateStr));
        info_province.setText(String.format(getResources().getString(R.string.result_province),province));
        info_isofacility.setText(String.format(getResources().getString(R.string.result_iso),isofacility));

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(this,"map is Ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady : map is ready");

        mMap = googleMap;

        MarkerOptions cnf_marker = new MarkerOptions();
//        cnf_marker.position(new LatLng(cnf_latitude,cnf_longitude))
//                .title(anal_time);

        String server_ID = cnf_id;
        String server_LocationName = user_time;
        String S1 = "날짜: "  + server_LocationName.substring(0,10) + "    " + "장소: "+location_name;

        if(labelColor==0){
            cnf_marker.position(new LatLng(cnf_latitude,cnf_longitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red));
            mMap.addMarker(cnf_marker);
        }
        else if(labelColor==1){
            cnf_marker.position(new LatLng(cnf_latitude, cnf_longitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow));
            mMap.addMarker(cnf_marker);
        }
        else{
            cnf_marker.position(new LatLng(cnf_latitude,cnf_longitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
            mMap.addMarker(cnf_marker);
        }


        MarkerOptions user_marker = new MarkerOptions();
        user_marker.position(new LatLng(user_latitude, user_longitude))
                .title(location_name)
                .snippet(S1)
                .icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        LatLng center = new LatLng(user_latitude, user_longitude);
        int m_radius = DataManager.getInstance().Option.m_iRadius;
        Log.d("tag","m_radius : " + m_radius);
        preCircle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(m_radius)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5)
        );
        mMap.addMarker(user_marker);

        moveCamera(new LatLng(user_latitude,user_longitude),15f);
    }
    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

}
