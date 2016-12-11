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
 * Created by axx42 on 2016-09-09.
 */
public class Walk_Info {
    public String walk_name;
    public String area;
    public String level;
    public Bitmap image;
    public String imgUrl;

    String walk;
    String bicycle;
    String pet;
    String baby;

    public WalkListAdapter wat;
    private ImageLoadTask loadTask;

    Walk_Info(String walk_name, String area, String level, String imgUrl,String walk,String bicycle,String pet,String baby){
        this.imgUrl = imgUrl;
        this.walk_name =walk_name;
        this.area = area;
        this.level = level;

        this.walk =walk;
        this.bicycle = bicycle;
        this.pet = pet;
        this.baby = baby;

        this.image = null;

        loadTask = new ImageLoadTask();
    }

    public void loadImage(WalkListAdapter wat) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.wat = wat;
        if (imgUrl != null && !imgUrl.equals("")) {
            loadTask.execute(imgUrl);
        }
    }

    public void cancel(){
        Log.e("Asyn","Cancel");
        loadTask.cancel(true);
    }

    public Bitmap getImage() {
        return image;
    }

    // ASYNC TASK TO AVOID CHOKING UP UI THREAD
    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            Log.e("ImageLoadTask", "Loading image...");
        }

        // PARAM[0] IS IMG URL
        @Override
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = Walk_Info.getBitmapFromURL(param[0]);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.e("ImageLoadTask", "Successfully loaded " + imgUrl + " image");
                image = ret;
                if (wat != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                    wat.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + imgUrl + " image");
            }
        }
    }




    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Bitmap resized = Bitmap.createScaledBitmap(myBitmap,300,250,true);
            return resized;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
