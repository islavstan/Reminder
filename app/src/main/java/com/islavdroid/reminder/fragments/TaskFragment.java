package com.islavdroid.reminder.fragments;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.islavdroid.reminder.MainActivity;
import com.islavdroid.reminder.R;
import com.islavdroid.reminder.adapters.TaskAdapter;

import com.islavdroid.reminder.model.Item;
import com.islavdroid.reminder.model.ModelTask;

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

    //пишем реализацию вызова диалога для удаления таска;   location у нас - viewHolder.getLayoutPosition()
    public void removeTaskDialog(final int location){
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(R.string.delete_dialog);
        Item item =adapter.getItem(location);
        if(item.isTask()){
            ModelTask removingTask = (ModelTask)item;
            final long timeStamp = removingTask.getTimeStamp();
            final boolean[] isRemoved = {false};
            alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(location);
                    isRemoved[0] =true;
                    //ставим отмену удаления на снекбар
                    Snackbar snackbar =Snackbar.make(getActivity().findViewById(R.id.activity_main),R.string.removed,Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          addTask(activity.dbHelper.query().getTask(timeStamp),false);
                            isRemoved[0]=false;
                        }
                    });
                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            //когда появляется на экране
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            //когда исчезает
                            if(isRemoved[0]){
                                //полностью удаляем из базы
                                activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });
                    snackbar.show();
                   dialog.dismiss();
                }
            });
            alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();


                }
            });
        }
        alertDialog.show();

    }


    public abstract void moveTask(ModelTask task);

    public abstract void findTasks(String title);

    public abstract void addTaskFromDB();

}


