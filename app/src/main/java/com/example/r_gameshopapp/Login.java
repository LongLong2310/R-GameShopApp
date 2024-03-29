package com.example.r_gameshopapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbManager = new DatabaseManager(this);
    }


    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.editTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
            Intent i = new Intent(Login.this, adminMenu.class);
            startActivity(i);

        } else {
            if (dbManager.checkUser(username.getText().toString(), password.getText().toString())) {
                Cursor cursor = dbManager.loginInfo(username.getText().toString());
                Toast.makeText(this, "Welcome user " + cursor.getString(1) + "!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, userMain.class);
                i.putExtra("id", cursor.getInt(0));
                startActivity(i);

            } else {
                Toast.makeText(this, "wrong password or username", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }

    public void onClickButtonToSignUp(View view) {
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }
}
