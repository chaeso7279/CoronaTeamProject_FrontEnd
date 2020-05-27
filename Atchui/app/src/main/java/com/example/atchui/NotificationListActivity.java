package com.example.atchui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;

public class NotificationListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView = null;
    NotificationList_RecyclerImageTextAdapter mAdapter = null;
    ArrayList<NotificationList_RecyclerItem> mList = new ArrayList<NotificationList_RecyclerItem>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        mRecyclerView = findViewById(R.id.recycler_notification);

        //RecyclerView에 SimpleTextAdapter 객체 지정
        mAdapter = new NotificationList_RecyclerImageTextAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //아이템 추가 테스트
        addItem(getDrawable(R.drawable.btn_help),
                "반경 3km 내에 확진자 방문 장소가 확인되었습니다", "1분 전");
    }

    public void addItem(Drawable label, String text, String time){
        NotificationList_RecyclerItem item = new NotificationList_RecyclerItem();

        item.setLabel(label);
        item.setText(text);
        item.setTime(time);
    }
}
