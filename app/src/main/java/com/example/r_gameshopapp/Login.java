package com.example.r_gameshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }



    public void login(View view) {
        EditText username = (EditText)findViewById(R.id.editTextUsername);
        EditText password = (EditText)findViewById(R.id.editTextPassword);
        if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
            Intent i = new Intent(Login.this, adminMenu.class);
            startActivity(i);

        } else {
            Toast.makeText(this, "wrong password or username", Toast.LENGTH_SHORT).show();
        }
    }
      

    public void onClickButtonToSignUp(View view) {

        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }
}
