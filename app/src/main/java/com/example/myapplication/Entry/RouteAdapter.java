package com.example.myapplication.Entry;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.interfaces;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class RouteAdapter extends OneLinerAdapter<NavItem> {

    @NonNull
    @Override
    public OneLineViewHolder<NavItem> onCreateViewHolder(@NonNull ViewGroup parent,
                                                                       int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.route_card_view,
                                                                 parent, false);
        return new RouteHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OneLineViewHolder<NavItem> holder, int position) {
        super.onBindViewHolder(holder, position);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(position % 3 == 2);


    }
}
