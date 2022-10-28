package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userName, password;
    Button signIn;
    TextView successfulSignIn;
    DBHelper db;
    boolean validAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        signIn = (Button) findViewById(R.id.signInButton);
        successfulSignIn = (TextView) findViewById(R.id.loginSuccessful);

        db = new DBHelper(this);
        signIn.setOnClickListener(this);

        db.addUsers("admin", "admin123", "admin");
    }

    @Override
    public void onClick(View view) {
        String usrname = userName.getText().toString();
        String passWord = password.getText().toString();
        if (usrname.equals("")||passWord.equals("")) {
            successfulSignIn.setText("Please enter a valid username or password");
            userName.setText("");
            password.setText("");
        }
        else if (db.checkLogin(usrname, passWord)) {
            validAccount=true;

        }
    }
}