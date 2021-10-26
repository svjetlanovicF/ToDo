package com.link.todo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ToDoPagerAdapter extends FragmentPagerAdapter {

     static final int NUM_ITEMS = 2;
     static final String[] TAB_TITLES = new String[]{"Activate", "Complited"};

    public ToDoPagerAdapter( FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();
        ItemsPreviewFragment itemsPreviewFragment = new ItemsPreviewFragment();

        switch (position){
            case 0:
                args.putString("mode", "active");
                itemsPreviewFragment.setArguments(args);
                return itemsPreviewFragment;
            case 1:
                args.putString("mode", "complited");
                itemsPreviewFragment.setArguments(args);
                return itemsPreviewFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
