package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.myapplication.Common.Views.Fragments.IModel;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * An adapter for displaying a list of items in a one-liner format.
 *
 * @param <T> the type of the items in the list
 */
public class OneLinerAdapter<T> extends Adapter<OneLineViewHolder<T>> {

    //  ArrayList for all items in the list
    final private ArrayList<T> rawItems;

    //  ArrayList ONLY for visible items! in the list ( after filtering.)
    private final ArrayList<T> visibleList;

    //reference to  RecyclerView
    private RecyclerView recyclerView;

    // listener for binding the view holder
    private interfaces.OnBindViewHolderListener<T, OneLineViewHolder<T>> bindListener = null;

    // listener for item click events.
    private interfaces.OnItemClickListener<T> onItemClickListener = null;

    /**
     * create OneLinerAdapter with empty list
     */
    public OneLinerAdapter() {
        rawItems    = new ArrayList<>();
        visibleList = new ArrayList<>();
    }

    /**
     * create  OneLinerAdapter with the given list of items.
     *
     * @param objects the list of items we want to display
     */
    public OneLinerAdapter(ArrayList<T> objects) {
        this.rawItems    = objects;
        this.visibleList = objects;
    }

    /**
     * return the visible items in the list.
     *
     * @return the list of visible items
     */
    public ArrayList<T> getItems() {
        return visibleList;
    }

    /**
     * Called when the adapter is attached to a RecyclerView instance.
     *
     * @param recyclerView the RecyclerView instance
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    /**
     * Called to create a new view holder for a list item.
     *
     * @param parent   the parent ViewGroup
     * @param viewType the view type of the new view holder
     * @return the new view holder instance
     */
    @NonNull
    @Override
    public OneLineViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                                                 parent, false);
        return new OneLineViewHolder<T>(view);
    }

    /**
     * bind data to the view holder
     *
     * @param holder   the view holder
     * @param position the position of the item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull OneLineViewHolder<T> holder, int position) {
        // Set the item for the view holder to the item at the given position.
        holder.setItem(getItems().get(position));
        // If a bind listener is set, call it to handle the binding.
        if (bindListener != null) {
            bindListener.onBindViewHolder(getItems().get(position), holder, position);
        } else {
            // Otherwise, if the item implements the IModel interface, call
            // its toPrettyString() method to get a string representation of the item.
            if (holder.getItem() instanceof IModel)
                holder.setText(((IModel) holder.getItem()).toPrettyString());
                // Otherwise, call the item's toString() method to get a string representation.
            else
                holder.setText(holder.getItem().toString());
        }
        // If an item click listener is set, also set it for the view holder.
        if (onItemClickListener != null)
            holder.setOnClickListener(onItemClickListener);
    }

    /**
     * @return the number of visible items
     */
    @Override
    public int getItemCount() {
        return getItems().size();
    }

    /**
     * remove item in a position
     *
     * @param position the position of the item to remove
     * @return the removed item
     */
    public T removeEntry(int position) {
        if (position >= getItems().size()) {
            throw new IndexOutOfBoundsException();
        }
        T item = getItems().remove(position);
        if (item != null) {
            rawItems.remove(item);
            notifyItemRemoved(position);
        }
        return item;
    }

    /**
     * remove item
     *
     * @param model the item to remove
     * @return the removed item
     */
    public T removeEntry(T model) {
        int     visiblePosition = visibleList.indexOf(model);
        boolean removed         = visibleList.remove(model);
        rawItems.remove(model);
        if (removed)
            notifyItemRemoved(visiblePosition);
        return removed ? model : null;
    }

    /**
     * add new item
     */
    public void addEntry(T item) {
        addEntry(item, true);
    }

    /**
     * add a new item to the list and notify the adapter of the change.
     *
     * @param item   the item we want to add/ update
     * @param update is item updated already exists in the list??
     */
    public void addEntry(T item, boolean update) {
        int itemIndex = getItems().indexOf(item);
        if (itemIndex != -1) {
            updateEntry(item, itemIndex);
        } else {
            getItems().add(item);
            rawItems.add(item);
            if (update)
                notifyItemInserted(getItems().size() - 1);
        }
    }

    /**
     * remove items from the list
     */
    public void clearList() {
        int size = this.getItems().size();
        rawItems.clear();
        this.getItems().clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * update item in the list
     *
     * @param entry    the item to update
     * @param position the position of the item in the list
     */
    public void updateEntry(T entry, int position) {
        int rawIndex     = rawItems.indexOf(entry);
        int visibleIndex = visibleList.indexOf(entry);

        if (rawIndex != position && visibleIndex != position)
            return;
        if (visibleList.get(visibleIndex).equals(entry))
            return;
        if (rawIndex == visibleIndex && visibleIndex == -1) {
            addEntry(entry);
            return;
        }
        notifyItemMoved(visibleIndex, 0);
        rawItems.set(rawIndex, entry);
        visibleList.remove(rawIndex);
        visibleList.add(0, entry);
        //notify the attached adapter  that item changed
        notifyItemChanged(0);
    }

    /**
     * -filter a list with query filter method
     * -updates visible list
     *
     * @param query   the query to filter the list with
     * @param filter  the filter method
     * @param <Query> the query type
     */
    public <Query> void setFilter(Query query, interfaces.FilterMethod<T, Query> filter) {
        int size = visibleList.size();
        visibleList.clear();


        //notify adapter  that item removed
        notifyItemRangeRemoved(0, size);
        //filter the list of presented items
        //add only items that we dont want to filter
        for (int i = 0; i < rawItems.size(); i++) {
            T item = rawItems.get(i);
            if (!filter.shouldFilter(item, query)) {
                visibleList.add(item);
            }
        }
        //notify the attached adapter  that item changed
        notifyItemRangeRemoved(0, visibleList.size());
    }

    /**
     * Set listener for binding view holder.
     *
     * @param bindViewHolderListener the listener
     */
    public void setBindViewHolderListener(interfaces.OnBindViewHolderListener<T,
            OneLineViewHolder<T>> bindViewHolderListener) {
        this.bindListener = bindViewHolderListener;
    }

    /**
     * Set listener for item click events
     *
     * @param l the listener
     */
    public void setOnItemClickListener(interfaces.OnItemClickListener<T> l) {
        this.onItemClickListener = l;
    }
}
