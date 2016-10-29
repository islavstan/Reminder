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


public static final int CURRENT_TASK_FRAGMENT_POSITION =0;
public static final int DONE_TASK_FRAGMENT_POSITION =1;
    //чтобы объекты не создавались каждый раз при вызове getItem нужно инициализировать объекты
    private CurrentTaskFragment currentTaskFragment;
    private DoneTaskFragment doneTaskFragment;


    public TabAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        currentTaskFragment = new CurrentTaskFragment();
        doneTaskFragment = new DoneTaskFragment();


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return  currentTaskFragment;

            case 1:
                return  doneTaskFragment;

            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }



}


