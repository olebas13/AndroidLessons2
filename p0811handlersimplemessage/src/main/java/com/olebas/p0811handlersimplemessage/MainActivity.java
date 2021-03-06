package com.olebas.p0811handlersimplemessage;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final int STATUS_NONE = 0;
    final int STATUS_CONNECTING = 1;
    final int STATUS_CONNECTED = 2;

    Handler h;

    TextView tvStatus;
    ProgressBar pbConnect;
    Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        pbConnect = (ProgressBar) findViewById(R.id.pbConnect);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_NONE:
                        btnConnect.setEnabled(true);
                        tvStatus.setText("Not connected");
                        break;
                    case STATUS_CONNECTING:
                        btnConnect.setEnabled(false);
                        pbConnect.setVisibility(View.VISIBLE);
                        tvStatus.setText("Connecting");
                        break;
                    case STATUS_CONNECTED:
                        pbConnect.setVisibility(View.GONE);
                        tvStatus.setText("Connected");
                        break;
                }
            }
        };
        h.sendEmptyMessage(STATUS_NONE);
    }

    public void onClick(View v) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    h.sendEmptyMessage(STATUS_CONNECTING);
                    TimeUnit.SECONDS.sleep(2);

                    h.sendEmptyMessage(STATUS_CONNECTED);
                    TimeUnit.SECONDS.sleep(3);

                    h.sendEmptyMessage(STATUS_NONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
