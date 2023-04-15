package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class OneLineViewHolder<Item> extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final CardView cardView;

    private Item item;

    public OneLineViewHolder(View view) {
        super(view);
        // Define click listener for the ViewHolder's View

        textView = (TextView) view.findViewById(R.id.line1);
        cardView = (CardView) view.findViewById(R.id.cardView0);

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;

    }
    public void setText(String text){
        textView.setText(text);
    }

    public void setOnClickListener(interfaces.OnItemClickListener<Item> l){
        cardView.setOnClickListener(v -> l.onClick(item, v));
    }

}
