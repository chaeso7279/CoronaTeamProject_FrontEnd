package com.example.atchui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.SettingData;
import com.example.atchui.network.DataEventListener;
import com.example.atchui.network.RetrofitClient;
import com.example.atchui.network.DataManager;
import com.example.atchui.network.ServiceAPI;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static java.sql.DriverManager.println;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private ClusterManager<MyItem> mClusterManager;

    private MyItem clickedClusterItem;
    LatLng mposition;
    static double current_lat;
    static double current_long;


    //widgets
    private EditText mSearchText;
    private EditText tSearchText;
    private ImageView mGps;
    String value ;
    //vars
    private Boolean mLocationPermissionsGranted = false;

    // pre-circle
    private Circle preCircle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DataManager.getInstance().GetAnalysisList();

//        double lat = DataManager.getInstance().lstPatientRoute.get(0).m_latitude;
//        Log.e(TAG, lat + "");

        getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        MapFragment mapFragment1 = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

        mSearchText = (EditText) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mapFragment.getMapAsync(this);


        //////////////////////////////////////
        /*button 누를 시 Activity 이동*/
        ImageButton btn_notification = (ImageButton)findViewById(R.id.btn_notification_list);
        ImageButton btn_setting = (ImageButton)findViewById(R.id.btn_setting);
        ImageButton btn_help = (ImageButton)findViewById(R.id.btn_help);

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.atchui.NotificationListActivity.class);
                startActivity(intent);
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.atchui.SettingActivity.class);
                startActivity(intent);
            }
        });

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                //startActivity(intent);

                //DataManager.getInstance().GetAnalysisList();
                // 임시
                int size = DataManager.getInstance().lstAnal.size();
                Log.e(TAG,"anal Lst Size: " + size);
            }
        });


        //////////////////////////////////////
        /*BackgroundService*/
        //서비스 시작
        Toast.makeText(getApplicationContext(), "Service 시작", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {
                    //searching을 실행한다
                    geoLocate();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });
//        hideSoftKeyboard();
    }


    private void geoLocate() {
        String serchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(serchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate : IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
//          Toast.makeText(this,address.toString(),Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        current_lat = location.getLatitude();
        current_long = location.getLongitude();
//        Toast.makeText(this, current_lat + ", " + current_long, Toast.LENGTH_SHORT).show();
        LatLng center = new LatLng(current_lat, current_long);
        if (preCircle != null) {
            preCircle.remove();
        }

        int m_radius = DataManager.getInstance().Option.m_iRadius;
        Log.d("tag","m_radius : " + m_radius);
        preCircle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(m_radius)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5)
        );
//        Log.d(TAG ,"m_iPeriod" + DataManager.getInstance().Option.m_iPeriod);

        tSearchText = (EditText) findViewById(R.id.input_search2);
        value = tSearchText.getText().toString();

        if(value.length()>=9) {
            tSearchText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if ((keyEvent.getAction()) == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        T_addItems(value);

                    }
                    return false;
                }
            });
        }
//        if(value.length()>=9) {
//            T_addItems(value);
//        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(this, "map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady : map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            //자신의 위치 나타내기
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(this);
            //자기 위치 찾는거 지운다
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
        //클러스터
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

    //        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
