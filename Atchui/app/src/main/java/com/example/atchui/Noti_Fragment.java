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

import com.example.atchui.network.DataEventListener;
import com.example.atchui.network.DataManager;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        // 이벤트 리스너 등록
        DataManager.getInstance().SetOnReceivedEvent(new DataEventListener() {
            @Override
            public void onReceivedEvent() {
                // 이벤트 수신
                Log.e("NotifyFragment", "Anal List Update!");
            }
        });
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
        //ItemType: CURRENT_NOTIFICATION: 현위치 알림(0), PATH_NOTIFICATION: 지난경로 알림(1)

        int size = DataManager.getInstance().lstAnal.size(); //리스트의 크기

        for(int i = 0 ; i < size ; i++){
            //읽은 알림(이전 알림)일 경우
            if(DataManager.getInstance().lstAnal.get(i).m_IsRead == 1){

                //확진자정보
                String location_name = DataManager.getInstance().lstAnal.get(i).m_locationName;   //방문장소명
                int labelColor =  DataManager.getInstance().lstAnal.get(i).m_color;    //라벨컬러

                //사용자정보
                String user_time = DataManager.getInstance().lstAnal.get(i).m_userVisitTime;

                //Noti정보
                int index = i;  //서버 list 내 인덱스
                String anal_time = DataManager.getInstance().lstAnal.get(i).m_analTime;   //분석시간
                int itemType = DataManager.getInstance().lstAnal.get(i).m_IsPast; //과거기반?현재기반?
                int analID = DataManager.getInstance().lstAnal.get(i).m_analID; // 분석 고유번호

                String user_timeStr = String.format(getResources().getString(R.string.noti_time),user_time.substring(0,10),user_time.substring(11,19));
                Log.d("사용자 시간", user_timeStr);

                String anal_timeStr = String.format(getResources().getString(R.string.noti_time),anal_time.substring(0,10),anal_time.substring(11,19));
                Log.d("분석 시간", anal_timeStr);

                String diffStr = analTimeDiff(anal_timeStr);

                //current일 경우
                if(itemType == CURRENT_NOTIFICATION || analID == 3){
                    int range = DataManager.getInstance().Option.m_iRadius;
                    String context = String.format(getResources().getString(R.string.noti_current),range*0.001);
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, CURRENT_NOTIFICATION, labelColor, analID, context, diffStr);
                    adapter.addItem(item);
                }
                //past일 경우
                else if(itemType == PATH_NOTIFICATION){
                    String context = String.format(getResources().getString(R.string.noti_past),location_name, user_time.substring(0,10));
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, itemType, labelColor,analID, context, diffStr);
                    adapter.addItem(item);
                }


                adapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onItemSelected(View v, int position) {
        Noti_RecyclerAdapter.Noti_ItemViewHolder viewHolder =
                (Noti_RecyclerAdapter.Noti_ItemViewHolder)recyclerView.findViewHolderForAdapterPosition(position);

        //itemType에 맞게 activity 이동
        if(viewHolder.itemType == CURRENT_NOTIFICATION){
            Intent intent = new Intent(getActivity(), CurrentResultActivity.class);

            intent.putExtra("lstIndex",viewHolder.lstIndex);
            DataManager.getInstance().UpdateAnalIsRead(viewHolder.lstIndex, true);
            startActivity(intent);
            if(getActivity() != null) {
                getActivity().finish();
            }
        }
        else if(viewHolder.itemType == PATH_NOTIFICATION){
            Intent intent = new Intent(getActivity(), PathResultActivity.class);

            intent.putExtra("lstIndex",viewHolder.lstIndex);

            startActivity(intent);
            if(getActivity() != null) {
                getActivity().finish();
            }
        }
    }
    private String analTimeDiff(String anal_timeStr){
        String diffStr = "오류";

        //현재시간
        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current_timeStr = dayTime.format(new Date(time));

        Log.d("차이/현재시간",current_timeStr);
        Log.d("차이/분석시간",anal_timeStr);

        //정수로 저장
        int anal_year = Integer.parseInt(anal_timeStr.substring(0,4));
        int anal_month = Integer.parseInt(anal_timeStr.substring(5,7));
        int anal_day = Integer.parseInt(anal_timeStr.substring(8,10));
        int anal_h = Integer.parseInt(anal_timeStr.substring(11,13));
        int anal_m = Integer.parseInt(anal_timeStr.substring(14,16));
        int anal_s = Integer.parseInt(anal_timeStr.substring(17,19));

        int now_year = Integer.parseInt(current_timeStr.substring(0,4));
        int now_month = Integer.parseInt(current_timeStr.substring(5,7));
        int now_day = Integer.parseInt(current_timeStr.substring(8,10));

        int now_h = Integer.parseInt(current_timeStr.substring(11,13));
        int now_m = Integer.parseInt(current_timeStr.substring(14,16));
        int now_s = Integer.parseInt(current_timeStr.substring(17,19));

//        Log.d("현재시간", current_timeStr);
//        Log.d("현재시간", now_year + " " + now_month+ " " +now_day+ " " +"/"+
//                now_h+ " " +now_m+ " " +now_s);
        if(now_year-anal_year!=0){
            return now_year-anal_year+"년 전";
        }
        if(now_month-anal_month!=0){
            return now_month-anal_month+"달 전";
        }
        if(now_day-anal_day!=0){
            return now_day-anal_day+"일 전";
        }
        if(now_h - anal_h != 0){
            return now_h-anal_h+ "시간 전";
        }
        if(now_m-anal_m!=0){
            return now_m-anal_m+"분 전";
        }
        if(now_s-anal_s!=0){
            return "방금 전";
        }
        return diffStr;
    }
}

