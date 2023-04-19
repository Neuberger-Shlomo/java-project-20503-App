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

public class OneLinerAdapter<T> extends Adapter<OneLineViewHolder<T>> {

    final private ArrayList<T> rawItems;

    private final ArrayList<T> visibleList;

    private RecyclerView recyclerView;

    private interfaces.OnBindViewHolderListener<T, OneLineViewHolder<T>> bindListener = null;

    public OneLinerAdapter() {
        rawItems    = new ArrayList<>();
        visibleList = new ArrayList<>();
    }

    public OneLinerAdapter(ArrayList<T> objects) {
        this.rawItems    = objects;
        this.visibleList = objects;
    }

    public ArrayList<T> getItems() {
        return visibleList;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public OneLineViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                                                 parent, false);
        return new OneLineViewHolder<T>(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OneLineViewHolder<T> holder, int position) {
        holder.setItem(getItems().get(position));
        if (bindListener != null) {
            bindListener.onBindViewHolder(getItems().get(position), holder, position);
        } else {
            if (holder.getItem() instanceof IModel)
                holder.setText(((IModel) holder.getItem()).toPrettyString());
            else
                holder.setText(holder.getItem().toString());
        }
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

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


    public T removeEntry(T model) {
        int     visiblePosition = visibleList.indexOf(model);
        boolean removed         = visibleList.remove(model);
        rawItems.remove(model);
        if (removed)
            notifyItemRemoved(visiblePosition);
        return removed ? model : null;
    }

    public void addEntry(T item) {
        addEntry(item, true);
    }

    public void addEntry(T item, boolean update) {
        int itemIndex = getItems().indexOf(item);
        if (itemIndex != -1) {
            updateEntry(item, itemIndex);
        }else {
            getItems().add(item);
            rawItems.add(item);
            if (update)
                notifyItemInserted(getItems().size() - 1);
        }
    }

    public void clearList() {
        int size = this.getItems().size();
        rawItems.clear();
        this.getItems().clear();
        notifyItemRangeRemoved(0, size);
    }

    public void updateEntry(T entry, int position) {
        int rawIndex     = rawItems.indexOf(entry);
        int visibleIndex = visibleList.indexOf(entry);

        if (rawIndex != position && visibleIndex != position)
            return;
        if(visibleList.get(visibleIndex).equals(entry)) return;
        if (rawIndex == visibleIndex && visibleIndex == -1) {
            addEntry(entry);
            return;
        }
        notifyItemMoved(visibleIndex, 0);
        rawItems.set(rawIndex, entry);
        visibleList.remove(rawIndex);
        visibleList.add(0, entry);
        notifyItemChanged(0);
    }

    public <Query> void setFilter(Query query, interfaces.FilterMethod<T, Query> filter) {
        int size = visibleList.size();
        visibleList.clear();
        notifyItemRangeRemoved(0, size);
        for (int i = 0; i < rawItems.size(); i++) {
            T item = rawItems.get(i);
            if (!filter.shouldFilter(item, query)) {
                visibleList.add(item);
            }
        }
        notifyItemRangeRemoved(0, visibleList.size());


    }

    public void setBindViewHolderListener(interfaces.OnBindViewHolderListener<T,
            OneLineViewHolder<T>> bindViewHolderListener) {
        this.bindListener = bindViewHolderListener;
    }


}
