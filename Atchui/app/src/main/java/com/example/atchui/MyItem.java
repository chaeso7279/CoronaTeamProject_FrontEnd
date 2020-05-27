package com.example.atchui;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


// 클러스터 이용
public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;



    public MyItem(double lat, double lng, String mTitle, String mSnippet) {
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
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
}
