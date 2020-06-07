package com.example.atchui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atchui.network.DataManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrentResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "CurrentResultActivity";

    TextView user_location;
    TextView user_datetime;
    TextView cnf_location;
    TextView cnf_datetime;
    TextView info_infectcase;
    TextView info_cnfdate;
    TextView info_province;
    TextView info_isofacility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_result);

        user_location = (TextView)findViewById(R.id.userLocation);
        user_datetime = (TextView)findViewById(R.id.userDatetime);
        cnf_location = (TextView)findViewById(R.id.cnfLocation);
        cnf_datetime = (TextView)findViewById(R.id.cnfDatetime);
        info_infectcase = (TextView)findViewById(R.id.info_infectCase);
        info_cnfdate = (TextView)findViewById(R.id.info_cnfdate);
        info_province = (TextView)findViewById(R.id.info_province);
        info_isofacility = (TextView)findViewById(R.id.info_isoFacility);

        /*선택된 알림 정보 가져오기*/
        Intent intent = getIntent();
        int lstIndex = intent.getExtras().getInt("lstIndex");

        getSelectedNotiData(lstIndex);

        /*google map*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_pathResult);
    //        mapFragment.getMapAsync(PathResultActivity.this);
    }

    private void getSelectedNotiData(int index){
       // DataManager.getInstance().lstAnal.get(index);
        cnf_location.setText(
                DataManager.getInstance().lstAnal.get(index).m_locationName
        );

    }

    //google map

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(this,"map is Ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady : map is ready");
        mMap = googleMap;
    }
}
