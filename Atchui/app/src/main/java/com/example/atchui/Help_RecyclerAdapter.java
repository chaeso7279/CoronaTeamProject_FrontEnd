package com.example.atchui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Help_RecyclerAdapter extends RecyclerView.Adapter<Help_RecyclerAdapter.Help_ItemViewHolder>  {

    // 어댑터에 들어갈 데이터 리스트
    private ArrayList<HelpData> lstData = new ArrayList<>();

    @NonNull
    @Override
    public Help_ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_help, parent, false);
        return new Help_ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Help_ItemViewHolder holder, int position) {
        // item을 하나 하나 보여주는(bind 되는) 함수
        holder.onBind(lstData.get(position));
    }

    @Override
    public int getItemCount() { return lstData.size(); }
    void addItem(HelpData data) { lstData.add(data); }

    class Help_ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textQuestion;
        private TextView textContent;
        private ImageView imgFold;

        public Help_ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.textView_Question);
            textContent = itemView.findViewById(R.id.textView_Content);
            imgFold = itemView.findViewById(R.id.imageView_Fold);
        }

        void onBind(HelpData data) {
            textQuestion.setText(data.getQuesiton());
            textContent.setText(data.getContent());
            imgFold.setImageResource(data.getResID());
        }
    }
}

