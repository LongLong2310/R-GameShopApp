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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

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
    private String selectedFilter, spinnerType;
    private SearchView searchView;
    private AutoCompleteTextView autoCompleteTextView, autoCompleteTextViewUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        dbManager = new DatabaseManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_screen);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        spinnerFilter.getBackground().setColorFilter(getResources().getColor(R.color.border_color), PorterDuff.Mode.SRC_ATOP);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        arrayList.add("ID");
        arrayList.add("Type");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList);
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

        ArrayList<String> arrayListType = new ArrayList<>();
        arrayListType.add("GAME");
        arrayListType.add("CONSOLE");
        arrayListType.add("ACCESSORY");
        ArrayAdapter<String> arrayAdapterType = new ArrayAdapter<String>(this, R.layout.spinner_type, arrayListType);
        autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(arrayAdapterType);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinnerType = autoCompleteTextView.getText().toString();
            }
        });

        autoCompleteTextViewUpdate = findViewById(R.id.filled_exposed_update);
        autoCompleteTextViewUpdate.setAdapter(arrayAdapterType);
        autoCompleteTextViewUpdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinnerType = autoCompleteTextViewUpdate.getText().toString();
            }
        });

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
    protected void onResume() {
        super.onResume();
        showStockList();
    }

    //One method to set visibility for our widgets
    private void setVisible(int id, boolean isVisible) {
        View aView = findViewById(id);
        if (isVisible)
            aView.setVisibility(View.VISIBLE);
        else
            aView.setVisibility(View.INVISIBLE);
    }

    public void onSubmitFormCancel(View view) {
        EditText tName = null;
        AutoCompleteTextView tType = null;
        EditText tStockNum = null;
        EditText tPrice = null;

        if (findViewById(R.id.addLayout).getVisibility() == View.VISIBLE) {
            tName = findViewById(R.id.tName);
            tType = findViewById(R.id.filled_exposed);
            tStockNum = findViewById(R.id.tStockNum);
            tPrice = findViewById(R.id.tPrice);
        } else if (findViewById(R.id.updateLayout).getVisibility() == View.VISIBLE) {
            tName = findViewById(R.id.tNameUpdate);
            tType = findViewById(R.id.filled_exposed_update);
            tStockNum = findViewById(R.id.tStockNumUpdate);
            tPrice = findViewById(R.id.tPriceUpdate);
        }

        setVisible(R.id.addLayout, false);
        setVisible(R.id.updateLayout, false);
        setVisible(R.id.list, true);

        tName.setText("");
        tStockNum.setText("");
        tPrice.setText("");
        tType.setText("");

    }

    public void onAddStock(View view) {
        EditText tName = findViewById(R.id.tName);
        AutoCompleteTextView tType = findViewById(R.id.filled_exposed);
        EditText tStockNum = findViewById(R.id.tStockNum);
        EditText tPrice = findViewById(R.id.tPrice);
        if (tName.getText().toString().equals("") || tType.getText().toString().equals("") ||
                tStockNum.getText().toString().equals("") || tPrice.getText().toString().equals("")) {
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
        if (!isNumeric(tStockNum.getText().toString()) || !isNumeric(tPrice.getText().toString()) || dbManager.checkStock(tName.getText().toString()) == true) {
            AlertDialog dialog = new
                    AlertDialog.Builder(stockScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The stock or price must be number and name must be unique");
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
        tStockNum.setText("");
        tPrice.setText("");
        tType.setText("");
        setVisible(R.id.addLayout, false);
        showStockList();
    }

    private void showStockList() {
        setVisible(R.id.addLayout, false);
        dbManager.open();
        cursor = dbManager.selectAllStock();
        if (cursor.getCount() == 0) {
//            setVisible(R.id.noRecordText, true);
            setVisible(R.id.list, false);
        } else {
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
        }
    }

    public void onAddClick(View view) {
        LinearLayout addLayout = findViewById(R.id.addLayout);
        setVisible(R.id.addLayout, true);
        setVisible(R.id.list, true);
    }

    public void onBackClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }

    public void onUpdateStock(View view) {
        EditText tName = findViewById(R.id.tNameUpdate);
        AutoCompleteTextView tType = findViewById(R.id.filled_exposed_update);
        EditText tStockNum = findViewById(R.id.tStockNumUpdate);
        EditText tPrice = findViewById(R.id.tPriceUpdate);

        if (tName.getText().toString().equals("") || tType.getText().toString().equals("") ||
                tStockNum.getText().toString().equals("") || tPrice.getText().toString().equals("")) {
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
        if (!isNumeric(tStockNum.getText().toString()) || !isNumeric(tPrice.getText().toString()) || ((dbManager.checkStock(tName.getText().toString()) == true) && (!tName.getText().toString().equals(cursor.getString(1))))) {
            AlertDialog dialog = new
                    AlertDialog.Builder(stockScreen.this).create();
            dialog.setTitle("Invalid input");
            dialog.setMessage("The stock or price must be number and name must be unique");
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
        setVisible(R.id.updateLayout, false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        searchView = (SearchView) findViewById(R.id.simpleSearchView);
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
        if (selectedFilter.equals("Name")) {
            cursor = dbManager.searchStockName(string);
        } else if (selectedFilter.equals("ID")) {
            cursor = dbManager.searchStockID(string);
        } else {
            cursor = dbManager.searchStockType(string);
        }

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show();
        } else {
            listView.setAdapter(adapter);
        }
    }
}