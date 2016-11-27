package com.example.ankwinam.myapplication;

/**
 * Created by axx42 on 2016-11-27.
 */

public class Communiry_item {
    int image;
    String title;

    int getImage(){
        return this.image;
    }
    String getTitle(){
        return this.title;
    }

    Communiry_item(int image, String title){
        this.image=image;
        this.title=title;
    }
}
