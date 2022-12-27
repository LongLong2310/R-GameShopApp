package com.example.r_gameshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class adminMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        Button buttonS = (Button)findViewById(R.id.buttonStock);
        Button buttonH = (Button)findViewById(R.id.buttonHistory);
        Button buttonA = (Button)findViewById(R.id.buttonAccount);
        Button buttonL = (Button)findViewById(R.id.buttonLogout);


        buttonS.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
                public final void onClick(View it) {
                    Toast.makeText((Context)adminMenu.this, "Stock", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(adminMenu.this, stockScreen.class);
                    startActivity(i);
                }
            }));
        buttonH.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Toast.makeText((Context)adminMenu.this, "History", Toast.LENGTH_SHORT).show();
            }
        }));
        buttonA.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                Toast.makeText((Context)adminMenu.this, "Account", Toast.LENGTH_SHORT).show();
            }
        }));
        buttonL.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
               finish();
            }
        }));

    }
}