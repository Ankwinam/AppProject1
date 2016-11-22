package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by axx42 on 2016-11-21.
 */

public class DetailActivity extends AppCompatActivity {
    TextView main_name;
    RatingBar level;
    ImageView imageView;
    TextView area;
    TextView info;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_scrolling);

        main_name = (TextView) findViewById(R.id.detail_name);
        area = (TextView) findViewById(R.id.detail_area);
        level = (RatingBar) findViewById(R.id.detail_level);
        info = (TextView) findViewById(R.id.detail_Info);
        imageView = (ImageView) findViewById(R.id.detail_imageView);

        Intent intent = getIntent();
        String walk_name = intent.getStringExtra("walk_name");
        String walk_level = intent.getStringExtra("walk_level");
        String walk_area = intent.getStringExtra("walk_area");

        byte[] arr = getIntent().getByteArrayExtra("walk_image");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        main_name.setText(walk_name);
        imageView.setImageBitmap(image);
        area.setText(walk_area);
        Log.e("Check",walk_level);
        level.setNumStars(Integer.parseInt(walk_level));
    }
}
