package com.voive.android;

import androidx.recyclerview.widget.DiffUtil;

import com.parse.ParseObject;

import java.util.List;

public class DiffUtils_Subscriptions extends DiffUtil.Callback {


    List<ParseObject> first;
    List<ParseObject> second;

    public DiffUtils_Subscriptions(List<ParseObject> first, List<ParseObject> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int getOldListSize() {
        return first.size();
    }

    @Override
    public int getNewListSize() {
        return second.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition==newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return first.get(oldItemPosition) == second.get(newItemPosition);
    }
}
