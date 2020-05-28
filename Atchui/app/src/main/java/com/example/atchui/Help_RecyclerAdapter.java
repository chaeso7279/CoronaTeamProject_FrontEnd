package com.example.atchui;


import android.animation.ValueAnimator;
import android.content.Context;
import android.renderscript.Sampler;
import android.util.Log;
import android.util.SparseBooleanArray;
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

    Context context;
    // 어댑터에 들어갈 데이터 리스트
    private ArrayList<HelpData> lstData = new ArrayList<>();

    // Item 클릭 상태 저장할 array
    private SparseBooleanArray selectedItem = new SparseBooleanArray();
    // 직전에 클릭 된 아이템의 position
    private int iPrePos = -1;

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
        holder.onBind(lstData.get(position), position);
    }

    @Override
    public int getItemCount() { return lstData.size(); }
    void addItem(HelpData data) { lstData.add(data); }

    class Help_ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textQuestion;
        private TextView textContent;
        private ImageButton imgButtonExpand;
        private ImageView imgViewContent;

        private HelpData data;

        private int iPos;

        Help_ItemViewHolder(View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.textView_Question);
            textContent = itemView.findViewById(R.id.textView_Content);
            imgButtonExpand = itemView.findViewById(R.id.imageBtn_Expand);
            imgViewContent = itemView.findViewById(R.id.imgView_Content);
        }

        void onBind(HelpData data, int position) {
            this.data = data;
            this.iPos = position;

            textQuestion.setText(data.getQuesiton());
            textContent.setText(data.getContent());
            imgButtonExpand.setImageResource(R.drawable.ic_logo);
            imgViewContent.setImageResource(R.drawable.img_test);

            changeVisibility(selectedItem.get(position));

            itemView.setOnClickListener(this);
            textQuestion.setOnClickListener(this);
            textContent.setOnClickListener(this);
            imgButtonExpand.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.linearItem_Help:
                    if(selectedItem.get(iPos)) {
                        // 펼쳐진 아이템 클릭
                        selectedItem.delete(iPos);
                    }
                    else {
                        // 직전에 클릭된 아이템의 상태값 지움
                        selectedItem.delete(iPrePos);
                        // 클릭한 아이템 pos값 넣어줌
                        selectedItem.put(iPos, true);
                    }

                    // 포지션이 바뀌었음을 알림
                    if(iPrePos != -1)
                        notifyItemChanged(iPrePos);
                    notifyItemChanged(iPos);

                    iPrePos = iPos;
                    break;
                case R.id.imageBtn_Expand:
                    Toast.makeText(context, "버튼 눌림", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        private void changeVisibility(final boolean isExpanded) {

            int iDPValue = 150;
            float fDensity = context.getResources().getDisplayMetrics().density;
            int iHeight = (int)(iDPValue * fDensity);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded?ValueAnimator.ofInt(0, iHeight) : ValueAnimator.ofInt(iHeight, 0);

            // Animation 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int iValue = (int) animation.getAnimatedValue();

                    // ImageView 높이 변경
                    imgViewContent.getLayoutParams().height = iValue;
                    imgViewContent.requestLayout();

                    // ImageView 사라지게 함
                    imgViewContent.setVisibility(isExpanded? View.VISIBLE : View.GONE);
                }
            });

            // Animation 시작
            va.start();
        }
    }
}

