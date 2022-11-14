package com.example.seg2105_termprojectcourseenrollmentapp;

import android.annotation.SuppressLint;
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

import android.os.Bundle;

import com.example.seg2105_termprojectcourseenrollmentapp.databinding.ActivityHomePageBinding;

public class InstructorHomePage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomePageBinding binding;
    private TextView welcomeInstructor, instruction;
    private Button toStudents, toTeachingAssistants;
    private DBHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_home_page);
        welcomeInstructor = (TextView) findViewById(R.id.welcomeInstructor);
        instruction = (TextView) findViewById(R.id.instruction);
        toStudents = (Button) findViewById(R.id.studentsButton);
        toTeachingAssistants = (Button) findViewById(R.id.assistTeachButton);
        db = new DBHelper((CourseEnrollmentApp)getApplicationContext());
        Bundle extras = getIntent().getExtras();
        String[] name = db.getName(extras.getString("username"));
        welcomeInstructor.setText("Welcome "+name[0]+" "+name[1]+"! you are logged in as Instructor");
        toStudents.setOnClickListener(this::onClick);
    }

    private void toStudentsSearch() {
        Intent q = new Intent(this, AdminUserSearch.class);
        startActivity(q);
    }

    public void onClick(View view) {
        String oldcode;
        String oldName;
        switch(view.getId()){
            case R.id.usersButton:
                toStudentsSearch();
                break;

        }


}