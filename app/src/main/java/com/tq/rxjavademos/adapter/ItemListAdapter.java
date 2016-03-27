package com.tq.rxjavademos.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tq.rxjavademos.R;
import com.tq.rxjavademos.model.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 2016/3/26 22:46
 * Created by Author boobooL
 * 邮箱：boobooMX@163.com
 */
public class ItemListAdapter extends RecyclerView.Adapter {

    List<Item>images;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new DebounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DebounceViewHolder debounceViewHolder= (DebounceViewHolder) holder;
        Item image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.imageUrl).into(debounceViewHolder.imageIv);
        debounceViewHolder.descriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {
        return images==null ?0:images.size();
    }

    public void setImages(List<Item> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    static class DebounceViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.imageIv)ImageView imageIv;
        @Bind(R.id.descriptionTv)TextView descriptionTv;
        public DebounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
