package com.example.r_gameshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        EditText username = (EditText) findViewById(R.id.editTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        EditText confirm = (EditText) findViewById(R.id.editTextConfirm);
      //  if (isValidPassword(password.getText().toString()) == false) {
       //     Toast.makeText(this, "Must have uppercase, lowercase and number, length 8-20", Toast.LENGTH_SHORT).show();
      //  } else {
            if (dbManager.checkUser(username.getText().toString()) == false && confirm.getText().toString().equals(password.getText().toString())) {
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
   // }

    // passwordMust have uppercase, lowercase and number, length 8-20
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
