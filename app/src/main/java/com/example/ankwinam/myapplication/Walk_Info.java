package com.example.ankwinam.myapplication;

import android.graphics.Bitmap;

/**
 * Created by axx42 on 2016-09-09.
 */
public class Walk_Info {
    public String walk_name;
    public String area;
    public String level;
    public Bitmap image;

    Walk_Info(String walk_name, String area, String level, Bitmap image){
        this.image = image;
        this.walk_name =walk_name;
        this.area = area;
        this.level = level;
    }
}
