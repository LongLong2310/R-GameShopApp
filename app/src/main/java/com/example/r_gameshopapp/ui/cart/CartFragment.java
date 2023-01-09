package com.example.r_gameshopapp.ui.cart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r_gameshopapp.GridAdapter;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.ListAdapter;
import com.example.r_gameshopapp.R;
import com.example.r_gameshopapp.databinding.FragmentCartBinding;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    ListView cartList;
    ArrayList<Item> itemCartList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel notificationsViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);
        Context context = inflater.getContext();

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cartList = root.findViewById(R.id.cart_list);
        itemCartList.add(new Item(1, "Pokemon", 3, "GAME", 69.99));
        itemCartList.add(new Item(2, "Pokemon", 3, "GAME", 69.99));
        itemCartList.add(new Item(3, "Pokemon", 3, "GAME", 69.99));
        itemCartList.add(new Item(4, "Pokemon", 3, "GAME", 69.99));
        itemCartList.add(new Item(5, "Pokemon", 3, "GAME", 69.99));

        ListAdapter listAdapter = new ListAdapter(context, R.layout.fragment_cart_item, itemCartList);
        cartList.setAdapter(listAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}