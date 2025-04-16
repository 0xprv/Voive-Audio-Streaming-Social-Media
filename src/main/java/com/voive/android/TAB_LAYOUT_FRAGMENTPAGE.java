package com.voive.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class TAB_LAYOUT_FRAGMENTPAGE extends FragmentStateAdapter {


    String table_id;


    public TAB_LAYOUT_FRAGMENTPAGE(FragmentActivity fragmentManager, String table_id) {
        super(fragmentManager);
        this.table_id = table_id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new categories_in_table(table_id);
            case 1:
                return new categories_speaker(table_id);

        }
        return new categories_in_table(table_id);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
