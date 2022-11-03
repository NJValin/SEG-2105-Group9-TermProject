package com.example.seg2105_termprojectcourseenrollmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.seg2105_termprojectcourseenrollmentapp.databinding.ActivityHomePageBinding;

import org.w3c.dom.Text;

public class AdminHomePage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomePageBinding binding;
    private TextView wlcmAdminmessage;
    private Button createClass, deleteClass, edtClass, searchUsers;
    private EditText searchforUsers, newClassCode, newClassName;
    private DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        wlcmAdminmessage = (TextView) findViewById(R.id.welcomeAdmin);
        createClass = (Button) findViewById(R.id.createClass);
        deleteClass = (Button) findViewById(R.id.deleteClass);
        edtClass = (Button) findViewById(R.id.editClass);
        searchUsers = (Button) findViewById(R.id.searchUsers);
        searchforUsers = (EditText) findViewById(R.id.searchBar);
        newClassCode = (EditText) findViewById(R.id.newCourseCode);
        newClassName = (EditText) findViewById(R.id.newNameBar);
        db = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        String[] name = db.getName(extras.getString("username"));
        wlcmAdminmessage.setText("Welcome "+name[0]+" "+name[1]+"! you are logged in as admin");
    }

    public void onClick(View view) {

    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}