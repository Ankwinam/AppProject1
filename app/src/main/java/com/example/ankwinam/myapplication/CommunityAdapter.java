package com.example.ankwinam.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by axx42 on 2016-11-27.
 */
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    Context context;
    List<Community_item> items;
    int item_layout;
    public CommunityAdapter(Context context, List<Community_item> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Community_item item=items.get(position);
//        Drawable drawable=context.getResources().getDrawable(item.getImage());
//        holder.image.setBackground(drawable);
        if(item.getImage() != null){
            holder.image.setImageBitmap(item.getImage());
        }else {
            holder.image.setImageResource(R.drawable.ic_menu_gallery);
        }
        holder.title.setText(item.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,CommentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("walk_name",item.walk_name);
                i.putExtra("date",item.date);
                i.putExtra("content",item.content);
                i.putExtra("id",item.id);
                context.startActivity(i);
            }
        });
        holder.comment_num.setText(item.comment_num+"");
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;
        TextView comment_num;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.community_img);
            title=(TextView)itemView.findViewById(R.id.community_title);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
            comment_num = (TextView) itemView.findViewById(R.id.comment_little_number);
        }
    }
}
