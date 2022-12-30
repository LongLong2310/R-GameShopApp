package com.example.r_gameshopapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class adminHistory extends AppCompatActivity {
    private Cursor cursor;
    private DatabaseManager dbManager;
    private ImageButton addButton;
    private String[] from = new String[]{
            DatabaseHelper.TID,
            DatabaseHelper.DATE,
            DatabaseHelper.CID,
            DatabaseHelper.AMOUNT
    };
    private int[] to = new int[]{
            R.id.lTransactionIdNumber,
            R.id.lDateNum,
            R.id.lCusIDNum,
            R.id.lAmount
    };
    private Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbManager = new DatabaseManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        spinnerFilter.getBackground().setColorFilter(getResources().getColor(R.color.border_color), PorterDuff.Mode.SRC_ATOP);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(arrayAdapter);

        showHistoryList();

//        addButton = (ImageButton) findViewById(R.id.add_button);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout addLayout = findViewById(R.id.addLayout);
//                addLayout.setVisibility(View.VISIBLE);
//                setVisible(R.id.list, false);
//            }
//        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        showHistoryList();
    }

    //One method to set visibility for our widgets
    private void setVisible(int id, boolean isVisible){
        View aView = findViewById(id);
        if (isVisible)
            aView.setVisibility(View.VISIBLE);
        else
            aView.setVisibility(View.INVISIBLE);
    }

    public void onSubmitFormCancel(View view) {
        setVisible(R.id.addLayout, false);
        setVisible(R.id.updateLayout, false);
        setVisible(R.id.list, true);
    }

    private void showHistoryList() {
        dbManager.open();
        cursor = dbManager.selectAllHistory();
        if (cursor.getCount() == 0){
//            setVisible(R.id.noRecordText, true);
            setVisible(R.id.list, false);
        }
        else {
            ListView listView = (ListView) findViewById(R.id.list);
            setVisible(R.id.list, true);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this, R.layout.activity_view_record_user_history, cursor, from, to, 0);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int
                        position, long id) {
                        setVisible(R.id.detail_layout,true);
                        showHistoryDetail();
                }
            });
        }
    }

    private void showHistoryDetail() {
        dbManager.open();
        cursor = dbManager.selectAllHistory();
        if (cursor.getCount() == 0){
//            setVisible(R.id.noRecordText, true);
            setVisible(R.id.list, false);
        }
        else {
            ListView listView = (ListView) findViewById(R.id.list);
            setVisible(R.id.list, true);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this, R.layout.activity_view_record_user_history_detail, cursor, from, to, 0);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dbManager.close();
    }
}