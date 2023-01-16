package com.example.r_gameshopapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserPurchaseHistory extends AppCompatActivity {
    private Cursor cursor;
    private DatabaseManager dbManager;
    private ImageButton addButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView itemTotal;
    private ArrayList<Item> itemCartList;

    private String[] from = new String[]{
            DatabaseHelper.ID,
            DatabaseHelper.DATE,
            DatabaseHelper.TOTAL
    };
    private int[] to = new int[]{
            R.id.lTransactionId,
            R.id.lDate,
            R.id.lTotalPrice
    };
    private ListView listView;
    private ListView listViewDetail;
    private Spinner spinnerFilter;
    private String selectedFilter;
    private SearchView searchView;
    private userMain userMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        dbManager = new DatabaseManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_purchase_history);
        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        spinnerFilter.getBackground().setColorFilter(getResources().getColor(R.color.border_color), PorterDuff.Mode.SRC_ATOP);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        arrayList.add("ID");
        arrayList.add("Type");
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

        showHistoryList(this);

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
        showHistoryList(this);
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


    private void showHistoryList(Context context) {
        dbManager.open();
        Intent intent = getIntent();
        String UserID = (String) intent.getExtras().get("UserID");
        System.out.println(UserID);
        cursor = dbManager.selectUserPurchaseHistory(UserID);
        if (cursor.getCount() == 0){
//            setVisible(R.id.noRecordText, true);
            setVisible(R.id.list, false);
            System.out.println("123");
        }
        else {
            System.out.println("321");
            listView = (ListView) findViewById(R.id.list);
            setVisible(R.id.list, true);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this, R.layout.activity_view_record_user_past_purchase, cursor, from, to, 0);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int
                        position, long id) {
                    setVisible(R.id.detail_layout,true);
                    createHistoryItemDetailDialog(context);
                }
            });
        }
    }

//    private void showHistoryDetail(Context context) {
//        dbManager.open();
//        cursor = dbManager.selectAllHistory();
//        if (cursor.getCount() == 0){
////            setVisible(R.id.noRecordText, true);
//            setVisible(R.id.list, false);
//        }
//        else {
//            ListView listView = (ListView) findViewById(R.id.listTDetail);
//            setVisible(R.id.list, true);
//            System.out.println(cursor.getString(2));
//            List<Item> list = new Gson().fromJson(cursor.getString(2), new TypeToken<List<Item>>(){}.getType());
//            itemCartList = new ArrayList<Item>(list);
//            ListAdapter listAdapter = new ListAdapter(context, R.layout.fragment_cart_item, itemCartList);
//            listView.setAdapter(listAdapter);
//        }
//    }

    public void createHistoryItemDetailDialog(Context context){
        dialogBuilder = new AlertDialog.Builder(context);
        final View itemDetailPopupView = getLayoutInflater().inflate(R.layout.transaction_detail, null);
        itemTotal = (TextView) itemDetailPopupView.findViewById(R.id.itemTotalTextView);

        listViewDetail = itemDetailPopupView.findViewById(R.id.listTDetail);
        setVisible(R.id.list, true);
        List<Item> list = new Gson().fromJson(cursor.getString(2), new TypeToken<List<Item>>(){}.getType());
        System.out.println(cursor.getString(2));
        System.out.println(list.get(0).getitemName());
        itemCartList = new ArrayList<Item>(list);
        System.out.println(itemCartList.get(0).getitemName());
        ListAdapter listAdapter = new ListAdapter(context, R.layout.fragment_cart_item, itemCartList);
        listViewDetail.setAdapter(listAdapter);

        dialogBuilder.setView(itemDetailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dbManager.close();
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
        }
        else if (selectedFilter.equals("ID")) {
            cursor = dbManager.searchStockID(string);
        }
        else {
            cursor = dbManager.searchStockType(string);
        }

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.activity_view_record_user_history, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show();
        }
        else {
            listView.setAdapter(adapter);
        }
    }
}