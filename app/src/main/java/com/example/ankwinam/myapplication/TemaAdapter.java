package com.example.ankwinam.myapplication;

/**
 * Created by axx42 on 2016-09-09.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TemaAdapter extends BaseAdapter {
    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Tema> data = null;
    private LayoutInflater inflater = null;

    public TemaAdapter(Context c, int l, ArrayList<Tema> d) {
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

        if(convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        ImageView Hu_image = (ImageView) convertView.findViewById(R.id.Local_image);
        TextView Hu_name = (TextView) convertView.findViewById(R.id.Local_name);
        TextView Hu_gender = (TextView) convertView.findViewById(R.id.Local_gender);
        TextView Hu_age = (TextView) convertView.findViewById(R.id.Local_age);

        Hu_image.setImageBitmap(data.get(position).image);
        Hu_name.setText(data.get(position).walk_name);
        Hu_gender.setText(data.get(position).area);
        Hu_age.setText(data.get(position).level);

        return convertView;
    }
}
