package com.example.r_gameshopapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.r_gameshopapp.ui.DatabaseHelper;

import java.util.ArrayList;

public class stockScreen extends AppCompatActivity {
    private Cursor cursor;
    private String[] from = new String[]{
            DatabaseHelper.ID,
            DatabaseHelper.NAME,
            DatabaseHelper.TYPE,
            DatabaseHelper.STOCK,
            DatabaseHelper.PRICE
    };
    private int[] to = new int[]{
            R.id.lIdNumber,
            R.id.lName,
            R.id.lType,
            R.id.lStockNumber,
            R.id.lPrice
    };
    private Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_screen);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(arrayAdapter);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setVisibility(View.VISIBLE);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
//                AlertDialog dialog = new
//                        AlertDialog.Builder(MainActivity.this).create();
//                dialog.setTitle("Delete " + cursor.getString(2));
//                dialog.setMessage("Do you want to delete this currency?");
//                dialog.setButton(AlertDialog.BUTTON_POSITIVE,
//                        "Delete",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int
//                                    which) {
//                                dbManager.delete(cursor.getInt(0));
//                                showCurrencyList();
//                            }
//                        });
//                dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
//                        "Update",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int
//                                    which) {
//                                setVisible(R.id.updateLayout, true);
//                                setVisible(R.id.button, false);
//                                setVisible(R.id.list, false);
//                            }
//                        });
//                dialog.show();
            }
        });
    }
}