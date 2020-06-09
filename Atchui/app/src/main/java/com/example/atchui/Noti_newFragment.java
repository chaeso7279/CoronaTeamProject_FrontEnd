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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Noti_newFragment extends Fragment implements Noti_RecyclerAdapter.OnListItemSelectedInterface {
    private static final int CURRENT_NOTIFICATION = 0;
    private static final int PATH_NOTIFICATION = 1;

    private Noti_RecyclerAdapter new_adapter;
    private RecyclerView new_recyclerView;

    //private SharedViewModel sharedViewModel; //fragment간 text전달을 위해 만듬

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

        new_adapter = new Noti_RecyclerAdapter(this);
        new_recyclerView.setAdapter(new_adapter);

    }
    private void setData() {
        //ItemType: CURRENT_NOTIFICATION: 현위치 알림(0), PATH_NOTIFICATION: 지난경로 알림(1)

        int size = DataManager.getInstance().lstAnal.size(); //리스트의 크기

        for(int i = 0 ; i < size ; i++){
            //읽지 않은 알림(새로운 알림)일 경우
            if(DataManager.getInstance().lstAnal.get(i).m_IsRead == 0){
                //

//                String cnf_id = DataManager.getInstance().lstAnal.get(i).m_cnfID; //확진자id
//                double cnf_latitude = DataManager.getInstance().lstAnal.get(i).m_cnfLatitude;    //확진자위도
//                double cnf_longitude = DataManager.getInstance().lstAnal.get(i).m_cnfLongitude;   //확진자경도
//                double user_latitude = DataManager.getInstance().lstAnal.get(i).m_userLatitude;   //사용자위도
//                double user_longitude = DataManager.getInstance().lstAnal.get(i).m_userLongitude;  //사용자경도


                //확진자정보
                String location_name = DataManager.getInstance().lstAnal.get(i).m_locationName;   //방문장소명
                int labelColor =  DataManager.getInstance().lstAnal.get(i).m_color;    //라벨컬러

                //사용자정보
                String user_time = DataManager.getInstance().lstAnal.get(i).m_analTime;           //TODO: table에 column 추가 후 제대로 받아오기(현재는 임시)

                //Noti정보
                int index = i;  //서버 list 내 인덱스
                String anal_time = DataManager.getInstance().lstAnal.get(i).m_analTime;   //분석시간
                int itemType = DataManager.getInstance().lstAnal.get(i).m_IsPast; //과거기반?현재기반?
                int isRead = DataManager.getInstance().lstAnal.get(i).m_IsRead; //읽었는지

                String user_timeStr = String.format(getResources().getString(R.string.noti_time),user_time.substring(0,10),user_time.substring(11,19));
                Log.d("사용자 시간", user_timeStr);  //TODO: 맞는지 확인

                String anal_timeStr = String.format(getResources().getString(R.string.noti_time),anal_time.substring(0,10),anal_time.substring(11,19));
                Log.d("분석 시간", anal_timeStr); //TODO: 맞는지 확인

                String diffStr = analTimeDiff(anal_timeStr);

                //past일 경우
                if(itemType == PATH_NOTIFICATION){
                    String context = String.format(getResources().getString(R.string.noti_past),location_name, user_time.substring(0,10));
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, itemType, labelColor, context, diffStr);
                    new_adapter.addItem(item);
                }
                //current일 경우
                else if(itemType == CURRENT_NOTIFICATION){
                    int range = DataManager.getInstance().Option.m_iRadius;
                    String context = String.format(getResources().getString(R.string.noti_current),range);
                    Noti_RecyclerItem item = new Noti_RecyclerItem(index, itemType, labelColor, context, diffStr);
                    new_adapter.addItem(item);
                }

                new_adapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onItemSelected(View v, int position) {

        Noti_newRecyclerAdapter.Noti_newItemViewHolder viewHolder =
                (Noti_newRecyclerAdapter.Noti_newItemViewHolder)new_recyclerView.findViewHolderForAdapterPosition(position);


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
            DataManager.getInstance().UpdateAnalIsRead(viewHolder.lstIndex, true);
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
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
            return now_s-anal_s+"초 전";
        }
        return diffStr;
    }
}
