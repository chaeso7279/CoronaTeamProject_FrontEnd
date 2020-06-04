package com.example.atchui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

//fragment간 text전달을 위해 만듬
public class SharedViewModel extends ViewModel {


    public MutableLiveData<Noti_RecyclerItem> item = new MutableLiveData<>();

    public LiveData<Noti_RecyclerItem> getItem(){
        return item;
    }

    public void setItem(Noti_RecyclerItem item){

        this.item.setValue(item);
//        this.itemType.setValue(itemType);
//        this.labelColor.setValue(labelColor);
//        this.contentStr.setValue(contentStr);
//        this.timeStr.setValue(timeStr);
    }
}


