package com.islavdroid.reminder.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.islavdroid.reminder.R;
import com.islavdroid.reminder.Utils;

import java.util.Calendar;

import com.islavdroid.reminder.alarm.AlarmHelper;
import com.islavdroid.reminder.model.ModelTask;

public class AddingTaskDialogFragment extends DialogFragment{

    private AddingTaskListener addingTaskListener;

    public interface AddingTaskListener{
        void onTaskAdded(ModelTask newTask);
        void onTaskAddingCancel();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            addingTaskListener=(AddingTaskListener) context;

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
                addingTaskListener=(AddingTaskListener) activity;

            }catch (ClassCastException ex){
                throw new ClassCastException(activity.toString()+"must implements AddingTaskListener");
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation2;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_task,null);
        final TextInputLayout title = (TextInputLayout)view.findViewById(R.id.dialogTaskTitle);
        final EditText etTitle = title.getEditText();
        title.setHint(getResources().getString(R.string.title_hint));
        TextInputLayout date = (TextInputLayout)view.findViewById(R.id.dialogTaskDate);
        final EditText etDate = date.getEditText();
        date.setHint(getResources().getString(R.string.date_hint));
        final TextInputLayout time = (TextInputLayout)view.findViewById(R.id.dialogTaskTime);
        final EditText etTime = time.getEditText();

        time.setHint(getResources().getString(R.string.time_hint));

        Spinner prioritySpinner = (Spinner)view.findViewById(R.id.spDialogTaskPriority) ;


        builder.setView(view);
        final ModelTask modelTask =new ModelTask();

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,ModelTask.PRIORITY_LEVELS);
      prioritySpinner.setAdapter(priorityAdapter);

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //установим приоритет используя position
                modelTask.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });














        final Calendar calendar=Calendar.getInstance();
        //срабатывает через час если указана только дата при создании задачи
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)+1);


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDate.length()==0){
                    etDate.setText(" ");
                }
                DialogFragment datePickerFragment =new DatePickerFragment(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        etDate.setText(Utils.getDate(calendar.getTimeInMillis()));

                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etDate.setText(null);
                    }
                };
                datePickerFragment.show(getFragmentManager(),"DatePickerFragment");
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTime.length()==0){
                    etTime.setText(" ");
                }
                DialogFragment timePickerFragment =new TimePickerFragment(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        calendar.set(Calendar.SECOND,0);
                        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        etTime.setText(null);
                    }
                };
                timePickerFragment.show(getFragmentManager(),"TimePickerFragment");
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modelTask.setTitle(etTitle.getText().toString());
                modelTask.setStatus(modelTask.STATUS_CURRENT);
                if(etDate.length()!=0||etTime.length()!=0){
                    modelTask.setDate(calendar.getTimeInMillis());
                   //здесь создаём объект AlarmHelper
                    AlarmHelper alarmHelper =AlarmHelper.getInstance();
                    alarmHelper.setAlarm(modelTask);



                }
                modelTask.setStatus(ModelTask.STATUS_CURRENT);
                addingTaskListener.onTaskAdded(modelTask);
             dialog.dismiss();



            }
        });
 builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
     @Override
     public void onClick(DialogInterface dialog, int which) {
         addingTaskListener.onTaskAddingCancel();
         dialog.dismiss();
     }
 });
        AlertDialog alertDialog=builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if(etTitle.length()==0){
                    positiveButton.setEnabled(false);
                    //если поле ввода текста пустое, показываем ошибку
                    title.setError(getResources().getString(R.string.error));
                }
                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                       if(s.length()==0){
                           positiveButton.setEnabled(false);
                           title.setError(getResources().getString(R.string.error));
                       }else{
                           positiveButton.setEnabled(true);
                           title.setErrorEnabled(false);
                       }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }
}
