package com.islavdroid.reminder;



import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;



import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.islavdroid.reminder.adapters.TabAdapter;
import com.islavdroid.reminder.database.DBHelper;
import com.islavdroid.reminder.dialogs.AddingTaskDialogFragment;
import com.islavdroid.reminder.fragments.CurrentTaskFragment;
import com.islavdroid.reminder.fragments.DoneTaskFragment;
import com.islavdroid.reminder.fragments.SplashFragment;
import com.islavdroid.reminder.fragments.TaskFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import model.ModelTask;


public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener,DoneTaskFragment.OnTaskRestoreListener,
        CurrentTaskFragment.OnTaskDoneListener{

private TabAdapter tabAdapter;
    private TaskFragment currentTaskFragment;
    private TaskFragment doneTaskFragment;
 private FragmentManager fragmentManager;
    PreferenceHelper preferenceHelper;
    public DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-----------сохранение настроек меню--------------
        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        //-----------сохранение настроек меню--------------

        dbHelper = new DBHelper(getApplicationContext());

        setUi();
        setData();
        fragmentManager = getFragmentManager();
        runSplash();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem splashItem = menu.findItem(R.id.splash_off);
        splashItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.splash_off){
            item.setChecked(!item.isChecked());
            //сохраняем состояние флага
            preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE,item.isChecked());

        }
        return super.onOptionsItemSelected(item);
    }
    public void runSplash(){
        //проверяем нажата галочка или нет
        if(!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();
            fragmentManager.beginTransaction().replace(R.id.activity_main, splashFragment).addToBackStack(null).commit();
        }

    }

    private void setUi(){
        setTitle("");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));
        final ViewPager viewPager =(ViewPager)findViewById(R.id.viewpager);
         tabAdapter =new TabAdapter(getSupportFragmentManager(),2);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
          viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
         currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment)tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);
        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DialogFragment addTask =new AddingTaskDialogFragment();
                addTask.show(fragmentManager,"AddingTaskDialogFragment");
            }
        });
    }

    private void setData(){
        TextView date_number =(TextView)findViewById(R.id.date_number);
        TextView day_week =(TextView)findViewById(R.id.day_week);
        TextView name_month =(TextView)findViewById(R.id.name_month);
        Locale locale = new Locale("ru", "RU");
        Calendar calendar = new GregorianCalendar();
        String month_name =calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
        String day_name =calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);
        String date =calendar.get(Calendar.DATE)+"";
        date_number.setText(date);
        day_week.setText(day_name);
        name_month.setText(month_name);
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
       currentTaskFragment.addTask(newTask,true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this,"Задание отменено",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        doneTaskFragment.addTask(task,false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        currentTaskFragment.addTask(task,false);

    }
}
