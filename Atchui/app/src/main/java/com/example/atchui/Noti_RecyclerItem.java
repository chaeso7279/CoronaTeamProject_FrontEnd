package com.example.atchui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class Noti_RecyclerItem {
    private int labelColor;
    private String contentStr;
    private String timeStr;

    public void setLabelColor(int label){
        labelColor = label;
    }
    public void setTextStr(String text){
        contentStr = text;
    }
    public void setTimeStr(String time){
        timeStr = time;
    }


    public int getLabelColor() {return labelColor;}
    public String getTextStr(){
        return contentStr;
    }
    public String getTimeStr(){
        return timeStr;
    }

}
