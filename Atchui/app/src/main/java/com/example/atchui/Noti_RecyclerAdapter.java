package com.example.atchui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Noti_RecyclerAdapter extends RecyclerView.Adapter<Noti_RecyclerAdapter.ViewHolder> {

    private ArrayList<Noti_RecyclerItem> mData = null;

    //생성자에서 데이터 리스트 객체를 전달받음
    Noti_RecyclerAdapter(ArrayList<Noti_RecyclerItem> list){
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public Noti_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context  =parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item,parent, false);
        Noti_RecyclerAdapter.ViewHolder vh = new Noti_RecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Noti_RecyclerAdapter.ViewHolder holder, int position) {
        Noti_RecyclerItem item = mData.get(position);

//        holder.label.set
        holder.text.setText(item.getTextStr());
        holder.time.setText(item.getTimeStr());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

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

}
