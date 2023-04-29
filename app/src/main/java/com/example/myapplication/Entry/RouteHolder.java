package com.example.myapplication.Entry;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.interfaces;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

/**
 * define view holder for the nevigation item
 */
public class RouteHolder extends OneLineViewHolder<NavItem> {

    private TextView              textView;
    private TextView              textView1;
    private CardView              card;
    private NavItem item;

    //we use it later
    int[] colors = {
            Color.parseColor("#4DD0E1"),// cyan 300
            Color.parseColor("#4FC3F7"),// light blue 300
            Color.parseColor("#4DB6AC"),// teal 300
            Color.parseColor("#5C6BC0"),// Indigo 300
            Color.parseColor("#BA68C8"),// Purple 300

    };


    public RouteHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.date);
        textView1 = itemView.findViewById(R.id.date2);
        card     = itemView.findViewById(R.id.card);
        // set the background color in random from the colors array  ((int[] colors))

        card.setCardBackgroundColor(colors[(int) Math.floor(Math.random() * colors.length)]);
    }



    public void setText(String text) {
        textView.setText(text);
    }
    public void setTitle(String text) {
        setText(text);
    }
    public void setDescription(String text) {
        textView1.setText(text);
    }

    public void hide(){
        card.setVisibility(View.GONE);
    }
    public void show(){
        card.setVisibility(View.VISIBLE);
    }


}
