package com.example.atchui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationListActivity extends AppCompatActivity {

    Noti_RecyclerAdapter mAdapter = null;
    ArrayList<Noti_RecyclerItem> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        ////
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_notification);

        mAdapter = new Noti_RecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //아이템 추가
        addItem("안녕","제발 되렴");
        addItem("응?","제발");
        addItem("응?","개빡치게하지말고");

        mAdapter.notifyDataSetChanged();

        int totalElements = mList.size();// arrayList의 요소의 갯수를 구한다.
        for (int index = 0; index < totalElements; index++) {
            System.out.println(mList.get(index).getTimeStr());
        }
    }

    public void addItem(String text, String time){
        Noti_RecyclerItem item = new Noti_RecyclerItem();

        item.setTextStr(text);
        item.setTimeStr(time);

        mList.add(0,item);
    }

}
