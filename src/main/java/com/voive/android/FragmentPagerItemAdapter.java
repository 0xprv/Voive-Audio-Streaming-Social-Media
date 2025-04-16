package com.voive.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class FragmentPagerItemAdapter extends FragmentPagerAdapter {

    ArrayList<String> titles=new ArrayList<>();
    List<Fragment> fragments=new ArrayList<>();
    public FragmentPagerItemAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }

    public void addfragment(Fragment fragment, String title){

        titles.add(title);

        fragments.add(fragment);

    }
}
