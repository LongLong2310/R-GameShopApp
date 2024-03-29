package com.example.r_gameshopapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;

import com.example.r_gameshopapp.ui.cart.CartFragment;
import com.example.r_gameshopapp.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.r_gameshopapp.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class userMain extends AppCompatActivity implements HomeFragment.ISendDataListener {

    private ActivityMainBinding binding;
    private String test = "";
    private boolean isPurchase = false;
    private boolean isFirstAddToCart = true;
    private List<Item> currentItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, BackgroundMusicService.class));
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        int i = bundle.getInt("id");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_account, R.id.navigation_cart)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


    public int getid() {
        Bundle bundle = getIntent().getExtras();
        int i = bundle.getInt("id");
        return i;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BackgroundMusicService.class));
        super.onDestroy();
    }

    public String getTest() {
        return test;
    }

    public void isPurchase(boolean purchaseStatus) {
        isPurchase = purchaseStatus;
    }

    @Override
    public void sendData(String string) {
        test = string;
    }

    public boolean getPurchaseStatus() {
        return isPurchase;
    }

    public List<Item> getCurrentItemList() {
        return currentItemList;
    }

    public void setCurrentItemList(List<Item> currentItemList) {
        this.currentItemList = currentItemList;
    }

    public boolean isFirstAddToCart() {
        return isFirstAddToCart;
    }

    public void setFirstAddToCart(boolean firstAddToCart) {
        isFirstAddToCart = firstAddToCart;
    }
}