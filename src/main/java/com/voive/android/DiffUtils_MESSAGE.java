package com.voive.android;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffUtils_MESSAGE extends DiffUtil.Callback {


    List<Messages> first;
    List<Messages> second;

    public DiffUtils_MESSAGE(List<Messages> first, List<Messages> second) {
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
