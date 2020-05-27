package com.example.atchui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationList_RecyclerImageTextAdapter extends RecyclerView.Adapter<NotificationList_RecyclerImageTextAdapter.ViewHolder>{
    private ArrayList<NotificationList_RecyclerItem> mData = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView label ;
        TextView text ;
        TextView time ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
//            label = (ImageView)itemView.findViewById(R.id.item_label) ;
            text = (TextView)itemView.findViewById(R.id.notification_text) ;
            time = (TextView)itemView.findViewById(R.id.notification_time) ;
        }
    }

    //생성자에서 데이터 리스트 객체를 전달받음
    NotificationList_RecyclerImageTextAdapter(ArrayList<NotificationList_RecyclerItem> list){
        this.mData = list;
    }

    //onCreateViewHolder() : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    //onBindViewHolder() : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(NotificationList_RecyclerImageTextAdapter.ViewHolder holder, int position) {
        NotificationList_RecyclerItem item = mData.get(position);

//        holder.label.setBackgroundResource(R.color.colorPrimary);
        holder.text.setText(item.getText());
        holder.time.setText(item.getTime());
    }

    // getItemCount() : 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return (null != mData ? mData.size() : 0);
    }




}
