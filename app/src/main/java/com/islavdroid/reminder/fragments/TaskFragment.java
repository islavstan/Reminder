package com.islavdroid.reminder.fragments;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.islavdroid.reminder.MainActivity;
import com.islavdroid.reminder.adapters.CurrentTasksAdapter;
import com.islavdroid.reminder.adapters.TaskAdapter;

import model.ModelTask;

public abstract class TaskFragment extends Fragment {
    //класс объединяющий DoneTaskFragment и CurrentTaskFragment
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected TaskAdapter adapter;
    public MainActivity activity;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity()!=null){
            activity=(MainActivity)getActivity();
        }

        //вызываем здесь, чтобы не вызывать отдельно в каждом фрагменте
        addTaskFromDB();
    }

    public void addTask(ModelTask newTask,boolean saveToDB){
        int position =-1;
        //чтобы элементы сортировались по дате
        for(int i=0;i<adapter.getItemCount();i++){
            if(adapter.getItem(i).isTask()){
                ModelTask task = (ModelTask)adapter.getItem(i);
                if(newTask.getDate()<task.getDate()){
                    position=i;
                    break;
                }
            }
        }

        if(position!=-1){
            adapter.addItem(position,newTask);

        }else{
            adapter.addItem(newTask);
        }
        if(saveToDB){
            activity.dbHelper.saveTask(newTask);
        }
    }

    public abstract void moveTask(ModelTask task);

    public abstract void addTaskFromDB();

}


