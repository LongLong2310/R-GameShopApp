package com.example.r_gameshopapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r_gameshopapp.BackgroundMusicService;
import com.example.r_gameshopapp.DatabaseManager;
import com.example.r_gameshopapp.GridAdapter;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.databinding.FragmentHomeBinding;
import com.example.r_gameshopapp.userMain;
import com.example.r_gameshopapp.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView itemName, itemStock, itemPrice, amount, category_title;
    private ImageButton button_cancel;
    private Button  button_add_to_cart, game_category_button,
                    console_category_button, accessories_category_button,
                    all_category_button, play_button, stop_button;
    private ImageView img, more_button;
    private DatabaseManager dbManager;
    private Button buttonGame;

    private String selected_category = "ALL";
    ArrayList<Item> displayList = new ArrayList<>();

    private FragmentHomeBinding binding;
    GridView gridList;
    ArrayList<Item> itemList = new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Context context = inflater.getContext();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gridList = (GridView) root.findViewById(R.id.gridView);
        more_button = (ImageView) root.findViewById(R.id.more_button);
        dbManager = new DatabaseManager(getActivity());
        itemList = dbManager.getAllItem();

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
            }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

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
                return true;
            }
        });
        popupMenu.show();
    }

    public void createServiceDialog(Context context) {
        dialogbuilder = new AlertDialog.Builder(context);
        final View backgroundMusicPopupView = getLayoutInflater().inflate(R.layout.music_service, null);
        dialogbuilder.setView(backgroundMusicPopupView);
        dialog = dialogbuilder.create();
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
        button_cancel = (ImageButton) backgroundMusicPopupView.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
    }

    public void createItemDetailDialog(Context context, Item item){
        dialogbuilder = new AlertDialog.Builder(context);
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

        button_cancel = (ImageButton) itemDetailPopupView.findViewById(R.id.button_cancel);
        button_add_to_cart = (Button) itemDetailPopupView.findViewById(R.id.button_add_to_cart);
        
        dialogbuilder.setView(itemDetailPopupView);
        dialog = dialogbuilder.create();
        dialog.show();

        button_cancel.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v){
              dialog.dismiss();
          }
        });
        button_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNumeric(amount.getText().toString()) == true) {
                    if (Integer.parseInt(amount.getText().toString()) <= item.getitemStock()) {
                        Toast.makeText(getContext(), "Account " + ((userMain) getActivity()).getid() + " add to cart " + amount.getText().toString(), Toast.LENGTH_SHORT).show();
                        //dbManager.buy(item.getitemName(),item.getitemStock()-Integer.parseInt(amount.getText().toString()));
                    } else {
                        Toast.makeText(getContext(), "buy amount over stock", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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