package com.islavdroid.reminder;

import android.content.Context;

import java.util.concurrent.TimeUnit;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SplashFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SplachTask splachTask =new SplachTask();
        splachTask.execute();
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    class SplachTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            //задержка 2 секунды
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getActivity().getFragmentManager().popBackStack();
            return null;
        }
    }


}
