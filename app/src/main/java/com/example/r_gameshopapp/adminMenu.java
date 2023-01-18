package com.example.r_gameshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class adminMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_admin_menu);

        Button buttonS = (Button) findViewById(R.id.buttonStock);
        Button buttonH = (Button) findViewById(R.id.buttonHistory);
        Button buttonA = (Button) findViewById(R.id.buttonAccount);
        Button buttonL = (Button) findViewById(R.id.buttonLogout);


        buttonS.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Intent i = new Intent(adminMenu.this, stockScreen.class);
                startActivity(i);
            }
        }));
        buttonH.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Intent i = new Intent(adminMenu.this, adminHistory.class);
                startActivity(i);
            }
        }));
        buttonA.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Intent i = new Intent(adminMenu.this, accountScreen.class);
                startActivity(i);
            }
        }));
        buttonL.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                finish();
            }
        }));

    }
}