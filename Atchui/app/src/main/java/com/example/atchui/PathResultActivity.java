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

public class PathResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "PathResultActivity";

    TextView user_location;
    TextView user_datetime;
    TextView cnf_location;
    TextView cnf_datetime;
    TextView info_infectcase;
    TextView info_cnfdate;
    TextView info_province;
    TextView info_isofacility;


    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_result);

        user_location = (TextView)findViewById(R.id.userLocation);
        user_datetime = (TextView)findViewById(R.id.userDatetime);
        cnf_location = (TextView)findViewById(R.id.cnfLocation);
        cnf_datetime = (TextView)findViewById(R.id.cnfDatetime);
        info_infectcase = (TextView)findViewById(R.id.info_infectCase);
        info_cnfdate = (TextView)findViewById(R.id.info_cnfdate);
        info_province = (TextView)findViewById(R.id.info_province);
        info_isofacility = (TextView)findViewById(R.id.info_isoFacility);

        Intent intent = getIntent();
        int lstIndex = intent.getExtras().getInt("lstIndex");

        getSelectedNotiData(lstIndex);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_pathResult);

//        mapFragment.getMapAsync(PathResultActivity.this);

    }

    private void getSelectedNotiData(int index){
        cnf_location.setText(
                DataManager.getInstance().lstAnal.get(index).m_locationName
        );

        i= index;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(this,"map is Ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady : map is ready");

        mMap = googleMap;



        String server_ID = String.valueOf(DataManager.getInstance().lstAnal.get(i).m_cnfRouteID);
        String server_LocationName = DataManager.getInstance().lstAnal.get(i).m_userVisitTime;
        String S1 = "날짜: "  + server_LocationName.substring(0,10) + "    " + "장소: "+DataManager.getInstance().lstAnal.get(i).m_locationName;


        if(DataManager.getInstance().lstAnal.get(i).m_color==0){
            MarkerOptions user_marker = new MarkerOptions();
            user_marker.position(new LatLng(DataManager.getInstance().lstAnal.get(i).m_userLatitude
                    ,DataManager.getInstance().lstAnal.get(i).m_userLongitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red));
            mMap.addMarker(user_marker);
        }
        else if(DataManager.getInstance().lstAnal.get(i).m_color==0){
            MarkerOptions user_marker = new MarkerOptions();
            user_marker.position(new LatLng(DataManager.getInstance().lstAnal.get(i).m_userLatitude
                    ,DataManager.getInstance().lstAnal.get(i).m_userLongitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow));
            mMap.addMarker(user_marker);
        }
        else{
            MarkerOptions user_marker = new MarkerOptions();
            user_marker.position(new LatLng(DataManager.getInstance().lstAnal.get(i).m_userLatitude
                    ,DataManager.getInstance().lstAnal.get(i).m_userLongitude))
                    .title(server_ID + "번째 확진자")
                    .snippet(S1)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
            mMap.addMarker(user_marker);
        }


        MarkerOptions cnf_marker = new MarkerOptions();
        cnf_marker.position(new LatLng(DataManager.getInstance().lstAnal.get(i).m_cnfLatitude
                , DataManager.getInstance().lstAnal.get(i).m_cnfLongitude))
                .title(DataManager.getInstance().lstAnal.get(i).m_locationName)
                .snippet(S1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue));

        mMap.addMarker(cnf_marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(DataManager.getInstance().lstAnal.get(i).m_userLatitude
                , DataManager.getInstance().lstAnal.get(i).m_userLongitude)));
    }
}
