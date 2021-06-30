package com.example.launcherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AppDrawer extends AppCompatActivity {


    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);

        recyclerView = findViewById(R.id.appsList);

        // add this
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppAdapter adapter = new AppAdapter(this);

        // replace this line by this two. (you should not call adapter method directly).
        // adapter.onCreateViewHolder(recyclerView, 0);        // LINE 23
        recyclerView.setAdapter(adapter);
        // notify the adapter that the data has changed
        //recyclerView.notifyDataSetChanged();
    }
}