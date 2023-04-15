package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import android.view.View;

import androidx.annotation.NonNull;

public final class interfaces {
    public interface FilterMethod<ObjectType,Query>{
        public boolean shouldFilter(ObjectType item,Query query);
    }
    public interface OnBindViewHolderListener<Item,ViewHolder>
    {
        public void onBindViewHolder(@NonNull Item item,@NonNull ViewHolder holder, int position);
    }

    public interface OnItemClickListener<T>{
        public void onClick(T item, View v);
    }

}
