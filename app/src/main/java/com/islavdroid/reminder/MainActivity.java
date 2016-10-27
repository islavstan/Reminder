package com.islavdroid.reminder;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
        if(!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();
            fragmentManager.beginTransaction().replace(R.id.activity_main, splashFragment).addToBackStack(null).commit();
        }

    }

}