//        mMap.setOnInfoWindowClickListener(mClusterManager); //added

        mClusterManager.setRenderer(new MyClusterRenderer(this, mMap, mClusterManager));
        addItems();
    }


    private void T_addItems(String value){
        mClusterManager.clearItems();
        int size = DataManager.getInstance().lstPatientRoute.size();
        Log.d("tag", "value: " + value);
        // Add ten cluster items in close proximity, for purposes of this example.

        final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());


        //  서버 정보 추가
        double server_lat;
        double server_long;
        String server_ID;
        String server_LocationName;
        String S1;
        int Server_color;

        /////////////////

            //시간별 확진자 색깔 따로 찍기
        for (int i = 0; i < size; i++) {
            String visitDatetime = DataManager.getInstance().lstPatientRoute.get(i).m_visitDatetime;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date mDate = null;
            try {
                mDate = format.parse(visitDatetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //입력 시간 가져오기
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date s_Date = null;
            try {
                s_Date = mSimpleDateFormat.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar server_cal = Calendar.getInstance();
            server_cal.setTime(mDate);
            Log.d("tag", "mDate: " + mDate);
            Calendar s_cal = Calendar.getInstance();
            s_cal.setTime(s_Date);
            Log.d("tag", "s_Date: " + s_Date);


            long diff = s_cal.getTimeInMillis() - server_cal.getTimeInMillis();

            Log.d("tag", "diff: " + diff);
            if (diff > 0) {
                Bitmap icon = null;
                server_lat = DataManager.getInstance().lstPatientRoute.get(i).m_latitude;
                server_long = DataManager.getInstance().lstPatientRoute.get(i).m_longitude;
                server_ID = DataManager.getInstance().lstPatientRoute.get(i).m_cnfID + "번 확진자";
                server_LocationName = DataManager.getInstance().lstPatientRoute.get(i).m_visitDatetime;
                S1 = "날짜: " + server_LocationName.substring(0, 10) + "    " + "장소: " + DataManager.getInstance().lstPatientRoute.get(i).m_locationName;
                Server_color = DataManager.getInstance().lstPatientRoute.get(i).m_color;

                if(Server_color == 0) {
                    Drawable clusterIcon = getResources().getDrawable(R.drawable.color_red);
                    mClusterIconGenerator.setBackground(clusterIcon);
                    icon = mClusterIconGenerator.makeIcon();
                }
                else if(Server_color == 1){
                    Drawable clusterIcon = getResources().getDrawable(R.drawable.color_yellow);
                    mClusterIconGenerator.setBackground(clusterIcon);
                    icon = mClusterIconGenerator.makeIcon();
                }
                else {
                    Drawable clusterIcon = getResources().getDrawable(R.drawable.color_green);
                    mClusterIconGenerator.setBackground(clusterIcon);
                    icon = mClusterIconGenerator.makeIcon();
                }
                if(icon != null) {
                    MyItem offsetItem = new MyItem(server_lat, server_long, server_ID, S1, icon);
                    mClusterManager.addItem(offsetItem);
                }
            }
        }
    }

    // 확진자 추가 함수
    private void addItems() {
        mClusterManager.clearItems();
        Log.d(TAG ,"m_iPeriod" + DataManager.getInstance().Option.m_iPeriod);
        int size = DataManager.getInstance().lstPatientRoute.size();
        // Add ten cluster items in close proximity, for purposes of this example.

        final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());


        //  서버 정보 추가
        double server_lat;
        double server_long;
        String server_ID;
        String server_LocationName;
        String S1;
        int Server_color;
        for (int i = 0; i < size; i++) {
            Bitmap icon = null;
            server_lat = DataManager.getInstance().lstPatientRoute.get(i).m_latitude;
            server_long = DataManager.getInstance().lstPatientRoute.get(i).m_longitude;
            server_ID = DataManager.getInstance().lstPatientRoute.get(i).m_cnfID + "번 확진자";
            server_LocationName = DataManager.getInstance().lstPatientRoute.get(i).m_visitDatetime;
            S1 = "날짜: " + server_LocationName.substring(0, 10) + "    " + "장소: " + DataManager.getInstance().lstPatientRoute.get(i).m_locationName;
            Server_color = DataManager.getInstance().lstPatientRoute.get(i).m_color;

            if(Server_color == 0) {
                Drawable clusterIcon = getResources().getDrawable(R.drawable.color_red);
                mClusterIconGenerator.setBackground(clusterIcon);
                icon = mClusterIconGenerator.makeIcon();
            }
            else if(Server_color == 1){
                Drawable clusterIcon = getResources().getDrawable(R.drawable.color_yellow);
                mClusterIconGenerator.setBackground(clusterIcon);
                icon = mClusterIconGenerator.makeIcon();
            }
            else {
                Drawable clusterIcon = getResources().getDrawable(R.drawable.color_green);
                mClusterIconGenerator.setBackground(clusterIcon);
                icon = mClusterIconGenerator.makeIcon();
            }
            if(icon != null) {
                MyItem offsetItem = new MyItem(server_lat, server_long, server_ID, S1, icon);
                Log.d("tag", "server lat : " + server_lat);
                mClusterManager.addItem(offsetItem);
            }
        }
//        mClusterManager.clearItems();
//        if(DataManager.getInstance().Option.m_iPeriod < 200) {
//            for (int i = 0; i < size; i++) {
//                Bitmap icon = null;
//                server_lat = DataManager.getInstance().lstPatientRoute.get(i).m_latitude;
//                server_long = DataManager.getInstance().lstPatientRoute.get(i).m_longitude;
//                server_ID = DataManager.getInstance().lstPatientRoute.get(i).m_cnfID + "번 확진자";
//                server_LocationName = DataManager.getInstance().lstPatientRoute.get(i).m_visitDatetime;
//                S1 = "날짜: " + server_LocationName.substring(0, 10) + "    " + "장소: " + DataManager.getInstance().lstPatientRoute.get(i).m_locationName;
//                Server_color = DataManager.getInstance().lstPatientRoute.get(i).m_color;
//                if (Server_color == 0) {
//                    Drawable clusterIcon = getResources().getDrawable(R.drawable.red);
//                    mClusterIconGenerator.setBackground(clusterIcon);
//                    icon = mClusterIconGenerator.makeIcon();
//                }
//                if(icon != null) {
//                    MyItem offsetItem = new MyItem(server_lat, server_long, server_ID, S1, icon);
//
//                    Log.d("tag", "server lat : " + server_lat);
//                    mClusterManager.addItem(offsetItem);
//                }
//            }
//        }else if(DataManager.getInstance().Option.m_iPeriod >= 200 && DataManager.getInstance().Option.m_iPeriod < 700) {
//            for (int i = 0; i < size; i++) {
//                Bitmap icon = null;
//                server_lat = DataManager.getInstance().lstPatientRoute.get(i).m_latitude;
//                server_long = DataManager.getInstance().lstPatientRoute.get(i).m_longitude;
//                server_ID = DataManager.getInstance().lstPatientRoute.get(i).m_cnfID+ "번 확진자";
//                server_LocationName = DataManager.getInstance().lstPatientRoute.get(i).m_visitDatetime;
//                S1 = "날짜: " + server_LocationName.substring(0, 10) + "    " + "장소: " + DataManager.getInstance().lstPatientRoute.get(i).m_locationName;
//                Server_color = DataManager.getInstance().lstPatientRoute.get(i).m_color;
//                if (Server_color == 0) {
//                    Drawable clusterIcon = getResources().getDrawable(R.drawable.red);
//                    mClusterIconGenerator.setBackground(clusterIcon);
//                    icon = mClusterIconGenerator.makeIcon();
//                } else if (Server_color == 1) {
//                    Drawable clusterIcon = getResources().getDrawable(R.drawable.yellow);
//                    mClusterIconGenerator.setBackground(clusterIcon);
//                    icon = mClusterIconGenerator.makeIcon();
//                }
//                if(icon != null) {
//                    MyItem offsetItem = new MyItem(server_lat, server_long, server_ID, S1, icon);
//
//                    Log.d("tag", "server lat : " + server_lat);
//                    mClusterManager.addItem(offsetItem);
//                }
//            }
//        }else{
//            for (int i = 0; i < size; i++) {
//                Bitmap icon = null;
//                server_lat = DataManager.getInstance().lstPatientRoute.get(i).m_latitude;
//                server_long = DataManager.getInstance().lstPatientRoute.get(i).m_longitude;
//                server_ID = DataManager.getInstance().lstPatientRoute.get(i).m_cnfID + "번 확진자";
//                server_LocationName = DataManager.getInstance().lstPatientRoute.get(i).m_visitDatetime;
//                S1 = "날짜: " + server_LocationName.substring(0, 10) + "    " + "장소: " + DataManager.getInstance().lstPatientRoute.get(i).m_locationName;
//                Server_color = DataManager.getInstance().lstPatientRoute.get(i).m_color;
//                if (Server_color == 0) {
//                    Drawable clusterIcon = getResources().getDrawable(R.drawable.red);
//                    mClusterIconGenerator.setBackground(clusterIcon);
//                    icon = mClusterIconGenerator.makeIcon();
//                } else if (Server_color == 1) {
//                    Drawable clusterIcon = getResources().getDrawable(R.drawable.yellow);
//                    mClusterIconGenerator.setBackground(clusterIcon);
//                    icon = mClusterIconGenerator.makeIcon();
//                } else {
//                    Drawable clusterIcon = getResources().getDrawable(R.drawable.green);
//                    mClusterIconGenerator.setBackground(clusterIcon);
//                    icon = mClusterIconGenerator.makeIcon();
//                }
//                if(icon != null) {
//                    MyItem offsetItem = new MyItem(server_lat, server_long, server_ID, S1, icon);
//                    Log.d("tag", "server lat : " + server_lat);
//                    mClusterManager.addItem(offsetItem);
//                }
//            }
//        }

    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            //색깔
//            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(item.getColor_code());
//            markerOptions.icon(markerDescriptor);


//            final Drawable clusterIcon = getResources().getDrawable(R.drawable.blue);
//            mClusterIconGenerator.setBackground(clusterIcon);
//            Bitmap icon = mClusterIconGenerator.makeIcon();

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.getIcon_code()));
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }

