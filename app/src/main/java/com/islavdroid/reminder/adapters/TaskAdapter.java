package com.islavdroid.reminder.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.islavdroid.reminder.fragments.TaskFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.islavdroid.reminder.model.Item;

//с помощью него мы объеденим current task adapter и donetaskadapter
public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   List<Item>items;
    TaskFragment taskFragment;
    public TaskAdapter(TaskFragment taskFragment){
        this.taskFragment =taskFragment;
        items=new ArrayList<>();
    }

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

    public void deleteItem(int location){
        //проверяем есть ли элементы в списке
   if(location>=0 && location <=getItemCount() -1){
       items.remove(location);
       notifyItemRemoved(location);
   }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView title,date;
        CircleImageView priorityImage;
        public TaskViewHolder(View itemView, TextView title, TextView date,CircleImageView priorityImage) {
            super(itemView);
            this.title=title;
            this.date=date;
            this.priorityImage=priorityImage;
        }
    }

    public TaskFragment getTaskFragment() {
        return taskFragment;
    }
}
