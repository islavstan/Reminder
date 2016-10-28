package com.islavdroid.reminder.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.islavdroid.reminder.R;
import com.islavdroid.reminder.Utils;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import model.ModelTask;


public class CurrentTasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Item> items = new ArrayList<>();

    private static final int TYPE_TASK=0;
    private static final int TYPE_SEPARATOR =1;
    public Item getItem(int position){
        return items.get(position);
    }
    public void addItem(Item item){
        //добавляем элемент но без анимации
        items.add(item);
        notifyItemInserted(getItemCount()-1);
    }

    public void addItem(int location,Item item){
        //добавление в определённое место в списке
        items.add(location, item);
        notifyItemInserted(location);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TASK:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_task, parent, false);
                TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
                TextView date = (TextView) v.findViewById(R.id.tvTaskDate);
                return new TaskViewHolder(v, title, date);
            default:
                return null;
        /*    case TYPE_SEPARATOR:
                break;
            default:
                return null;*/
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      Item item =items.get(position);
        if(item.isTask()){
            holder.itemView.setEnabled(true);
            ModelTask task =(ModelTask)item;
            TaskViewHolder viewHolder =(TaskViewHolder)holder;
            viewHolder.title.setText(task.getTitle());
            if(task.getDate()!=0){
                viewHolder.date.setText(Utils.getFullDate(task.getDate()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {
        //в зависимости от позиции возвращает таск или сепаратор
     if(getItem(position).isTask()){
         return TYPE_TASK;
     }else return TYPE_SEPARATOR;
    }

    private class TaskViewHolder extends RecyclerView.ViewHolder{
         TextView title,date;
        public TaskViewHolder(View itemView,TextView title,TextView date) {
            super(itemView);
            this.title=title;
            this.date=date;
        }
    }
}
