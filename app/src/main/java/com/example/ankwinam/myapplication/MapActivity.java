package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by axx42 on 2016-09-04.
 */
public class MapActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.seoul_map);

        //임시 버튼 -->지도로 대체해야함
        Button button = (Button)findViewById(R.id.button_map);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent board = new Intent(MapActivity.this, Local_NaviActivity.class);
                startActivity(board);
                finish();
            }
        });
    }
}
