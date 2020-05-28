package com.example.atchui;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.view.ViewDebug.*;


public class Help_RecyclerAdapter extends RecyclerView.Adapter<Help_RecyclerAdapter.Help_ItemViewHolder>  {

    // 어댑터에 들어갈 데이터 리스트
    private ArrayList<HelpData> lstData = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public Help_ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_help, parent, false);
        context = parent.getContext();
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

    class Help_ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textQuestion;
        private TextView textContent;
        private ImageButton imgButtonFold;
        private HelpData data;


        Help_ItemViewHolder(View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.textView_Question);
            textContent = itemView.findViewById(R.id.textView_Content);
            imgButtonFold = itemView.findViewById(R.id.imageBtn_Fold);
        }

        void onBind(HelpData data) {
            this.data = data;

            textQuestion.setText(data.getQuesiton());
            textContent.setText(data.getContent());
            imgButtonFold.setImageResource(data.getResID());

            itemView.setOnClickListener(this);
            textQuestion.setOnClickListener(this);
            textContent.setOnClickListener(this);
            imgButtonFold.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            switch(v.getId()) {
                case R.id.linearItem_Help:
                    Toast.makeText(context, "아이템 눌림" + data.getQuesiton(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.imageBtn_Fold:
                    Toast.makeText(context, "버튼 눌림", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
}

