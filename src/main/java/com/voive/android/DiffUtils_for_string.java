package com.voive.android;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffUtils_for_string extends DiffUtil.Callback {
    List<String> FIRST;
    List<String> SECOND;

    public DiffUtils_for_string(List<String> FIRST, List<String> SECOND) {
        this.FIRST = FIRST;
        this.SECOND = SECOND;
    }

    @Override
    public int getOldListSize() {
        return FIRST.size();
    }

    @Override
    public int getNewListSize() {
        return SECOND.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition==newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return FIRST.get(oldItemPosition) == SECOND.get(newItemPosition);
    }
}
