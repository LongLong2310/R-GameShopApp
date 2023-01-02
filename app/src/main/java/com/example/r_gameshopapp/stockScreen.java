package com.example.r_gameshopapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class stockScreen extends AppCompatActivity {
    private Cursor cursor;
    private DatabaseManager dbManager;
    private ImageButton addButton;
    private ImageButton backButton;
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
    private ListView listView;
    private Spinner spinnerFilter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbManager = new DatabaseManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_screen);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        spinnerFilter.getBackground().setColorFilter(getResources().getColor(R.color.border_color), PorterDuff.Mode.SRC_ATOP);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(arrayAdapter);

        showStockList();

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
        showStockList();
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

    public void onAddStock(View view) {
        EditText tName= findViewById(R.id.tName);
        EditText tType = findViewById(R.id.tType);
        EditText tStockNum = findViewById(R.id.tStockNum);
        EditText tPrice = findViewById(R.id.tPrice);
        if (tName.getText().toString().equals("") || tType.getText().toString().equals("") ||
                tStockNum.getText().toString().equals("") || tPrice.getText().toString().equals("")){
            AlertDialog dialog = new
                    AlertDialog.Builder(stockScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The input cannot be empty");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.addLayout, false);
                            showStockList();
                        }
                    });
            dialog.show();
            return;
        }
        if (!isNumeric(tStockNum.getText().toString()) || !isNumeric(tPrice.getText().toString())) {
            AlertDialog dialog = new
                    AlertDialog.Builder(stockScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The stock or price must be number");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.addLayout, false);
                            showStockList();
                        }
                    });
            dialog.show();
            return;
        }
        dbManager.insertStock(
                tName.getText().toString(),
                tType.getText().toString(),
                Integer.parseInt(tStockNum.getText().toString()),
                Double.parseDouble(tPrice.getText().toString()));
        tName.setText("");
        tType.setText("");
        tStockNum.setText("");
        tPrice.setText("");
        setVisible(R.id.addLayout,false);
        showStockList();
    }

    private void showStockList() {
        setVisible(R.id.addLayout, false);
        dbManager.open();
        cursor = dbManager.selectAllStock();
        if (cursor.getCount() == 0){
//            setVisible(R.id.noRecordText, true);
            setVisible(R.id.list, false);
        }
        else {
            listView = (ListView) findViewById(R.id.list);
            setVisible(R.id.list, true);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this, R.layout.activity_view_record, cursor, from, to, 0);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int
                        position, long id) {
                    AlertDialog dialog = new
                            AlertDialog.Builder(stockScreen.this).create();
                    dialog.setTitle(cursor.getString(2));
                    dialog.setMessage("Do you want to delete or update this stock?");
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                            "Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int
                                        which) {
                                    dbManager.deleteStock(cursor.getInt(0));
                                    showStockList();
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
            searchView = (SearchView) findViewById(R.id.simpleSearchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (toList(query).contains(query)) {
                        Toast.makeText(stockScreen.this, "Found", Toast.LENGTH_LONG).show();
                        adapter.getFilter().filter(query);
                    }
                    else {
                        Toast.makeText(stockScreen.this, "No match found", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
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

    public void onUpdateStock(View view) {
        EditText tName= findViewById(R.id.tNameUpdate);
        EditText tType = findViewById(R.id.tTypeUpdate);
        EditText tStockNum = findViewById(R.id.tStockNumUpdate);
        EditText tPrice = findViewById(R.id.tPriceUpdate);

        if (tName.getText().toString().equals("") || tType.getText().toString().equals("") ||
                tStockNum.getText().toString().equals("") || tPrice.getText().toString().equals("")){
            AlertDialog dialog = new
                    AlertDialog.Builder(stockScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The input cannot be empty");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.updateLayout, false);
                            showStockList();
                        }
                    });
            dialog.show();
            return;
        }
        if (!isNumeric(tStockNum.getText().toString()) || !isNumeric(tPrice.getText().toString())) {
            AlertDialog dialog = new
                    AlertDialog.Builder(stockScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The stock or price must be number");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setVisible(R.id.updateLayout, false);
                            showStockList();
                        }
                    });
            dialog.show();
            return;
        }
        dbManager.updateStock(
                cursor.getInt(0),
                tName.getText().toString(),
                tType.getText().toString(),
                Integer.parseInt(tStockNum.getText().toString()),
                Double.parseDouble(tPrice.getText().toString()));
        tName.setText("");
        tType.setText("");
        tStockNum.setText("");
        tPrice.setText("");
        setVisible(R.id.updateLayout,false);
        showStockList();
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

    @SuppressLint("Range")
    private ArrayList<String> toList(String name) {
        dbManager.open();
        cursor = dbManager.searchName(name);
        ArrayList<String>  list = new ArrayList<String>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME))); //add the item
            cursor.moveToNext();
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView = (SearchView) findViewById(R.id.simpleSearchView);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStock(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchStock(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchStock(String string) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.activity_view_record_user_history_detail, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        List<Stock> stockList = databaseHelper.search(string);
        if (stockList != null) {
            listView.setAdapter(adapter);
        }
    }
}