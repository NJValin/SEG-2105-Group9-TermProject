package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText userName, password;
    private Button signIn, register;
    private TextView errorMessage;
    private DBHelper db;
    private boolean validAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        signIn = (Button) findViewById(R.id.signInButton);
        errorMessage = (TextView) findViewById(R.id.loginSuccessful);
        register = (Button) findViewById(R.id.register);

        db = new DBHelper(this);
        signIn.setOnClickListener(this);
        register.setOnClickListener(this);
        db.addUsers("admin", "admin123", "admin");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:

                if (isValidInput(userName.getText().toString(), password.getText().toString())) {
                    if (register(userName.getText().toString(), password.getText().toString(), null)) {
                           toHomePage();
                    }
                    else {
                        errorMessage.setText("Username/password already in use.");
                        userName.setText("");
                        password.setText("");
                    }
                }
                break;
            case R.id.signInButton:
                if (isValidInput(userName.getText().toString(), password.getText().toString())) {

                }
                break;
        }
        String usrname = userName.getText().toString();
        String passWord = password.getText().toString();
        if (usrname.equals("")||passWord.equals("")) {
            errorMessage.setText("Please enter a valid username or password");
            userName.setText("");
            password.setText("");
        }
        else if (db.checkLogin(usrname, passWord)) {
            validAccount=true;

        }
        else {
            validAccount=false;
            errorMessage.setText("User name/password isn't valid");
            userName.setText("");
            password.setText("");
        }
    }
    private boolean isValidInput(String username, String password) {
        boolean toReturn = true;
        if (username.equals("")||password.equals("")) {
            errorMessage.setText("Please enter a valid username or password");
            userName.setText("");
            this.password.setText("");
            toReturn=false;
        }
        return toReturn;
    }
    private boolean register(String username, String password, String userType) {
        boolean x = db.addUsers(username, password, null);
        return x;
    }
    private void signIn(String username, String password) {

    }
    private void toHomePage() {
        String userType = db.getUserType(userName.getText().toString());
        if (userType.equals("admin")) {
            startActivity(new Intent(this, AdminHomePage.class));
        }
    }
}