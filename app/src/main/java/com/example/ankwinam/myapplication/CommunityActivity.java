package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axx42 on 2016-11-27.
 */

public class CommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_main);

        //글쓰기 버튼 이벤트
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent write = new Intent(CommunityActivity.this, CommunityWriteActivity.class);
                startActivity(write);
            }
        });

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Communiry_item> items=new ArrayList<>();
        Communiry_item[] item=new Communiry_item[5];
        item[0]=new Communiry_item(R.drawable.ic_menu_camera,"#1");
        item[1]=new Communiry_item(R.drawable.ic_menu_camera,"#2");
        item[2]=new Communiry_item(R.drawable.ic_menu_camera,"#3");
        item[3]=new Communiry_item(R.drawable.ic_menu_camera,"#4");
        item[4]=new Communiry_item(R.drawable.ic_menu_camera,"#5");

        for(int i=0;i<5;i++) items.add(item[i]);

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),items,R.layout.activity_main));
    }

}
