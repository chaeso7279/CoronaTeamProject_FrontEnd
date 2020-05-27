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

    RecyclerView mRecyclerView = null;
    NotificationList_RecyclerImageTextAdapter mAdapter = null;
    ArrayList<NotificationList_RecyclerItem> mList = new ArrayList<NotificationList_RecyclerItem>();
    int count = -1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_notification);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //RecyclerView에 SimpleTextAdapter 객체 지정
        mAdapter = new NotificationList_RecyclerImageTextAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //아이템 추가 테스트
        Button btn = (Button)findViewById(R.id.btn_insert);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
//                NotificationList_RecyclerItem data = new NotificationList_RecyclerItem(getDrawable(R.color.colorPrimary), "sss","sss");
                NotificationList_RecyclerItem data = new NotificationList_RecyclerItem(count+" ","sss");
                mList.add(0,data);

                mAdapter.notifyDataSetChanged();
            }
        });
//        addItem(getDrawable(R.color.colorPrimary),
//                "반경 3km 내에 확진자 방문 장소가 확인되었습니다", "1분 전");
    }

//    public void addItem(Drawable label, String text, String time){
//        NotificationList_RecyclerItem item = new NotificationList_RecyclerItem(label, text, time);
////
////        item.setLabel(label);
////        item.setText(text);
////        item.setTime(time);
//    }
}
