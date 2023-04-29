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
/**

 represent  item in the list and sets properties:
 title, description...
 */
public class RouteAdapter extends OneLinerAdapter<NavItem> {

    @NonNull
    @Override
    public OneLineViewHolder<NavItem> onCreateViewHolder(@NonNull ViewGroup parent,
     //create a view from the layout file that represents a single navigation item in the list
                                                         int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.route_card_view,
                                                                 parent, false);
        return new RouteHolder(view);
    }
    //bind data to the view holder
    @Override
    public void onBindViewHolder(@NonNull OneLineViewHolder<NavItem> holder, int position) {
        super.onBindViewHolder(holder, position);
        //adjust the layout of the items to create grid layout
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(position % 3 == 2); //adjusts layout of items RecyclerView grid (by sapn some items to full width)
        //just to make it look better


    }
}
