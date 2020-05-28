package com.example.atchui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

public class HelpActivity extends AppCompatActivity {

    private Help_RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Initialize();
        setData();
    }

    private void Initialize() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_Help);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Help_RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void setData() {
        HelpData data = new HelpData();
        data.setQuestion("질문1");
        data.setContent("답변1");
        data.setResID(R.drawable.ic_logo);

        adapter.addItem(data);

        data = new HelpData();
        data.setQuestion("질문2");
        data.setContent("답변2");
        data.setResID(R.drawable.ic_logo);

        adapter.addItem(data);
        adapter.notifyDataSetChanged();
    }
}
