package com.example.ankwinam.myapplication;

/**
 * Created by axx42 on 2016-09-09.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class TemaResultActivity extends Activity{
    Bitmap image;
    ImageView BigImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tema_result);
        BigImage = (ImageView)findViewById(R.id.BigImage);

        Intent intent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("image");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        BigImage.setImageBitmap(image);
    }
}
