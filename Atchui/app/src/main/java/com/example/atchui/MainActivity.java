package com.example.atchui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private ClusterManager<MyItem> mClusterManager;
    private MyItem clickedClusterItem;
    //widgets
    private EditText mSearchText;
    private ImageView mGps;

    //vars
    private Boolean mLocationPermissionsGranted =false;

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
                        if(!task.isSuccessful()){
                            Log.w("FCM Log", "getInstanced failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰"+ token);
                        Toast.makeText(MainActivity.this, "토큰:"+token, Toast.LENGTH_SHORT).show();
                    }
                });


        MapFragment mapFragment1 = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

        mSearchText = (EditText)findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mapFragment.getMapAsync(this);


        //////////////////////////////////////
        /*button 누를 시 Activity 이동*/

        Button btn_notification = (Button)findViewById(R.id.btn_notification_list);
        Button btn_setting = (Button)findViewById(R.id.btn_setting);
        Button btn_help = (Button)findViewById(R.id.btn_help);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        //////////////////////////////////////
        /*BackgroundService*/
        //서비스 시작
        Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,BackgroundService.class);
        startService(intent);

    }

    private void init(){
        Log.d(TAG,"init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER){
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

    private void geoLocate(){

        String serchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(serchString,1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate : IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
//          Toast.makeText(this,address.toString(),Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Toast.makeText(this,"map is Ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady : map is ready");
        mMap = googleMap;

        if(mLocationPermissionsGranted){
            getDeviceLocation();

            //자신의 위치 나타내기
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    !=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            mMap.setMyLocationEnabled(true);
            //자기 위치 찾는거 지운다
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
//        LatLng semin = new LatLng(37.2706008,127.0135755999997);
//        mMap.addMarker(new MarkerOptions().position(semin).title("세종대학교"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(semin));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
//
//        mMap.setOnInfoWindowCloseListener((marker -> {
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("tel:0312345041"));
//            if(intent.resolveActivity(getPackageManager())!=null){
//                startActivity(intent);
//            }
//        }));

        mClusterManager = new ClusterManager<>(this,mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);


        mClusterManager.setRenderer(new MyClusterRenderer(this, mMap, mClusterManager));
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager); //added
        mClusterManager
                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                    @Override
                    public boolean onClusterItemClick(MyItem item) {
                        clickedClusterItem = item;
                        return false;
                    }
                });

        addItems();
    }

    // 확진자 추가 함수
    private void addItems() {
// Set some lat/lng coordinates to start with.
        int Corona_Confirmer = 5;
        double[] lat = new double[Corona_Confirmer];
        double[] lng = new double[Corona_Confirmer];

//        Set the title and snippet strings.
        String[] title = new String[Corona_Confirmer];
        String[] snippet = new String[Corona_Confirmer];


        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < Corona_Confirmer; i++) {
             switch (i) {
                case 0:
                    lat[i] = 37.550498;
                    lng[i] = 127.073193;
                    title[i] = "확진자1";
                    snippet[i] = "세종대학교";
                    break;
                case 1:
                    lat[i] = 37.541009;
                    lng[i] = 127.079311;
                    title[i] = "확진자2";
                    snippet[i] = "건국대학교";
                    break;
                case 2:
                    lat[i] = 37.547985;
                    lng[i] = 127.074646;
                    title[i] = "확진자3";
                    snippet[i] = "어린이대공원역";
                    break;
                case 3:
                    lat[i] = 37.546590;
                    lng[i] = 127.074963;
                    title[i] = "확진자4";
                    snippet[i] = "어대역 뚜레쥬르";
                    break;
                 case 4:
                     lat[i] = 37.552964;
                     lng[i] = 127.076774;
                     title[i] = "확진자5";
                     snippet[i] = "어대역 도미노피자";
                     break;
            }
            MyItem offsetItem = new MyItem(lat[i], lng[i], title[i], snippet[i]);
            mClusterManager.addItem(offsetItem);
        }
    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {

        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);

            markerOptions.icon(markerDescriptor);
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions){

            final Drawable clusterIcon = getResources().getDrawable(R.drawable.ic_lens_black_24dp);
            clusterIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);

            mClusterIconGenerator.setBackground(clusterIcon);

            //modify padding for one or two digit numbers
            if (cluster.getSize() < 20) {
                mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
            }
            else {
                mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
            }

            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }

    //현위치 가져오기
    private void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation: getting the current devices location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete: found location!");
                            Location currentLocation = (Location)task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");
                        }else{
                            Log.d(TAG,"onComplete: current location is null");
                            Toast.makeText(MainActivity.this,"unable to get current location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng,float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: "+ latLng.latitude + ", lng: "+ latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG,"initMap:initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
    }

    //위치정보 동의
    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult: called");
        mLocationPermissionsGranted=false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG,"onRequestPermissionsResult: failed");
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionsResult: permission granted");

                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
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
}
