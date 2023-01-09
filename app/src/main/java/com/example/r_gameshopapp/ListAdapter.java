package com.example.r_gameshopapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.r_gameshopapp.databinding.FragmentCartItemBinding;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter {
    Context context;
    String[] itemName;
    int[] itemStock;
    String[] itemCategory;
    int[] itemPrice;
    int[] image;

    ArrayList<Item> cartList = new ArrayList<>();

    public ListAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        cartList = objects;
    }

    public ListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_cart_item, null);
        TextView itemNameView = (TextView) v.findViewById(R.id.item_name);
        TextView itemStockView = (TextView) v.findViewById(R.id.item_stock);
        TextView itemPriceView = (TextView) v.findViewById(R.id.item_price);
        ImageView itemImageView = (ImageView) v.findViewById(R.id.item_image);

        itemNameView.setText(cartList.get(position).getitemName());
        itemStockView.setText("x  " + cartList.get(position).getitemStock());
        itemPriceView.setText("$" + cartList.get(position).getitemPrice());
        if (cartList.get(position).getitemCategory().equals("GAME")) {
            itemImageView.setImageResource(R.drawable.game);
        }
        if (cartList.get(position).getitemCategory().equals("CONSOLE")) {
            itemImageView.setImageResource(R.drawable.console);
        }
        if (cartList.get(position).getitemCategory().equals("ACCESSORY")) {
            itemImageView.setImageResource(R.drawable.accessories);
        }

        if (cartList.get(position).getitemStock() == 0) {
            itemStockView.setTextColor(Color.RED);
        }

        return v;

    }
}
