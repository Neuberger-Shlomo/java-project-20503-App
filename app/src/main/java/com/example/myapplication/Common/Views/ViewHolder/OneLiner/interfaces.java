package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import androidx.annotation.NonNull;

public final class interfaces {
    public interface filterMethod<ObjectType,Query>{
        public boolean shouldFilter(ObjectType item,Query query);
    }
    public interface onBindViewHolderListener<Item,ViewHolder>
    {
        public void onBindViewHolder(@NonNull Item item,@NonNull ViewHolder holder, int position);
    }

}
