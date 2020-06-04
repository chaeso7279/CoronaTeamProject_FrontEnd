package com.example.atchui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Noti_Fragment extends Fragment implements Noti_RecyclerAdapter.OnListItemSelectedInterface {
    private static final int CURRENT_NOTIFICATION = 1;
    private static final int PATH_NOTIFICATION = 2;
    private Noti_RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private SharedViewModel sharedViewModel;

    private Noti_RecyclerItem newItem = new Noti_RecyclerItem();

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

        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        sharedViewModel.getItem().observe(this, new Observer<Noti_RecyclerItem>() {
            @Override
            public void onChanged(@Nullable Noti_RecyclerItem item) {
                setItem(item);
                Log.d("newItem", item.getTextStr() +"");

            }
        });
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
        item.setItemType(PATH_NOTIFICATION);
        item.setLabelColor(this.getResources().getColor(R.color.label_red));
        item.setTextStr("강남구청 근방에서 2020-05-28에 동선겹침이 확인되었습니다.");
        item.setTimeStr("6분 전");

        adapter.addItem(item);

        item = new Noti_RecyclerItem();
        item.setItemType(CURRENT_NOTIFICATION);
        item.setLabelColor(this.getResources().getColor(R.color.label_yellow));
        item.setTextStr("반경 1km 내에 확진자 동선이 확인되었습니다.");
        item.setTimeStr("5분 전");

        adapter.addItem(item);

        adapter.notifyDataSetChanged();
    }

    private void setData(int itemType, int labelColor, String contentStr, String timeStr) {

        Noti_RecyclerItem item = new Noti_RecyclerItem(itemType, labelColor, contentStr, timeStr);

        adapter.addItem(item);

        adapter.notifyDataSetChanged();
    }

    private void setItem(Noti_RecyclerItem item) {
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
