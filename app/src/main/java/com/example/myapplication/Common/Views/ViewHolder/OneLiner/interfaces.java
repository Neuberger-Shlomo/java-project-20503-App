package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import android.view.View;

import androidx.annotation.NonNull;

public final class interfaces {

    public interface FilterMethod<ObjectType, Query> {
        boolean shouldFilter(ObjectType item, Query query);
    }

    public interface OnBindViewHolderListener<Item, ViewHolder> {
        void onBindViewHolder(@NonNull Item item, @NonNull ViewHolder holder, int position);
    }

    public interface OnItemClickListener<T> {
        void onClick(T item, View v);
    }
}
