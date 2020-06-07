package com.example.atchui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.atchui.database.SettingData;
import com.example.atchui.network.DataManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Noti_newFragment extends Fragment implements Noti_RecyclerAdapter.OnListItemSelectedInterface {
    private static final int CURRENT_NOTIFICATION = 0;
    private static final int PATH_NOTIFICATION = 1;

    private Noti_newRecyclerAdapter new_adapter;
    private RecyclerView new_recyclerView;

    //private SharedViewModel sharedViewModel; //fragment간 text전달을 위해 만듬
z
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_noti_new_fragment, container, false);


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

        //sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class); //fragment간 text전달을 위해 만듬
    }

    private void Initialize(View view) {

        new_recyclerView = view.findViewById(R.id.recycler_newnoti);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        new_recyclerView.setLayoutManager(linearLayoutManager);

        new_adapter = new Noti_newRecyclerAdapter(this);
        new_recyclerView.setAdapter(new_adapter);

    }
    private void setData() {
        //ItemType: CURRENT_NOTIFICATION: 현위치 알림(0), PATH_NOTIFICATION: 지난경로 알림(1)

        int size = DataManager.getInstance().lstAnal.size(); //리스트의 크기

        for(int i = 0 ; i < size ; i++){
            //읽지 않은 알림(새로운 알림)일 경우
            if(DataManager.getInstance().lstAnal.get(i).m_IsRead == 0){
                //
                int index = i;  //서버 list 내 인덱스
                int itemType = DataManager.getInstance().lstAnal.get(i).m_IsPast;
                int labelColor = this.getResources().getColor(R.color.label_green);     //TODO:table에 column 추가 후 제대로 받아오기(현재는 임시)
                String location = DataManager.getInstance().lstAnal.get(i).m_locationName;
                String user_time = "0000-00-00";                                        //TODO: table에 column 추가 후 제대로 받아오기(현재는 임시)
                String timeStr = DataManager.getInstance().lstAnal.get(i).m_analTime;

                //past일 경우
                if(itemType == PATH_NOTIFICATION){
                    String context = String.format(getResources().getString(R.string.noti_past),location, user_time);
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, itemType, labelColor, context, timeStr);
                    new_adapter.addItem(item);
                }
                //current일 경우
                else if(itemType == CURRENT_NOTIFICATION){
                    int range = DataManager.getInstance().Option.m_iRadius;
                    String context = String.format(getResources().getString(R.string.noti_current),range);
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, itemType, labelColor, context, timeStr);
                    new_adapter.addItem(item);
                }

                new_adapter.notifyDataSetChanged();
            }
        }

//        Noti_RecyclerItem item = new Noti_RecyclerItem();
//
//        item.setItemType(CURRENT_NOTIFICATION);
//        item.setLabelColor(this.getResources().getColor(R.color.label_green));
//        item.setTextStr("반경 1km 내에 확진자 동선이 확인되었습니다.");
//        item.setTimeStr("1분 전");
//
//        new_adapter.addItem(item);
//
//        item = new Noti_RecyclerItem();
//        item.setItemType(PATH_NOTIFICATION);
//        item.setLabelColor(this.getResources().getColor(R.color.label_red));
//        item.setTextStr("강남구청 근방에서 2020-05-28에 동선겹침이 확인되었습니다.");
//        item.setTimeStr("1분 전");
//

    }
    @Override
    public void onItemSelected(View v, int position) {

        Noti_newRecyclerAdapter.Noti_newItemViewHolder viewHolder =
                (Noti_newRecyclerAdapter.Noti_newItemViewHolder)new_recyclerView.findViewHolderForAdapterPosition(position);


        //itemType에 맞게 activity 이동
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
//
//        //fragment간 text전달을 위해 만듬
//        if(sharedViewModel!=null){
//            int itemType = viewHolder.itemType;
//            ColorDrawable drawable = (ColorDrawable) viewHolder.labelColor.getBackground();
//            drawable.getColor();
//            int labelColor = drawable.getColor();
//            String contentStr = viewHolder.textContent.getText().toString();
//            String timeStr = viewHolder.textTime.getText().toString();
//            Noti_RecyclerItem item = new Noti_RecyclerItem(itemType, labelColor, contentStr, timeStr);
//            sharedViewModel.setItem(item);
//        }
    }
}
