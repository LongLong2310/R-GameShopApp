package com.example.r_gameshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbManager = new DatabaseManager(this);
        dbManager.open();
    }

    public void Signup(View view) {

        EditText username = (EditText)findViewById(R.id.editTextUsername);
        EditText password = (EditText)findViewById(R.id.editTextPassword);
        EditText confirm = (EditText)findViewById(R.id.editTextConfirm);
        if (dbManager.checkUser(username.getText().toString())==false && confirm.getText().toString().equals(password.getText().toString())) {
            dbManager.insertAccount(
                    username.getText().toString(),
                    password.getText().toString(),
                    0);
            dbManager.close();
            finish();

        } else {
            Toast.makeText(this, "name must be unique and password and confirm must be the same", Toast.LENGTH_SHORT).show();
        }
    }
}
