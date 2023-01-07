package com.example.r_gameshopapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

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
    private ListView listView;
    private Spinner spinnerFilter;
    private String selectedFilter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbManager = new DatabaseManager(this);
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_account_screen);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        spinnerFilter.getBackground().setColorFilter(getResources().getColor(R.color.border_color), PorterDuff.Mode.SRC_ATOP);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        arrayList.add("ID");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerFilter.setAdapter(arrayAdapter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilter = spinnerFilter.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            listView = (ListView) findViewById(R.id.list);
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

    public void onBackClick(View view) {
        finish();
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
        if ( !isNumeric(tCash.getText().toString())|| ((dbManager.checkUser(tName.getText().toString())==true) && (!tName.getText().toString().equals(cursor.getString(1))))) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        searchView = (SearchView) findViewById(R.id.simpleSearchView);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAccount(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAccount(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchAccount(String string) {
        if (selectedFilter.equals("Name")) {
            cursor = dbManager.searchAccountName(string);
        }
        else if (selectedFilter.equals("ID")) {
            cursor = dbManager.searchAccountID(string);
        }

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.account_layout, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show();
        }
        else {
            listView.setAdapter(adapter);
        }
    }
}