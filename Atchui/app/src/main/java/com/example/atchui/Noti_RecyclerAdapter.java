package com.example.atchui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Noti_RecyclerAdapter extends RecyclerView.Adapter<Noti_RecyclerAdapter.Noti_ItemViewHolder> {

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;


    //어댑터에 들어갈 데이터 리스트
    private ArrayList<Noti_RecyclerItem> lstData = new ArrayList<>();

    public Noti_RecyclerAdapter(OnListItemSelectedInterface listener){
        this.mListener = listener;
    }
    @NonNull
    @Override
    public Noti_ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_noti, parent, false);
        return new Noti_ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Noti_RecyclerAdapter.Noti_ItemViewHolder holder, int position) {
        // item을 하나 하나 보여주는(bind 되는) 함수
        holder.onBind(lstData.get(position));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    void addItem(Noti_RecyclerItem data) { lstData.add(0,data); }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class Noti_ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView labelColor;
        TextView textContent ;
        TextView textTime ;

        Noti_ItemViewHolder(View itemView) {
            super(itemView) ;

            labelColor = (ImageView)itemView.findViewById(R.id.imageView_label);
            textContent = (TextView)itemView.findViewById(R.id.textView_Content) ;
            textTime = (TextView)itemView.findViewById(R.id.textview_Time) ;

            //Item Click Event Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());

                    Log.d("Recyclerview", "position = "+ getAdapterPosition());
                }
            });
        }

        void onBind(Noti_RecyclerItem item) {
            labelColor.setBackgroundColor(item.getLabelColor());
            textContent.setText(item.getTextStr());
            textTime.setText(item.getTimeStr());
        }

    }

}