//        @Override
//        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions){
//
//            final Drawable clusterIcon = getResources().getDrawable(R.drawable.ic_lens_black_24dp);
//            clusterIcon.setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
//
//            mClusterIconGenerator.setBackground(clusterIcon);
//
//            //modify padding for one or two digit numbers
//            if (cluster.getSize() < 10) {
//                mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
//            }
//            else {
//                mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
//            }
//
//            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//        }
    }


    //현위치 가져오기
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the current devices location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");
//                            LatLng center = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//                            if (preCircle != null) { preCircle.remove(); }
//                            preCircle = mMap.addCircle(new CircleOptions()
//                                        .center(center)
//                                        .radius(500)
//                                        .strokeColor(Color.RED)
//                                        .fillColor(Color.TRANSPARENT)
//                            );
//                            mMap.addPolyline(new PolylineOptions()
//                                    .clickable(true)
//                                    .add(
//                                            center
//                                    )
//                            );
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void initMap() {
        Log.d(TAG, "initMap:initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
    }

    //위치정보 동의
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");

                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onNewIntent(Intent intent) {
        println("onNewIntent 호출됨");

        //인텐트를 받은 경우만, 값을 Activity로 전달하도록 합니다.
        if(intent != null)
        {
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }

    //인텐트를 처리하는 메소드
    private void processIntent(Intent intent){
        String from = intent.getStringExtra("from");
        if(from == null){
            //from 값이 없는 경우, 값을 전달하지 않습니다. (푸쉬 노티 메시지가 아닌것을 판단하고 처리하지 않는 듯)
            Log.d(TAG, "보낸 곳이 없습니다.");
            return;
        }
        //메시지를 받은 경우 처리를 합니다.
        Log.d(TAG, "여기서 메시지 응답 처리를 하면 됩니다.");
   }
}
