package com.tq.rxjavademos.adapter;

import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tq.rxjavademos.R;
import com.tq.rxjavademos.model.ZhuangbiImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boobooL on 2016/3/25 0025
 * Created 邮箱 ：boobooMX@163.com
 */
public class ZhuangbiListAdapter extends RecyclerView.Adapter {
    List<ZhuangbiImage> images;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new DebounceViewHolor(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DebounceViewHolor debounceViewHolor= (DebounceViewHolor) holder;
        ZhuangbiImage image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.image_url).into(debounceViewHolor.imageIv);
        debounceViewHolor.descriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {

        return images==null?0:images.size();
    }

    public void setImages(List<ZhuangbiImage> images) {
        this.images = images;
         notifyDataSetChanged();
    }

    static class DebounceViewHolor extends  RecyclerView.ViewHolder{
        @Bind(R.id.imageIv)
        ImageView imageIv;
        @Bind(R.id.descriptionTv)
        TextView descriptionTv;

        public DebounceViewHolor(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
