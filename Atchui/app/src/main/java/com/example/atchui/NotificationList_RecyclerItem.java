package com.example.atchui;

import android.graphics.drawable.Drawable;

public class NotificationList_RecyclerItem {
    private Drawable labelDrawable;
    private String textStr;
    private String timeStr;

    public void setLabel(Drawable label){
        labelDrawable = label;
    }
    public void setText(String text){
        textStr = text;
    }
    public void setTime(String time){
        timeStr = time;
    }

    public Drawable getLabel(){
        return this.labelDrawable;
    }
    public String getText(){
        return this.textStr;
    }
    public String getTime(){
        return this.timeStr;
    }
}
