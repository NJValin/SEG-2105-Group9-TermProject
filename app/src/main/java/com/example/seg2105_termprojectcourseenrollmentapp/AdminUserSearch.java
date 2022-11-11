package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminUserSearch extends AppCompatActivity {
    private DBHelper db;
    private EditText userSearchBar;
    private Button deleteUser, returnToHome, searchButton;
    private TextView errorMessage;
    private ListView userListView;
    private String userToDelete;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_search);
        db = new DBHelper((CourseEnrollmentApp)getApplicationContext());
        userSearchBar = (EditText) findViewById(R.id.userNameToSearch);
        deleteUser =  (Button) findViewById(R.id.confirmDelete);
        returnToHome = (Button) findViewById(R.id.returnToHome);
        searchButton = (Button) findViewById(R.id.searchBtn);
        userListView = (ListView) findViewById(R.id.listOfUsers);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        deleteUser.setOnClickListener(this::onClick);
        returnToHome.setOnClickListener(this::onClick);
        searchButton.setOnClickListener(this::onClick);
        userToDelete ="";
        userList = new ArrayList<>();
        displayAllUsers();
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = userListView.getItemAtPosition(i);
                userToDelete = o.toString().split(": ")[0];
                deleteUser.setVisibility(View.VISIBLE);
                deleteUser.setClickable(true);
            }
        });

    }
    private void displayAllUsers() {
        userList.clear();
        String[] x = db.userList();
        userList.addAll(Arrays.asList(x));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(adapter);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.returnToHome:
                returnHome();
                break;
            case R.id.confirmDelete:
                boolean deleteworked = deleteUser(userToDelete);
                if (deleteworked) {
                    userSearchBar.setText("");
                    deleteUser.setVisibility(View.INVISIBLE);
                    deleteUser.setClickable(false);
                    errorMessage.setText("");
                    userToDelete="";
                    displayAllUsers();
                }
                else {
                    userSearchBar.setText("");
                    deleteUser.setVisibility(View.INVISIBLE);
                    deleteUser.setClickable(false);
                    userToDelete="";
                    errorMessage.setText("Deletion failed");
                }
                break;
            case R.id.searchBtn:
                String x = userSearchBar.getText().toString();
                boolean exists = db.userExists(x);
                if (x.equals("")) {
                    userSearchBar.setText("");
                    displayAllUsers();
                    errorMessage.setText("Invalid username");
                }
                else if (exists) {
                    userList.clear();
                    userList.add(db.getUser(x));
                    errorMessage.setText("");
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
                    userListView.setAdapter(adapter);
                }
                else {
                    userSearchBar.setText("");
                    displayAllUsers();
                    errorMessage.setText("Invalid username");
                }
        }
    }
    private void returnHome() {
        finish();
    }
    private boolean deleteUser(String username) {
        return db.removeUser(username);
    }
}