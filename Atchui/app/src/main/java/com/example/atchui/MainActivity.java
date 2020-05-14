package com.example.atchui;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String CLIENT_ID = "wst0xgno8h";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient(CLIENT_ID)
        );

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    // 위치 권한 설정
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                map.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*
    * 지도와 관려뇐 모든 조작은 Navermap 클래스를 통해 이루어진다.
    * 인스턴스는 직접 생성할 수 없고 이와 같은 메서드를 호출해 비동기적으로 얻어야 한다.
     */
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        map = naverMap;
        map.setLocationSource(locationSource);
        // 현위치 버튼 컨트롤 사용 (=클릭에 따라 위치 추적 모드 변경)
        map.getUiSettings().setLocationButtonEnabled(true);
        // 위치 추적 모드 지정 (None / NoFollow / Follow / Face)
        map.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 위치 변경 이벤트 리스너 등록 (자바 8 이상 람다식 표현)
        map.addOnLocationChangeListener(location ->
            Toast.makeText(this,
            location.getLatitude()+", "+location.getLongitude(),
                Toast.LENGTH_SHORT).show()
        );
        /* 람다식 표현이 아니면 이렇게
        map.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                Toast.makeText(getApplicationContext(),
                        location.getLatitude()+", "+location.getLongitude(),
                        Toast.LENGTH_SHORT).show());
            }
        });
         */

    }
}
