package com.voive.android;

import androidx.recyclerview.widget.DiffUtil;

import com.parse.ParseUser;

import java.util.List;

public class DiffUtils extends DiffUtil.Callback {


    List<ParseUser> first;
    List<ParseUser> second;

    public DiffUtils(List<ParseUser> first, List<ParseUser> second) {
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
