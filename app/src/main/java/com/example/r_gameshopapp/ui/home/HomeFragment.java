package com.example.r_gameshopapp.ui.home;

import static android.content.Context.SEARCH_SERVICE;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.r_gameshopapp.BackgroundMusicService;
import com.example.r_gameshopapp.DatabaseManager;
import com.example.r_gameshopapp.GridAdapter;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.databinding.FragmentHomeBinding;
import com.example.r_gameshopapp.userMain;
import com.example.r_gameshopapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView itemName, itemStock, itemPrice, amount, category_title, no_result, editTextContact, editTextSubject;
    private ImageButton cancel_button, more_button, search_button;
    private Button  button_add_to_cart, game_category_button,
                    console_category_button, accessories_category_button,
                    all_category_button, play_button, stop_button, contactConfirm;
    private ImageView img;
    private LinearLayout search_bar;
    private DatabaseManager dbManager;
    private Spinner spinnerFilter;
    private String selectedFilter, spinnerType;
    private SearchView searchView;
    private Cursor cursor;
    private String selected_category = "ALL";
    private String listAsString = "";
    ArrayList<Item> displayList = new ArrayList<>();

    private FragmentHomeBinding binding;
    GridView gridList;
    ArrayList<Item> itemList = new ArrayList<>();
    List<Item> itemListCart = new ArrayList<>();
    boolean isAddToCart = false;

    private ISendDataListener iSendDataListener;

    public interface ISendDataListener{
        void sendData(String string);
    }


    public HomeFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iSendDataListener = (ISendDataListener) getActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context context = inflater.getContext();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gridList = (GridView) root.findViewById(R.id.gridView);
        more_button = (ImageButton) root.findViewById(R.id.more_button);
        search_button = (ImageButton) root.findViewById(R.id.search_button);
        search_bar = (LinearLayout) root.findViewById(R.id.search_bar);
        search_bar.setVisibility(View.GONE);
        no_result = (TextView) root.findViewById(R.id.no_result);
        no_result.setVisibility(View.GONE);

        dbManager = new DatabaseManager(getActivity());
        itemList = dbManager.getAllItem();
        dbManager.open();
        cursor = dbManager.searchAccountID(Integer.toString(((userMain)getActivity()).getid()));
        displayList.clear();
        for (Item item: itemList){
            if(item.getitemCategory().equals(selected_category)){
                displayList.add(item);
            } else if (selected_category.equals("ALL")){
                displayList.add(item);
            }
        }

        GridAdapter gridAdapter = new GridAdapter(context, R.layout.fragment_home_item, displayList);
        gridList.setAdapter(gridAdapter);
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                createItemDetailDialog(context, displayList.get(i));

            }
        });
        category_title = (TextView) root.findViewById(R.id.category_title);
        game_category_button = (Button) root.findViewById(R.id.game_category_button);
        console_category_button = (Button) root.findViewById(R.id.console_category_button);
        accessories_category_button = (Button) root.findViewById(R.id.accessories_category_button);
        all_category_button = (Button) root.findViewById(R.id.all_category_button);
        game_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridAdapter.clear();
                for (Item item: itemList){
                    if(item.getitemCategory().equals("GAME")) {
                        gridAdapter.add(item);
                    }
                }
                gridAdapter.notifyDataSetChanged();
                gridList.invalidateViews();
                category_title.setText("GAME");
                if (gridAdapter.getCount() == 0) {
                    no_result.setVisibility(View.VISIBLE);
                } else {
                    no_result.setVisibility(View.GONE);
                }

            }
        });

        console_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridAdapter.clear();
                for (Item item: itemList){
                    if(item.getitemCategory().equals("CONSOLE")) {
                        gridAdapter.add(item);
                    }
                }
                gridAdapter.notifyDataSetChanged();
                gridList.invalidateViews();
                category_title.setText("CONSOLE");
                if (gridAdapter.getCount() == 0) {
                    no_result.setVisibility(View.VISIBLE);
                } else {
                    no_result.setVisibility(View.GONE);
                }
            }
        });

        accessories_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridAdapter.clear();
                for (Item item: itemList){
                    if(item.getitemCategory().equals("ACCESSORY")) {
                        gridAdapter.add(item);
                    }
                }
                gridAdapter.notifyDataSetChanged();
                gridList.invalidateViews();
                category_title.setText("ACCESSORIES");
                if (gridAdapter.getCount() == 0) {
                    no_result.setVisibility(View.VISIBLE);
                } else {
                    no_result.setVisibility(View.GONE);
                }
            }
        });

        all_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridAdapter.clear();
                for (Item item: itemList){
                    gridAdapter.add(item);
                }
                gridAdapter.notifyDataSetChanged();
                gridList.invalidateViews();
                category_title.setText("ALL PRODUCTS");
                if (gridAdapter.getCount() == 0) {
                    no_result.setVisibility(View.VISIBLE);
                } else {
                    no_result.setVisibility(View.GONE);
                }
            }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        spinnerFilter = (Spinner) root.findViewById(R.id.spinnerFilter);
        spinnerFilter.getBackground().setColorFilter(getResources().getColor(R.color.border_color), PorterDuff.Mode.SRC_ATOP);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Name");
        arrayList.add("Max Price");
        arrayList.add("Min Price");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, arrayList);
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
            searchView = (SearchView) root.findViewById(R.id.simpleSearchView);
            SearchManager searchManager = (SearchManager) requireActivity().getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    gridAdapter.clear();
                    if (selectedFilter.equals("Name")) {
                        for (Item item : itemList) {
                            if (item.getitemName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                                gridAdapter.add(item);
                            }
                        }
                    } else if (selectedFilter.equals("Max Price")) {
                        if (query.length() == 0) {
                            for (Item item: itemList) {
                                gridAdapter.add(item);
                            }
                        } if (isNumeric(query)) {
                            for (Item item : itemList) {
                                if (item.getitemPrice() <= Double.parseDouble(query)) {
                                    gridAdapter.add(item);
                                }
                            }
                        }
                    } else if (selectedFilter.equals("Min Price")) {
                        if (query.length() == 0) {
                            for (Item item: itemList) {
                                gridAdapter.add(item);
                            }
                        } if (isNumeric(query)) {
                            for (Item item : itemList) {
                                if (item.getitemPrice() >= Double.parseDouble(query)) {
                                    gridAdapter.add(item);
                                }
                            }
                        }
                    }
                    gridAdapter.notifyDataSetChanged();
                    gridList.invalidateViews();
                    if (gridAdapter.getCount() == 0) {
                        no_result.setVisibility(View.VISIBLE);
                    } else {
                        no_result.setVisibility(View.GONE);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    gridAdapter.clear();
                    if (selectedFilter == null) {
                        for (Item item: itemList) {
                            gridAdapter.add(item);
                        }
                    } else if (selectedFilter.equals("Name")) {
                        for (Item item : itemList) {
                            if (item.getitemName().toLowerCase(Locale.getDefault()).contains(newText.toLowerCase(Locale.getDefault()))) {
                                gridAdapter.add(item);
                            }
                        }
                    } else if (selectedFilter.equals("Max Price")) {
                        if (newText.length() == 0) {
                            for (Item item: itemList) {
                                gridAdapter.add(item);
                            }
                        } if (isNumeric(newText)) {
                            for (Item item : itemList) {
                                if (item.getitemPrice() <= Double.parseDouble(newText)) {
                                    gridAdapter.add(item);
                                }
                            }
                        }
                    } else if (selectedFilter.equals("Min Price")) {
                        if (newText.length() == 0) {
                            for (Item item: itemList) {
                                gridAdapter.add(item);
                            }
                        } if (isNumeric(newText)) {
                            for (Item item : itemList) {
                                if (item.getitemPrice() >= Double.parseDouble(newText)) {
                                    gridAdapter.add(item);
                                }
                            }
                        }
                    }
                    gridAdapter.notifyDataSetChanged();
                    gridList.invalidateViews();
                    if (gridAdapter.getCount() == 0) {
                        no_result.setVisibility(View.VISIBLE);
                    } else {
                        no_result.setVisibility(View.GONE);
                    }
                    return false;
                }
            });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search_bar.getVisibility() == View.VISIBLE){
                    search_bar.setVisibility(View.GONE);
                } else {
                    search_bar.setVisibility(View.VISIBLE);
                }
            }
        });

        if (!((userMain) getActivity()).isFirstAddToCart()) {
            itemListCart = new ArrayList<Item>(((userMain) getActivity()).getCurrentItemList());
        }
        listAsString = new Gson().toJson(itemListCart);
        iSendDataListener.sendData(listAsString);
        return root;
    }

    private void showMenu(View v) {
        Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.music)
                    createServiceDialog(getLayoutInflater().getContext());
                if(item.getItemId() == R.id.logout)
                    requireActivity().finish();
                if(item.getItemId() == R.id.contact)
                    createContactUsDialog(getLayoutInflater().getContext());
                return true;
            }
        });
        popupMenu.show();
    }

    public void createServiceDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        final View backgroundMusicPopupView = getLayoutInflater().inflate(R.layout.music_service, null);
        dialogBuilder.setView(backgroundMusicPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        play_button = (Button) backgroundMusicPopupView.findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().startService(new Intent(requireActivity(), BackgroundMusicService.class));
            }
        });
        stop_button = (Button) backgroundMusicPopupView.findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().stopService(new Intent(requireActivity(), BackgroundMusicService.class));
            }
        });
        cancel_button = (ImageButton) backgroundMusicPopupView.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
    }

    public void createItemDetailDialog(Context context, Item item){
        dialogBuilder = new AlertDialog.Builder(context);
        final View itemDetailPopupView = getLayoutInflater().inflate(R.layout.item_detail, null);
        itemName = (TextView) itemDetailPopupView.findViewById(R.id.item_name);
        itemName.setText(item.getitemName());
        itemStock = (TextView) itemDetailPopupView.findViewById(R.id.item_stock);
        itemStock.setText("STOCK: " + (item.getitemStock()));
        itemPrice = (TextView) itemDetailPopupView.findViewById(R.id.item_price);
        itemPrice.setText("$" + (item.getitemPrice()));
        amount = (EditText) itemDetailPopupView.findViewById(R.id.amount);
        img = (ImageView) itemDetailPopupView.findViewById(R.id.imageView);
        if (item.getitemCategory().equals("GAME")) {
            img.setImageResource(R.drawable.game);
        }
        if (item.getitemCategory().equals("CONSOLE")) {
            img.setImageResource(R.drawable.console);
        }
        if (item.getitemCategory().equals("ACCESSORY")) {
            img.setImageResource(R.drawable.accessories);
        }

        if (item.getitemStock() == 0) {
            itemStock.setTextColor(Color.RED);
        }

        cancel_button = (ImageButton) itemDetailPopupView.findViewById(R.id.cancel_button);
        button_add_to_cart = (Button) itemDetailPopupView.findViewById(R.id.button_add_to_cart);
        
        dialogBuilder.setView(itemDetailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        cancel_button.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v){
              dialog.dismiss();
          }
        });
        ((userMain) getActivity()).isPurchase(isAddToCart);
        button_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNumeric(amount.getText().toString())) {
                    if (Integer.parseInt(amount.getText().toString()) <= item.getitemStock()) {
                        Toast.makeText(getContext(),  "add to cart " + amount.getText().toString()+" "+itemName.getText().toString(), Toast.LENGTH_SHORT).show();

                        ((userMain) getActivity()).isPurchase(false);
                        isAddToCart = true;
                        if (!((userMain) getActivity()).getPurchaseStatus()) {
                            String NameItem = itemName.getText().toString();
                            double PriceItem = Double.parseDouble(itemPrice.getText().toString().replaceAll("[$]", ""));
                            Item itemCart = new Item(NameItem, Integer.parseInt(amount.getText().toString()), category_title.getText().toString(), PriceItem);
                            itemListCart.add(itemCart);
                            listAsString = new Gson().toJson(itemListCart);
                            ((userMain) getActivity()).isPurchase(isAddToCart);
                            ((userMain) getActivity()).setFirstAddToCart(false);
                        }
                        iSendDataListener.sendData(listAsString);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Buy amount over stock", Toast.LENGTH_SHORT).show();
                    }
                }
                isAddToCart = false;
            }
        });
    }

    public void createContactUsDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        final View contactUsPopupView = getLayoutInflater().inflate(R.layout.contact_popup, null);
        editTextContact = (EditText) contactUsPopupView.findViewById(R.id.editTextContact);
        editTextSubject = (EditText) contactUsPopupView.findViewById(R.id.editTextSubject);
        dialogBuilder.setView(contactUsPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        contactConfirm = (Button) contactUsPopupView.findViewById(R.id.button_send);
        contactConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to="chitoan.tran@yahoo.com.vn";
                String subject = editTextSubject.getText().toString();
                String message = editTextContact.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        cancel_button = (ImageButton) contactUsPopupView.findViewById(R.id.button_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}