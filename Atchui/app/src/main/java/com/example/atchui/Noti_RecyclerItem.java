package com.example.atchui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.util.Observable;
import java.util.Observer;

public class Noti_RecyclerItem{
    private int itemType;
    private int labelColor;
    private String contentStr;
    private String timeStr;

    public void setItemType(int type) {itemType = type;}
    public void setLabelColor(int label){
        labelColor = label;
    }
    public void setTextStr(String text){
        contentStr = text;
    }
    public void setTimeStr(String time){
        timeStr = time;
    }


    public int getItemType() { return itemType;}
    public int getLabelColor() { return labelColor;}
    public String getTextStr(){
        return contentStr;
    }
    public String getTimeStr(){
        return timeStr;
    }

    Noti_RecyclerItem(){

    }
    Noti_RecyclerItem(int type, int label, String content, String time){
        itemType = type;
        labelColor = label;
        contentStr = content;
        timeStr = time;
    }
}

