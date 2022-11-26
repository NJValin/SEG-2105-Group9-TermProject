package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText userName, password, firstname, lastname;
    private Button signIn, register, instructorBtn, studentBtn;
    private TextView errorMessage;
    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        signIn = (Button) findViewById(R.id.signInButton);
        errorMessage = (TextView) findViewById(R.id.loginSuccessful);
        register = (Button) findViewById(R.id.register);
        instructorBtn = (Button) findViewById(R.id.instructorBtn);
        studentBtn = (Button) findViewById(R.id.studentBtn);
        firstname = (EditText) findViewById(R.id.firstName);
        lastname = (EditText) findViewById(R.id.lastName);
        db = new DBHelper((CourseEnrollmentApp)getApplicationContext()) ;

        signIn.setOnClickListener(this);
        register.setOnClickListener(this);
        studentBtn.setOnClickListener(this);
        instructorBtn.setOnClickListener(this);
        boolean y = register("admin", "admin123", "admin", "university", "of Ottawa");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                if (isValidInput(userName.getText().toString(), password.getText().toString())) {
                    studentBtn.setVisibility(View.VISIBLE);
                    instructorBtn.setVisibility(View.VISIBLE);
                    firstname.setVisibility(View.VISIBLE);
                    lastname.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.signInButton:
                if (isValidInput(userName.getText().toString(), password.getText().toString())) {
                    boolean q = signIn(userName.getText().toString(), password.getText().toString());
                    if (q) {
                        toHomePage();
                    }
                    else {
                        errorMessage.setText("Incorrect username/password");
                        
                    }

                }
                break;
            case R.id.studentBtn:
                if (firstname.getText().toString().equals("")||lastname.getText().toString().equals("")) {
                    errorMessage.setText("Please enter your first and last name.");
                }

                else if (register(userName.getText().toString(), password.getText().toString(), "student", firstname.getText().toString(), lastname.getText().toString())) {
                    instructorBtn.setVisibility(view.GONE);
                    studentBtn.setVisibility(view.GONE);
                    firstname.setVisibility(View.GONE);
                    lastname.setVisibility(View.GONE);
                    toHomePage();
                    userName.setText("");
                    password.setText("");
                    firstname.setText("");
                    lastname.setText("");
                }
                else {
                    errorMessage.setText("Username/password already in use.");
                    instructorBtn.setVisibility(view.GONE);
                    studentBtn.setVisibility(view.GONE);
                    firstname.setVisibility(View.GONE);
                    lastname.setVisibility(View.GONE);
                    userName.setText("");
                    password.setText("");
                    firstname.setText("");
                    lastname.setText("");
                }
                break;
            case R.id.instructorBtn:
                if (firstname.getText().toString().equals("")||lastname.getText().toString().equals("")) {
                    errorMessage.setText("Please enter your first and last name.");
                }
                else if (register(userName.getText().toString(), password.getText().toString(), "instructor", firstname.getText().toString(), lastname.getText().toString())) {
                    toHomePage();
                    instructorBtn.setVisibility(view.GONE);
                    studentBtn.setVisibility(view.GONE);

                }
                else {
                    instructorBtn.setVisibility(view.GONE);
                    studentBtn.setVisibility(view.GONE);
                    errorMessage.setText("Username/password already in use.");
                    firstname.setVisibility(View.GONE);
                    lastname.setVisibility(View.GONE);
                    userName.setText("");
                    password.setText("");
                    firstname.setText("");
                    lastname.setText("");
                }

                break;
        }

    }
    public boolean isValidInput(String username, String password) {
        boolean toReturn = true;
        if (username.equals("")||password.equals("")) {
            errorMessage.setText("Please enter a valid username or password");
            userName.setText("");
            this.password.setText("");
            toReturn=false;
        }
        return toReturn;
    }
    public boolean register(String username, String password, String userType, String firstName, String lastName) {
        boolean x = db.addUsers(username, password, userType, firstName, lastName);
        return x;
    }
    public boolean signIn(String username, String password) {
        boolean x = db.checkLogin(username, password);
        return x;
    }
    private void toHomePage() {
        String userType = db.getUserType(userName.getText().toString());
        if (userType.equals("admin")) {
            Intent i = new Intent(this, AdminHomePage.class);
            i.putExtra("username", userName.getText().toString());
            userName.setText("");
            this.password.setText("");
            firstname.setText("");
            lastname.setText("");
            startActivity(i);
        }
        else if (userType.equals("student")) {
            Intent d = new Intent(this, StudentHomePage.class);
            d.putExtra("username", userName.getText().toString());
            firstname.setVisibility(View.GONE);
            lastname.setVisibility(View.GONE);
            userName.setText("");
            this.password.setText("");
            firstname.setText("");
            lastname.setText("");
            errorMessage.setText("");
            startActivity(d);
        }
        else {
            Intent k = new Intent(this, InstructorHomePage.class);
            k.putExtra("username", userName.getText().toString());
            userName.setText("");
            firstname.setVisibility(View.GONE);
            lastname.setVisibility(View.GONE);
            this.password.setText("");
            firstname.setText("");
            lastname.setText("");
            startActivity(k);
        }
    }
}