package com.example.r_gameshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter {
    Context context;
    String[] itemName;
    int[] itemStock;
    String[] itemCategory;
    int[] itemPrice;
    int[] image;

    ArrayList<Item> itemList = new ArrayList<>();

    public GridAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        itemList = objects;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_main_item, null);
        TextView itemNameView = (TextView) v.findViewById(R.id.item_name);
        TextView itemStockView = (TextView) v.findViewById(R.id.item_stock);
        TextView itemCategoryView = (TextView) v.findViewById(R.id.item_category);
        TextView itemPriceView = (TextView) v.findViewById(R.id.item_price);
        ImageView itemImageView = (ImageView) v.findViewById(R.id.item_image);

        itemNameView.setText(itemList.get(position).getitemName());
        itemStockView.setText(Integer.toString(itemList.get(position).getiemStock()));
        itemCategoryView.setText(itemList.get(position).getitemCategory());
        itemPriceView.setText("$" + itemList.get(position).getitemPrice());
        itemImageView.setImageResource(itemList.get(position).getitemImage());
        return v;

    }
}
