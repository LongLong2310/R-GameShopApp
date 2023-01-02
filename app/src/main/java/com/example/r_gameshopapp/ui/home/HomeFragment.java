package com.example.r_gameshopapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r_gameshopapp.GridAdapter;
import com.example.r_gameshopapp.Item;
import com.example.r_gameshopapp.R;
import com.example.r_gameshopapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

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
        itemList.add(new Item(1, "Pokemon Violet", 7, "GAME", 59.99, R.drawable.game));
        itemList.add(new Item(2, "DualShock 4 PS4", 2, "CONSOLE", 79.99, R.drawable.console));
        itemList.add(new Item(3, "Razor Headset", 0, "ACCESSORIES", 29.99, R.drawable.accessories));

        GridAdapter gridAdapter = new GridAdapter(context, R.layout.activity_main_item, itemList);
        gridList.setAdapter(gridAdapter);
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}