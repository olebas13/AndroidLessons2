package com.olebas.p0621alertdialogitem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import static android.content.DialogInterface.*;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    final int DIALOG_ITEMS = 1;
    final int DIALOG_ADAPTER = 2;
    final int DIALOG_CURSOR = 3;
    int cnt = 0;
    DB db;
    Cursor cursor;

    String[] data = {"one", "two", "three", "four"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        db.open();
        cursor = db.getAllData();
        startManagingCursor(cursor);
    }

    public void onClick(View v) {
        changeCount();
        switch (v.getId()) {
            case R.id.btnItems:
                showDialog(DIALOG_ITEMS);
                break;
            case R.id.btnAdapter:
                showDialog(DIALOG_ADAPTER);
                break;
            case R.id.btnCursor:
                showDialog(DIALOG_CURSOR);
                break;
            default:
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {
            case DIALOG_ITEMS:
                adb.setTitle(R.string.items);
                adb.setItems(data, myClickListener);
                break;
            case DIALOG_ADAPTER:
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.select_dialog_item, data);
                adb.setAdapter(adapter, myClickListener);
                break;
            case DIALOG_CURSOR:
                adb.setTitle(R.string.cursor);
                adb.setCursor(cursor, myClickListener, DB.COLUMN_TXT);
                break;
        }
        return adb.create();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        AlertDialog aDialog = (AlertDialog) dialog;
        ListAdapter lAdapter = aDialog.getListView().getAdapter();

        switch (id) {
            case DIALOG_ITEMS:
            case DIALOG_ADAPTER:
                if (lAdapter instanceof BaseAdapter) {
                    BaseAdapter bAdapter = (BaseAdapter) lAdapter;
                    bAdapter.notifyDataSetChanged();
                }
                break;
            case DIALOG_CURSOR:
                break;
            default:
                break;
        }
        super.onPrepareDialog(id, dialog);
    }

    OnClickListener myClickListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(LOG_TAG, "which = " + which);
        }
    };

    private void changeCount() {
        cnt++;
        data[3] = String.valueOf(cnt);
        db.changeRec(4, String.valueOf(cnt));
        cursor.requery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
