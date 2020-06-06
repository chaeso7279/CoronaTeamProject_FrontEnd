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

import com.example.atchui.network.ServerFunction;

public class Noti_Fragment extends Fragment implements Noti_RecyclerAdapter.OnListItemSelectedInterface {
    private static final int CURRENT_NOTIFICATION = 0;
    private static final int PATH_NOTIFICATION = 1;

    private Noti_RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    //private SharedViewModel sharedViewModel;

    //private Noti_RecyclerItem newItem = new Noti_RecyclerItem();

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

//        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
//
//        sharedViewModel.getItem().observe(this, new Observer<Noti_RecyclerItem>() {
//            @Override
//            public void onChanged(@Nullable Noti_RecyclerItem item) {
//                setItem(item);
//                Log.d("newItem", item.getTextStr() +"");
//
//            }
//        });
    }

    private void Initialize(View view) {

        recyclerView = view.findViewById(R.id.recycler_noti);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Noti_RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }
    private void setData() {
        //ItemType: CURRENT_NOTIFICATION: 현위치 알림(0), PATH_NOTIFICATION: 지난경로 알림(1)

        int size = ServerFunction.getInstance().lstAnal.size(); //리스트의 크기

        for(int i = 0 ; i < size ; i++){
            //읽은 알림(이전 알림)일 경우
            if(ServerFunction.getInstance().lstAnal.get(i).m_IsRead == 1){
                //
                int index = i;  //서버 list 내 인덱스
                int itemType = ServerFunction.getInstance().lstAnal.get(i).m_IsPast;    //TODO:정수 이거에 맞게 코드 고쳐야 함(1,2 -> 0,1)
                int labelColor = this.getResources().getColor(R.color.label_green);     //TODO:table에 column 추가 후 제대로 받아오기(현재는 임시)
                String location = ServerFunction.getInstance().lstAnal.get(i).m_locationName;
                String user_time = "0000-00-00";                                        //TODO: table에 column 추가 후 제대로 받아오기(현재는 임시)
                String timeStr = ServerFunction.getInstance().lstAnal.get(i).m_analTime;

                //past일 경우
                if(itemType == PATH_NOTIFICATION){
                    String context = String.format(getResources().getString(R.string.noti_past),location, user_time);
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, itemType, labelColor, context, timeStr);
                    adapter.addItem(item);
                }
                //current일 경우
                else if(itemType == CURRENT_NOTIFICATION){
                    String context = String.format(getResources().getString(R.string.noti_current),1); //TODO: 반경 Nkm의 N 받아오기(현재는 임시)
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, itemType, labelColor, context, timeStr);
                    adapter.addItem(item);
                }

                adapter.notifyDataSetChanged();
            }
        }

//        Noti_RecyclerItem item = new Noti_RecyclerItem();
//        item.setItemType(PATH_NOTIFICATION);
//        item.setLabelColor(this.getResources().getColor(R.color.label_red));
//        item.setTextStr("강남구청 근방에서 2020-05-28에 동선겹침이 확인되었습니다.");
//        item.setTimeStr("6분 전");
//
//        adapter.addItem(item);
//
//        item = new Noti_RecyclerItem();
//        item.setItemType(CURRENT_NOTIFICATION);
//        item.setLabelColor(this.getResources().getColor(R.color.label_yellow));
//        item.setTextStr("반경 1km 내에 확진자 동선이 확인되었습니다.");
//        item.setTimeStr("5분 전");
    }

    private void setItem(Noti_RecyclerItem item) {
        adapter.addItem(item);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(View v, int position) {
        Noti_RecyclerAdapter.Noti_ItemViewHolder viewHolder =
                (Noti_RecyclerAdapter.Noti_ItemViewHolder)recyclerView.findViewHolderForAdapterPosition(position);

        if(viewHolder.itemType == CURRENT_NOTIFICATION){
            Intent intent = new Intent(getActivity(), CurrentResultActivity.class);

            //데이터 송신 - 실제 포지션에 맞는 정보 출력할 때 사용
            intent.putExtra("lstIndex", viewHolder.lstIndex);

            startActivity(intent);
        }
        else if(viewHolder.itemType == PATH_NOTIFICATION){
            Intent intent = new Intent(getActivity(), PathResultActivity.class);

            //데이터 송신 - 실제 포지션에 맞는 정보 출력할 때 사용
            intent.putExtra("lstIndex", viewHolder.lstIndex);

            startActivity(intent);
        }
    }
}
