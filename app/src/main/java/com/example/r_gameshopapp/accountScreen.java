package com.example.r_gameshopapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
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

public class accountScreen extends AppCompatActivity {
    private Cursor cursor;
    private DatabaseManager dbManager;
    private ImageButton addButton;
    private ImageButton backButton;
    private String[] from = new String[]{
            DatabaseHelper.ID,
            DatabaseHelper.NAME,
            DatabaseHelper.PASS,
            DatabaseHelper.CASH
    };
    private int[] to = new int[]{
            R.id.lIdNumber,
            R.id.lName,
            R.id.lPassword,
            R.id.lBalanceNumber
    };
    private Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbManager = new DatabaseManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(arrayAdapter);

        showAccountList();

        addButton = (ImageButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout addLayout = findViewById(R.id.addLayout);
                addLayout.setVisibility(View.VISIBLE);
                setVisible(R.id.list, false);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        showAccountList();
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

    public void onAddAccount(View view) {
        EditText tName= findViewById(R.id.tName);
        EditText tPass = findViewById(R.id.tPass);
        EditText tCash = findViewById(R.id.tCash);
        if (tName.getText().toString().equals("") || tPass.getText().toString().equals("") ||
                tCash.getText().toString().equals("") ){
            AlertDialog dialog = new
                    AlertDialog.Builder(accountScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The input cannot be empty");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.addLayout, false);
                            showAccountList();
                        }
                    });
            dialog.show();
            return;
        }
        if ( !isNumeric(tCash.getText().toString()) ||dbManager.checkUser(tName.getText().toString())==true) {
            AlertDialog dialog = new
                    AlertDialog.Builder(accountScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The balance must be number and name must be unique");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.addLayout, false);
                            showAccountList();
                        }
                    });
            dialog.show();
            return;
        }
        dbManager.insertAccount(
                tName.getText().toString(),
                tPass.getText().toString(),
                Double.parseDouble(tCash.getText().toString()));
        tName.setText("");
        tPass.setText("");
        tCash.setText("");
        setVisible(R.id.addLayout,false);
        showAccountList();
    }

    private void showAccountList() {
        setVisible(R.id.addLayout, false);
        dbManager.open();
        cursor = dbManager.selectAllAccount();
        if (cursor.getCount() == 0){
//            setVisible(R.id.noRecordText, true);
            setVisible(R.id.list, false);
        }
        else {
            ListView listView = (ListView) findViewById(R.id.list);
            setVisible(R.id.list, true);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this, R.layout.account_layout, cursor, from, to, 0);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int
                        position, long id) {
                    AlertDialog dialog = new
                            AlertDialog.Builder(accountScreen.this).create();
                    dialog.setTitle(cursor.getString(1));
                    dialog.setMessage("Do you want to delete or update this account?");
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                            "Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int
                                        which) {
                                    dbManager.deleteAccount(cursor.getInt(0));
                                    showAccountList();
                                }
                            });
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                            "Update",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int
                                        which) {
                                    setVisible(R.id.updateLayout, true);
                                    setVisible(R.id.list, false);
                                }
                            });
                    dialog.show();
                }
            });
        }
    }
    public void onAddClick(View view) {
        LinearLayout addLayout = findViewById(R.id.addLayout);
        setVisible(R.id.addLayout,true);
        setVisible(R.id.list, true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dbManager.close();
    }

    public void onUpdateAccount(View view) {
        EditText tName= findViewById(R.id.tNameUpdate);
        EditText tPass = findViewById(R.id.tPassUpdate);
        EditText tCash = findViewById(R.id.tCashUpdate);

        if (tName.getText().toString().equals("") || tPass.getText().toString().equals("") ||
                tCash.getText().toString().equals("")){
            AlertDialog dialog = new
                    AlertDialog.Builder(accountScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The input cannot be empty");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.updateLayout, false);
                            showAccountList();
                        }
                    });
            dialog.show();
            return;
        }
        if ( !isNumeric(tCash.getText().toString())|| dbManager.checkUser(tName.getText().toString())==true) {
            AlertDialog dialog = new
                    AlertDialog.Builder(accountScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The balance must be number and name must be unique");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.updateLayout, false);
                            showAccountList();
                        }
                    });
            dialog.show();
            return;
        }
        dbManager.updateAccount(
                cursor.getInt(0),
                tName.getText().toString(),
                tPass.getText().toString(),
                Double.parseDouble(tCash.getText().toString()));
        tName.setText("");
        tPass.setText("");
        tCash.setText("");
        setVisible(R.id.updateLayout,false);
        showAccountList();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}