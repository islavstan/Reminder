package com.islavdroid.reminder.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.islavdroid.reminder.R;
import com.islavdroid.reminder.Utils;
import com.islavdroid.reminder.fragments.CurrentTaskFragment;
import com.islavdroid.reminder.fragments.TaskFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Item;
import model.ModelTask;


public class CurrentTasksAdapter extends TaskAdapter {



    private static final int TYPE_TASK=0;
    private static final int TYPE_SEPARATOR =1;

    public CurrentTasksAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TASK:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_task, parent, false);
                TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
                TextView date = (TextView) v.findViewById(R.id.tvTaskDate);
                CircleImageView imagePriority=(CircleImageView)v.findViewById(R.id.cvTaskProirity);
                return new TaskViewHolder(v, title, date,imagePriority);
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
            final ModelTask task =(ModelTask)item;
            final TaskViewHolder viewHolder =(TaskViewHolder)holder;

            final View itemView = viewHolder.itemView;
            final Resources resources = itemView.getResources();

            viewHolder.title.setText(task.getTitle());
            if(task.getDate()!=0){
                viewHolder.date.setText(Utils.getFullDate(task.getDate()));
            }else{
                viewHolder.date.setText(null);
            }
            itemView.setVisibility(View.VISIBLE);
            itemView.setBackgroundColor(resources.getColor(R.color.gray_50));
            viewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
            viewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
            viewHolder.priorityImage.setColorFilter(resources.getColor(task.getPriorityColor()));
            viewHolder.priorityImage.setImageResource(R.drawable.ic_checkbox_blank_circle);
            //подключаем слушателя который по клику на картинке меняет статус задачи
            viewHolder.priorityImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.setStatus(ModelTask.STATUS_DONE);

                    itemView.setBackgroundColor(resources.getColor(R.color.gray_200));
                    viewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
                    viewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
                    viewHolder.priorityImage.setColorFilter(resources.getColor(task.getPriorityColor()));
                    viewHolder.priorityImage.setImageResource(R.drawable.ic_checkbox_blank_circle);
                     //анимация поворота картинки по вертикальной оси
                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(viewHolder.priorityImage,"rotationY",-180f,0f);
                    flipIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                         if(task.getStatus()==ModelTask.STATUS_DONE){
                             viewHolder.priorityImage.setImageResource(R.drawable.ic_check_circle_white_24px);
                             //делаем ещё одну анимацию которая убирает объект из поля зрения
                             ObjectAnimator transactionX = ObjectAnimator.ofFloat(itemView,"translationX",0f,itemView.getWidth());

                             transactionX.addListener(new Animator.AnimatorListener() {
                                 @Override
                                 public void onAnimationStart(Animator animation) {

                                 }

                                 @Override
                                 public void onAnimationEnd(Animator animation) {
                                     //чтобы удалённый item не отображался
                                      itemView.setVisibility(View.GONE);
                                     getTaskFragment().moveTask(task);
                                     deleteItem(viewHolder.getLayoutPosition());

                                 }

                                 @Override
                                 public void onAnimationCancel(Animator animation) {

                                 }

                                 @Override
                                 public void onAnimationRepeat(Animator animation) {

                                 }
                             });
                             //этот аниматор возвращает объект списка в исходное состояние
                            ObjectAnimator transactionXback = ObjectAnimator.ofFloat(itemView,"translationX",itemView.getWidth(),0f);
                             AnimatorSet transactionSet = new AnimatorSet();
                             transactionSet.play(transactionX).before(transactionXback);
                             transactionSet.start();
                         }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    flipIn.start();
                }
            });
        }

    }



    @Override
    public int getItemViewType(int position) {
        //в зависимости от позиции возвращает таск или сепаратор
     if(getItem(position).isTask()){
         return TYPE_TASK;
     }else return TYPE_SEPARATOR;
    }


}
