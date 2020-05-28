package com.example.atchui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Noti_Fragment extends Fragment implements Noti_RecyclerAdapter.OnListItemSelectedInterface {
    private static final int CURRENT_NOTIFICATION = 1;
    private static final int PATH_NOTIFICATION = 2;
    private Noti_RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_noti__fragment, container, false);

        Initialize(view);
        setData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void Initialize(View view) {

        recyclerView = view.findViewById(R.id.recycler_noti);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Noti_RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }
    private void setData() {
        //ItemType: CURRENT_NOTIFICATION: 현위치 알림, PATH_NOTIFICATION: 지난경로 알림

        Noti_RecyclerItem item = new Noti_RecyclerItem();
        item.setItemType(CURRENT_NOTIFICATION);
        item.setLabelColor(this.getResources().getColor(R.color.label_green));
        item.setTextStr("반경 1km 내에 확진자 동선이 확인되었습니다.");
        item.setTimeStr("5분 전");

        adapter.addItem(item);

        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemSelected(View v, int position) {
        Noti_RecyclerAdapter.Noti_ItemViewHolder viewHolder =
                (Noti_RecyclerAdapter.Noti_ItemViewHolder)recyclerView.findViewHolderForAdapterPosition(position);

        if(viewHolder.itemType == 1){
            Intent intent = new Intent(getActivity(), CurrentResultActivity.class);

            //데이터(position) 송신
            intent.putExtra("position",position);

            startActivity(intent);
        }
        else if(viewHolder.itemType == 2){
            Intent intent = new Intent(getActivity(), PathResultActivity.class);

            //데이터(position) 송신
            intent.putExtra("position",position);

            startActivity(intent);
        }
    }
}
