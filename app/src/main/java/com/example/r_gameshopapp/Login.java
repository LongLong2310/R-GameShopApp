package com.example.r_gameshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void onClickButtonToSignUp(View view) {
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }
}
