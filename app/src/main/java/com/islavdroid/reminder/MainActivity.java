package com.islavdroid.reminder;



import android.app.FragmentManager;
import android.support.design.widget.TabLayout;



import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.islavdroid.reminder.adapters.TabAdapter;
import com.islavdroid.reminder.fragments.SplashFragment;


public class MainActivity extends AppCompatActivity {
 private FragmentManager fragmentManager;
    PreferenceHelper preferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-----------сохранение настроек меню--------------
        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        //-----------сохранение настроек меню--------------

        setUi();
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
        TabAdapter tabAdapter =new TabAdapter(getSupportFragmentManager(),2);
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
    }

}
