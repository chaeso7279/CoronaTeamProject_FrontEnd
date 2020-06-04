package com.example.atchui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.ui.IconGenerator;


// 클러스터 이용
public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
//    private final float color_code;
    private Context contextN;
    Drawable mclusterIcon;
    Bitmap mIcon;

    public MyItem(double lat, double lng, String mTitle, String mSnippet, Bitmap mIcon) {
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
//        this.color_code = color_code;
        this.mIcon = mIcon;
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

//    public float getColor_code() {
//        return color_code;
//    }
    public Bitmap getIcon_code() {
        return mIcon;
    }

}
