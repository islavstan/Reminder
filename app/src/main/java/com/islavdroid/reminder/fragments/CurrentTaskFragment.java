package com.islavdroid.reminder.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islavdroid.reminder.R;
import com.islavdroid.reminder.adapters.CurrentTasksAdapter;
import com.islavdroid.reminder.adapters.TaskAdapter;
import com.islavdroid.reminder.dialogs.AddingTaskDialogFragment;

import model.ModelTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTaskFragment extends TaskFragment {


    public CurrentTaskFragment() {
        // Required empty public constructor
    }
    OnTaskDoneListener onTaskDoneListener;

    public interface OnTaskDoneListener{
        void onTaskDone(ModelTask task);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onTaskDoneListener=(OnTaskDoneListener) context;

        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString()+"must implements AddingTaskListener");
        }
    }




    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                onTaskDoneListener=(OnTaskDoneListener) activity;

            }catch (ClassCastException ex){
                throw new ClassCastException(activity.toString()+"must implements AddingTaskListener");
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CurrentTasksAdapter(this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void moveTask(ModelTask task) {
   onTaskDoneListener.onTaskDone(task);
    }
}
