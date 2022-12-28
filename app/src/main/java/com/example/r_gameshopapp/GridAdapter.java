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
    int[] image;

    ArrayList<Item> itemList = new ArrayList<Item>();

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
        TextView textView = (TextView) v.findViewById(R.id.item_name);
        ImageView imageView = (ImageView) v.findViewById(R.id.grid_image);
        textView.setText(itemList.get(position).getitemName());
        imageView.setImageResource(itemList.get(position).getitemImage());
        return v;

    }
}
