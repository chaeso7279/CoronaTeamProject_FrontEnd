package com.example.atchui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.PatientRouteResponse;
import com.example.atchui.database.SettingResponse;
import com.example.atchui.network.RetrofitClient;
import com.example.atchui.network.ServerFunction;
import com.example.atchui.network.ServiceAPI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

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
    private ImageView mGps;

    //vars
    private Boolean mLocationPermissionsGranted = false;

    // Sever
    public ServiceAPI service;
    // Android UUID
    public String m_DeviceID = " ";

    // pre-circle
    private Circle preCircle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //firebase 푸시알림
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanced failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰" + token);

                        //Toast.makeText(MainActivity.this, "토큰:" + token, Toast.LENGTH_SHORT).show();
                    }
                });

        // Server 연동
        service = RetrofitClient.getClient().create(ServiceAPI.class);
        ServerFunction.getInstance().Initialize(service);

        // Android 고유 ID 가져오기
        try {
            InitDeviceUUID();
            ServerFunction.getInstance().SetUserID(m_DeviceID);
        } catch (IOException e) {
            Log.e("실패", "고유 ID 생성 실패");
        }

        MapFragment mapFragment1 = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

        mSearchText = (EditText) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mapFragment.getMapAsync(this);


        //////////////////////////////////////
        /*button 누를 시 Activity 이동*/

        Button btn_notification = (Button) findViewById(R.id.btn_notification_list);
        Button btn_setting = (Button) findViewById(R.id.btn_setting);
        Button btn_help = (Button) findViewById(R.id.btn_help);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                //startActivity(intent);

                // 경로 가져올 때 이 함수를 쓰시면 ServiceFunction 객체 내에 데이터가 저장됩니다
                ServerFunction.getInstance().GetLatestPatientRouteData();
                // 이걸로 접근하시면 됩니다!
                // ServerFunction.getInstance().patientRouteResponse.m_latitude
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
        hideSoftKeyboard();
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
        Toast.makeText(this, current_lat + ", " + current_long, Toast.LENGTH_SHORT).show();
        LatLng center = new LatLng(current_lat, current_long);
        if (preCircle != null) {
            preCircle.remove();
        }
        preCircle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(500)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT)
        );
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
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            init();
        }
        //클러스터
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

//        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager); //added


        mClusterManager.setRenderer(new MyClusterRenderer(this, mMap, mClusterManager));

        addItems();

    }

    // 확진자 추가 함수
    private void addItems() {
        // Set some lat/lng coordinates to start with.
        int Corona_Confirmer = 16;
        double[] lat = new double[Corona_Confirmer];
        double[] lng = new double[Corona_Confirmer];

        //  Set the title and snippet strings.
        String[] title = new String[Corona_Confirmer];
        String[] snippet = new String[Corona_Confirmer];

        LatLng center;
        float color_code = 0;
        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < Corona_Confirmer; i++) {
            switch (i) {
                case 0:
                    lat[i] = 37.550498;
                    lng[i] = 127.173193;
                    title[i] = "exercise";
                    snippet[i] = "1";
                    color_code = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 1:
                    lat[i] = 37.550498;
                    lng[i] = 127.073193;
                    title[i] = "확진자1";
                    snippet[i] = "세종대학교";
                    color_code = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 2:
                    lat[i] = 37.541009;
                    lng[i] = 127.079311;
                    title[i] = "확진자2";
                    snippet[i] = "건국대학교";
                    color_code = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 3:
                    lat[i] = 37.547985;
                    lng[i] = 127.074646;
                    title[i] = "확진자3";
                    snippet[i] = "어린이대공원역";
                    color_code = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 4:
                    lat[i] = 37.546590;
                    lng[i] = 127.074963;
                    title[i] = "확진자4";
                    snippet[i] = "어대역 뚜레쥬르";
                    color_code = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 5:
                    lat[i] = 37.552964;
                    lng[i] = 127.076774;
                    title[i] = "확진자5";
                    snippet[i] = "어대역 도미노피자";
                    color_code = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 6:
                    lat[i] = 37.554498;
                    lng[i] = 127.075193;
                    title[i] = "확진자6";
                    snippet[i] = "6";
                    color_code = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case 7:
                    lat[i] = 37.543009;
                    lng[i] = 127.077311;
                    title[i] = "확진자7";
                    snippet[i] = "7";
                    color_code = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case 8:
                    lat[i] = 37.548985;
                    lng[i] = 127.074646;
                    title[i] = "확진자8";
                    snippet[i] = "8";
                    color_code = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case 9:
                    lat[i] = 37.544590;
                    lng[i] = 127.075963;
                    title[i] = "확진자9";
                    snippet[i] = "9";
                    color_code = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case 10:
                    lat[i] = 37.554364;
                    lng[i] = 127.075774;
                    title[i] = "확진자10";
                    snippet[i] = "10";
                    color_code = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
                case 11:
                    lat[i] = 37.550698;
                    lng[i] = 127.073093;
                    title[i] = "확진자11";
                    snippet[i] = "11";
                    color_code = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case 12:
                    lat[i] = 37.541439;
                    lng[i] = 127.079711;
                    title[i] = "확진자12";
                    snippet[i] = "12";
                    color_code = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case 13:
                    lat[i] = 37.547485;
                    lng[i] = 127.074146;
                    title[i] = "확진자13";
                    snippet[i] = "13";
                    color_code = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case 14:
                    lat[i] = 37.546190;
                    lng[i] = 127.074263;
                    title[i] = "확진자14";
                    snippet[i] = "14";
                    color_code = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case 15:
                    lat[i] = 37.552464;
                    lng[i] = 127.076574;
                    title[i] = "확진자15";
                    snippet[i] = "15";
                    color_code = BitmapDescriptorFactory.HUE_GREEN;
                    break;
            }
            MyItem offsetItem = new MyItem(lat[i], lng[i], title[i], snippet[i], color_code);

            mClusterManager.addItem(offsetItem);
        }
    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {
        public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(item.getColor_code());

            markerOptions.icon(markerDescriptor);
        }
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
                    .title(title);
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

    /* 안드로이드 고유 아이디 (UUID) 초기화 및 사용자옵션 데이터베이스에서 가져옴 */
    public void InitDeviceUUID() throws IOException {
        try {
            // 파일이 존재하면 파일에서 아이디 가져옴
            FileInputStream fis = openFileInput("UUID.txt");
            StringBuffer data = new StringBuffer();
            fis = openFileInput("UUID.txt");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            String strTemp = buffer.readLine();
            while (strTemp != null) {
                data.append(strTemp + "\n");
                strTemp = buffer.readLine();
            }
            m_DeviceID = data.toString();
            Log.e("UUID", m_DeviceID + "파일 불러오기 완료");
            buffer.close();

        } catch (Exception e) {
            // 파일이 존재하지 않으면 새로 할당받아 파일을 작성해줌
            m_DeviceID = UUID.randomUUID().toString();

            FileOutputStream fos = openFileOutput("UUID.txt", Context.MODE_APPEND);
            PrintWriter out = new PrintWriter(fos);
            out.println(m_DeviceID);
            out.close();
            Log.e("UUID", m_DeviceID + "파일 저장 완료");

            // DB 에 정보 저장
            ServerFunction.getInstance().SendUserOption(m_DeviceID, 5, 30);
        }
    }
}
