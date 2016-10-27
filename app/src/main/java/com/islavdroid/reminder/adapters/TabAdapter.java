package com.islavdroid.reminder.adapters;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.islavdroid.reminder.fragments.CurrentTaskFragment;
import com.islavdroid.reminder.fragments.DoneTaskFragment;

public class TabAdapter  extends FragmentStatePagerAdapter {
   private int numOfTabs;




    public TabAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CurrentTaskFragment();

            case 1:
                return new DoneTaskFragment();

            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }



}


