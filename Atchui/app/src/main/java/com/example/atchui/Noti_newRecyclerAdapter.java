package com.example.atchui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Noti_newRecyclerAdapter extends RecyclerView.Adapter<Noti_newRecyclerAdapter.Noti_newItemViewHolder> {
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private Noti_RecyclerAdapter.OnListItemSelectedInterface mListener;


    //어댑터에 들어갈 데이터 리스트
    private ArrayList<Noti_RecyclerItem> lstData = new ArrayList<>();

    public Noti_newRecyclerAdapter(Noti_RecyclerAdapter.OnListItemSelectedInterface listener){
        this.mListener = listener;
    }


    @NonNull
    @Override
    public Noti_newRecyclerAdapter.Noti_newItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_noti, parent, false);
        return new Noti_newRecyclerAdapter.Noti_newItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Noti_newRecyclerAdapter.Noti_newItemViewHolder holder, int position) {
        holder.onBind(lstData.get(position));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    void addItem(Noti_RecyclerItem data) { lstData.add(0,data); }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class Noti_newItemViewHolder extends RecyclerView.ViewHolder {

        int lstIndex;
        int itemType;
        ImageView labelColor;
        TextView textContent ;
        TextView textTime ;

        LinearLayout itemLayout;

        Noti_newItemViewHolder(View itemView) {
            super(itemView) ;

            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.background_newItem));

            labelColor = (ImageView)itemView.findViewById(R.id.imageView_label);
            textContent = (TextView)itemView.findViewById(R.id.textView_Content) ;
            textTime = (TextView)itemView.findViewById(R.id.textview_Time) ;

            itemLayout = (LinearLayout)itemView.findViewById(R.id.item_layout);

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
            lstIndex = item.getLstIndex();
            itemType = item.getItemType();
            labelColor.setBackgroundColor(item.getLabelColor());
            textContent.setText(item.getTextStr());
            textTime.setText(item.getTimeStr());
        }

    }

}
