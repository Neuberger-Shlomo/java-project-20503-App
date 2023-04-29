package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

/**
 * view holder for a one line item.
 * @param <Item> the type of the item in the view holder
 */
public class OneLineViewHolder<Item> extends RecyclerView.ViewHolder {
    private final TextView textView;

    private final CardView cardView;

    private final View superView;

    private Item item;

    /**
     * Creates a new OneLineViewHolder instance with the given view.
     *
     * @param view the view to display in the view holder
     */
    public OneLineViewHolder(View view) {
        super(view);
        superView = view;
        textView = (TextView) view.findViewById(R.id.line1);
        cardView = (CardView) view.findViewById(R.id.cardView0);
    }

    /**
     * @return the item in the view holder
     */
    public Item getItem() {
        return item;
    }

    /**
     * save the current item in the view holder.
     * @param item the item to save
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * set the text for TextView in holder.
     *
     * @param text
     */
    public void setText(String text) {
        textView.setText(text);
    }

    /**
     * Sets an item click listener for the view holder.
     * @param l the listener
     */
    public void setOnClickListener(interfaces.OnItemClickListener<Item> l) {
        superView.setOnClickListener(v -> l.onClick(item, v));
    }
}
