package com.example.ankwinam.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by axx42 on 2016-12-07.
 */

public class Comment_item {
    public String date;
    public String id;
    public String contents;

    Comment_item(String date, String id, String contents){
        this.date= date;
        this.id = id;
        this.contents = contents;
    }
}
