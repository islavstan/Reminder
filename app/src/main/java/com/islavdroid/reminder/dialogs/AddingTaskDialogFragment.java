package com.islavdroid.reminder.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.islavdroid.reminder.R;
import com.islavdroid.reminder.Utils;

import java.util.Calendar;

public class AddingTaskDialogFragment extends DialogFragment{

    private AddingTaskListener addingTaskListener;

    public interface AddingTaskListener{
        void onTaskAdded();
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
        builder.setView(view);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDate.length()==0){
                    etDate.setText(" ");
                }
                DialogFragment datePickerFragment =new DatePickerFragment(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,month,dayOfMonth);
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
                        Calendar timeCal = Calendar.getInstance();
                        timeCal.set(0,0,0,hourOfDay,minute);
                        etTime.setText(Utils.getTime(timeCal.getTimeInMillis()));
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
                addingTaskListener.onTaskAdded();
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