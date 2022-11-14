package com.example.seg2105_termprojectcourseenrollmentapp;

import android.app.Application;
import android.content.Intent;
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
    private Button toUsers, toCourses;
    private DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        wlcmAdminmessage = (TextView) findViewById(R.id.welcomeAdmin);
        toUsers = (Button) findViewById(R.id.usersButton);
        toCourses = (Button) findViewById(R.id.coursesButtons);
        db = new DBHelper((CourseEnrollmentApp)getApplicationContext());
        Bundle extras = getIntent().getExtras();
        String[] name = db.getName(extras.getString("username"));
        wlcmAdminmessage.setText("Welcome "+name[0]+" "+name[1]+"! you are logged in as admin");
        toUsers.setOnClickListener(this::onClick);
        toCourses.setOnClickListener(this::onClick);
    }
    private void toUsersSearch() {
        Intent q = new Intent(this, AdminUserSearch.class);
        startActivity(q);
    }
    private void toCourseCreate() {
        Intent w = new Intent(this, AdminCourseEdit.class);
        startActivity(w);
    }

    public void onClick(View view) {
        String oldcode;
        String oldName;
        switch(view.getId()){
            case R.id.coursesButtons:
                toCourseCreate();
                break;
            case R.id.usersButton:
                toUsersSearch();
                break;
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}