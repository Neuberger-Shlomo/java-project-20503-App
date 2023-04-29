package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * interface that is used in one liner view holder. and one liner adapter.
 * for operations filtering, binding, and click events.
 */
public final class interfaces {

    /**
     * Interface for defining a filtering method.
     *
     * @param <ObjectType>  the object to filter
     * @param <Query>       the query for filtering.
     */
    public interface FilterMethod<ObjectType, Query> {
        /**
         * return if want to filter item or not
         *
         * @param item  the object we checke
         * @param query
         * @return True if we want to filter
         */
        public boolean shouldFilter(ObjectType item, Query query);
    }

    /**
     * define the binding action of ViewHolder.
     * @param <Item>      the type of the item we want to bind
     * @param <ViewHolder> the type of the ViewHolder
     */
    public interface OnBindViewHolderListener<Item, ViewHolder> {
        /**
         * Bind item to ViewHolder
         *
         * @param item     the item we want to bind
         * @param holder   the ViewHolder
         * @param position the position of the item
         */
        public void onBindViewHolder(@NonNull Item item, @NonNull ViewHolder holder, int position);
    }

    /**
     * define click action in ViewHolder.
     * @param <T> the type of item we click
     */
    public interface OnItemClickListener<T> {
        /**
         * Handle event: click on item
         *
         * @param item The item that we click on
         * @param v    The view where we click
         */
        public void onClick(T item, View v);
    }
}
