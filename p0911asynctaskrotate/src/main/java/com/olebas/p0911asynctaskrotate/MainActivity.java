package com.olebas.p0911asynctaskrotate;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {


    MyTask mt;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("myLogs", "create MainActivity: " + this.hashCode());

        tv = (TextView) findViewById(R.id.tv);

        mt = (MyTask) getLastNonConfigurationInstance();
        if (mt == null) {
            mt = new MyTask();
            mt.execute();
        }
        mt.link(this);
        Log.d("myLogs", "create MyTask: " + mt.hashCode());
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        mt.unLink();
        return mt;
    }

    private static class MyTask extends AsyncTask<String, Integer, Void> {

        MainActivity activity;

        @Override
        protected Void doInBackground(String... params) {
            try {
                for (int i = 1; i <= 10; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    publishProgress(i);
                    Log.d("myLogs", "i = " + i
                        + ", MyTask: " + this.hashCode()
                        + ", MainActivity: " + this.hashCode());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            activity.tv.setText("i = " + values[0]);
        }

        public void link(MainActivity act) {
            activity = act;
        }

        public void unLink() {
            activity = null;
        }
    }
}
