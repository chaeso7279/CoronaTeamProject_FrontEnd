package com.example.atchui;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


// 클러스터 이용
public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final float color_code;


    public MyItem(double lat, double lng, String mTitle, String mSnippet, float color_code) {
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        this.color_code = color_code;
        mPosition = new LatLng(lat, lng);
    }
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public float getColor_code() {
        return color_code;
    }
}
