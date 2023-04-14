package com.example.myapplication.Common.Views.ViewHolder.OneLiner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.myapplication.R;

import java.util.ArrayList;

public class OneLinerAdapter<T> extends Adapter<OneLineViewHolder<T>> {

    final private ArrayList<T> rawItems;

    private ArrayList<T> visibleList;

    private RecyclerView recyclerView;

    private interfaces.onBindViewHolderListener<T, OneLineViewHolder<T>> bindListener = null;

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
        if (bindListener != null) {
            bindListener.onBindViewHolder(getItems().get(position), holder, position);
        } else {
            holder.setText(getItems().get(position).toString());
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

    public void addEntry(T item){
        addEntry(item,true);
    }

    public void addEntry(T item,boolean update) {
        getItems().add(item);
        rawItems.add(item);
        if(update) notifyItemInserted(getItems().size() - 1);
    }


    public void clearList() {
        int size = this.getItems().size();
        rawItems.clear();
        this.getItems().clear();
        notifyItemRangeRemoved(0, size);
    }

    public void updateEntry(T entry, int position) {
        if (position < getItems().size()) {
            T sourceItem = getItems().get(position);
            getItems().set(position, entry);
            for (int i = 0; i < rawItems.size(); i++) {
                T item = rawItems.get(i);
                if (item.equals(sourceItem)) {
                    rawItems.set(i, entry);
                    i = rawItems.size();
                }
            }
            notifyItemChanged(position);
        } else {
            addEntry(entry);
        }
    }

    public <Query> void setFilter(Query query, interfaces.filterMethod<T, Query> filter) {
        int size = visibleList.size();
        visibleList.clear();
        notifyItemRangeRemoved(0, size);
        for (int i = 0; i < rawItems.size(); i++) {
            T item = rawItems.get(i);
            if (!filter.shouldFilter(item, query)) {
                visibleList.add(item);
            }
        }
        notifyItemRangeRemoved(0,visibleList.size());


    }

    public void setBindViewHolderListener(interfaces.onBindViewHolderListener<T,
            OneLineViewHolder<T>> bindViewHolderListener) {
        this.bindListener = bindViewHolderListener;
    }
}
