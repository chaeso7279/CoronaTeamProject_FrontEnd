package com.example.atchui;

public class Noti_RecyclerItem {
    //    private Drawable labelDrawable;
    private String textStr;
    private String timeStr;

//    public void setLabel(Drawable label) {
//        labelDrawable = label ;
//    }

    public void setTextStr(String text){
        textStr = text;
    }
    public void setTimeStr(String time){
        timeStr = time;
    }

    //    public Drawable getLabel(){
//        return this.labelDrawable;
//    }
    public String getTextStr(){
        return this.textStr;
    }

    public String getTimeStr(){
        return this.timeStr;
    }

}
