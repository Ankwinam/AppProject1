package com.example.ankwinam.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by axx42 on 2016-12-07.
 */

public class CommentAdapter extends BaseAdapter{
    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Comment_item> data = null;
    private LayoutInflater inflater = null;

    public CommentAdapter(Context c, int l, ArrayList<Comment_item> d) {
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

        TextView id = (TextView) convertView.findViewById(R.id.comment_info_id);
        TextView contents = (TextView) convertView.findViewById(R.id.comment_info_content);

        id.setText(data.get(position).id);
        contents.setText( data.get(position).contents);

        return convertView;
    }
}
