package com.olebas.p0801handler;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    Handler h;
    TextView tvInfo;
    Button btnStart;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = this.findViewById(R.id.tvInfo);
        btnStart = findViewById(R.id.btnStart);
        h = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(android.os.Message msg) {
                tvInfo.setText("Закачано файлов: " + msg.what);
                if (msg.what == 10) {
                    btnStart.setEnabled(true);
                }
            }
        };
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1; i <= 10; i++) {
                            downloadFile();
                            h.sendEmptyMessage(i);
                            Log.d(LOG_TAG, "i = " + i);
                        }
                    }
                });
                t.start();
                break;
            case R.id.btnTest:
                Log.d(LOG_TAG, "test");
                break;
            default:
                break;
        }
    }

    private void downloadFile() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
