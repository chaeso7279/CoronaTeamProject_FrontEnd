package com.example.atchui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationListActivity extends AppCompatActivity implements Noti_RecyclerAdapter.OnListItemSelectedInterface {

    private static final int CURRENT_NOTIFICATION = 1;
    private static final int PATH_NOTIFICATION = 2;

    private Noti_RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        Initialize();
        setData();
    }

    private void Initialize() {
        mRecyclerView = findViewById(R.id.recycler_noti);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new Noti_RecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setData() {
        //ItemType: CURRENT_NOTIFICATION: 현위치 알림, PATH_NOTIFICATION: 지난경로 알림

        Noti_RecyclerItem item = new Noti_RecyclerItem();
        item.setItemType(CURRENT_NOTIFICATION);
        item.setLabelColor(this.getResources().getColor(R.color.label_green));
        item.setTextStr("반경 1km 내에 확진자 동선이 확인되었습니다.");
        item.setTimeStr("5분 전");

        mAdapter.addItem(item);

        item = new Noti_RecyclerItem();
        item.setItemType(PATH_NOTIFICATION);
        item.setLabelColor(this.getResources().getColor(R.color.label_red));
        item.setTextStr("강남구청 근방에서 2020-05-28에 동선겹침이 확인되었습니다.");
        item.setTimeStr("1분 전");

        mAdapter.addItem(item);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemSelected(View v, int position) {
        Noti_RecyclerAdapter.Noti_ItemViewHolder viewHolder =
                (Noti_RecyclerAdapter.Noti_ItemViewHolder)mRecyclerView.findViewHolderForAdapterPosition(position);
        Toast.makeText(this, viewHolder.textTime.getText().toString(), Toast.LENGTH_SHORT).show();

        if(viewHolder.itemType == 1){
            Intent intent = new Intent(getApplicationContext(), CurrentResultActivity.class);

            //데이터(position) 송신
            intent.putExtra("position",position);

            startActivity(intent);
        }
        else if(viewHolder.itemType == 2){
            Intent intent = new Intent(getApplicationContext(), PathResultActivity.class);

            //데이터(position) 송신
            intent.putExtra("position",position);

            startActivity(intent);
        }

    }
}
