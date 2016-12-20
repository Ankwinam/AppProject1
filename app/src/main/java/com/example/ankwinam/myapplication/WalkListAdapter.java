package com.example.ankwinam.myapplication;

/**
 * Created by axx42 on 2016-09-09.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WalkListAdapter extends BaseAdapter {
    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Walk_Info> data = null;
    private LayoutInflater inflater = null;

    public WalkListAdapter(Context c, int l, ArrayList<Walk_Info> d) {
        this.mContext = c;
        this.layout = l;
        this.data = d;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        RecyclerView.ViewHolder h;

        if(convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        ImageView Hu_image = (ImageView) convertView.findViewById(R.id.Walk_image);
        TextView Hu_name = (TextView) convertView.findViewById(R.id.Walk_name);
        TextView Hu_gender = (TextView) convertView.findViewById(R.id.Walk_gu);
        TextView Hu_age = (TextView) convertView.findViewById(R.id.Walk_level);
        TextView num_walk = (TextView) convertView.findViewById(R.id.little_walk_text);
        TextView num_by = (TextView) convertView.findViewById(R.id.little_bicycle_text);
        TextView num_pet = (TextView) convertView.findViewById(R.id.little_dog_text);
        TextView num_baby = (TextView) convertView.findViewById(R.id.little_baby_text);

        if(data.get(position).getImage() != null){
            Hu_image.setImageBitmap(data.get(position).image);
        }else{
            Hu_image.setImageResource(R.drawable.ic_menu_gallery);
        }
        Hu_name.setText(data.get(position).walk_name);
        Hu_gender.setText(data.get(position).area);
        Hu_age.setText("코스레벨 "+ data.get(position).level);
        num_baby.setText(data.get(position).baby);
        num_by.setText(data.get(position).bicycle);
        num_pet.setText(data.get(position).pet);
        num_walk.setText(data.get(position).walk);

        return convertView;
    }
}
