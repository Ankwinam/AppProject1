package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {
    TextView main_name;
    RatingBar level;
    ImageView image;
    TextView info;

    Button detail_tracking_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        main_name = (TextView) findViewById(R.id.road_name);
        level = (RatingBar) findViewById(R.id.detail_level);
        info = (TextView)findViewById (R.id.detail_Info);

        detail_tracking_btn = (Button)findViewById(R.id.detail_tracking_btn);
        detail_tracking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_go = new Intent(ScrollingActivity.this, MapActivity.class);
                startActivity(map_go);
            }
        });

//        Intent intent = getIntent();
//        byte[] arr = getIntent().getByteArrayExtra("image");
//        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
//        BigImage.setImageBitmap(image);

    }
}
