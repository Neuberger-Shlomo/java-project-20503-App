package com.example.myapplication.Entry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.R;

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
        StaggeredGridLayoutManager.LayoutParams layoutParams =
                (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(position % 3 == 2);


    }
}
