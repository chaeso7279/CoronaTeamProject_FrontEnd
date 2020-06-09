package com.example.atchui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atchui.network.DataManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.HEAD;

public class PathResultActivity extends FragmentActivity implements OnMapReadyCallback {

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

    //사용자정보
    String user_time;       //TODO: 받아오기

    //Noti정보
    int lstIndex;              //서버 list 내 인덱스
    String anal_time;              //분석시간

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
        //        mapFragment.getMapAsync(PathResultActivity.this);

    }

    private void getSelectedNotiData(int i){
        /*서버의 데이터 가져오기*/
        cnf_id = DataManager.getInstance().lstAnal.get(i).m_cnfID; //확진자id
        Log.d("확진자ID",""+cnf_id);
        cnf_latitude = DataManager.getInstance().lstAnal.get(i).m_cnfLatitude;    //확진자위도
        cnf_longitude = DataManager.getInstance().lstAnal.get(i).m_cnfLongitude;   //확진자경도
        user_latitude = DataManager.getInstance().lstAnal.get(i).m_userLatitude;   //사용자위도
        user_longitude = DataManager.getInstance().lstAnal.get(i).m_userLongitude;  //사용자경도
        //확진자정보
        location_name = DataManager.getInstance().lstAnal.get(i).m_locationName;   //방문장소명
        labelColor =  DataManager.getInstance().lstAnal.get(i).m_color;    //라벨컬러
        //사용자정보
        user_time = DataManager.getInstance().lstAnal.get(i).m_analTime;           //TODO: table에 column 추가 후 제대로 받아오기(현재는 임시)
        //Noti정보
        anal_time = DataManager.getInstance().lstAnal.get(i).m_analTime;   //분석시간

        String user_timeStr = String.format(getResources().getString(R.string.noti_time),user_time.substring(0,10),user_time.substring(11,19));
        Log.d("사용자 시간", user_timeStr);  //TODO: 맞는지 확인


        /*텍스트뷰에 집어넣기*/
        user_datetime.setText(user_timeStr);  //TODO: 유저 방문시간 추가된거 받아오기 - fragment도 바꿔야됨
        cnf_location.setText(location_name);
//        cnf_datetime.setText();   //TODO: 확진자방문장소 시간 .... db 추가....
//        info_infectcase.setText();
//        info_cnfdate.setText();
//        info_province.setText();
//        info_isofacility.setText();   //TODO: db 추가...
        info_cnfNum.setText(String.format(getResources().getString(R.string.path_cnfid),cnf_id));
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(this,"map is Ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady : map is ready");

        mMap = googleMap;

        MarkerOptions user_marker = new MarkerOptions();
        user_marker.position(new LatLng(user_latitude,user_longitude))
                .title(anal_time);

        String server_ID = cnf_id;
        String server_LocationName = user_time;
        String S1 = "날짜: "  + server_LocationName.substring(0,10) + "    " + "장소: "+location_name;

        if(labelColor==0){
            user_marker.position(new LatLng(user_latitude,user_longitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red));
            mMap.addMarker(user_marker);
        }
        else if(labelColor==0){
            user_marker.position(new LatLng(user_latitude,user_longitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow));
            mMap.addMarker(user_marker);
        }
        else{
            user_marker.position(new LatLng(user_latitude,user_longitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
            mMap.addMarker(user_marker);
        }


        MarkerOptions cnf_marker = new MarkerOptions();

        cnf_marker.position(new LatLng(cnf_latitude,cnf_longitude))
                .title(location_name);
        mMap.addMarker(cnf_marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(user_latitude,user_longitude)));

        cnf_marker.position(new LatLng(cnf_latitude, cnf_longitude))
                .title(location_name)
                .snippet(S1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue));

        mMap.addMarker(cnf_marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(user_latitude, user_longitude)));
    }
}
