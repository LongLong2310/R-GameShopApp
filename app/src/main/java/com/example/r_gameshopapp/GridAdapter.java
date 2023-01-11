package com.example.r_gameshopapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.r_gameshopapp.databinding.FragmentHomeItemBinding;

import java.util.ArrayList;
import java.util.Locale;

public class GridAdapter extends ArrayAdapter {
    Context context;
    String[] itemName;
    int[] itemStock;
    String[] itemCategory;
    int[] itemPrice;
    int[] image;

    ArrayList<Item> itemList;
    ArrayList<Item> itemFilteredList;


    public GridAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        itemList = objects;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_home_item, null);
        TextView itemNameView = (TextView) v.findViewById(R.id.item_name);
        TextView itemStockView = (TextView) v.findViewById(R.id.item_stock);
        TextView itemCategoryView = (TextView) v.findViewById(R.id.item_category);
        TextView itemPriceView = (TextView) v.findViewById(R.id.item_price);
        ImageView itemImageView = (ImageView) v.findViewById(R.id.item_image);

        itemNameView.setText(itemList.get(position).getitemName());
        itemStockView.setText("Stock:  " + itemList.get(position).getitemStock());
        itemCategoryView.setText(itemList.get(position).getitemCategory());
        itemPriceView.setText("$" + itemList.get(position).getitemPrice());
        if (itemList.get(position).getitemCategory().equals("GAME")) {
            itemImageView.setImageResource(R.drawable.game);
        }
        if (itemList.get(position).getitemCategory().equals("CONSOLE")) {
            itemImageView.setImageResource(R.drawable.console);
        }
        if (itemList.get(position).getitemCategory().equals("ACCESSORY")) {
            itemImageView.setImageResource(R.drawable.accessories);
        }

        if (itemList.get(position).getitemStock() == 0) {
            itemStockView.setTextColor(Color.RED);
        }
        return v;
    }
}
