package com.example.seg2105_termprojectcourseenrollmentapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.seg2105_termprojectcourseenrollmentapp.databinding.ActivityHomePageBinding;

import java.util.ArrayList;

public class InstructorHomePage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomePageBinding binding;
    private TextView welcomeInstructor, errorMsg;
    private Button teachCourse, toInstructorCourseEdit, confirmSearch;
    private DBHelper db;
    private String[] name;
    private EditText crsCode, crsName;
    private ListView courseList;
    private ArrayList<String> courses;
    private ArrayAdapter<String> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_home_page);
        welcomeInstructor = (TextView) findViewById(R.id.welcomeInstructor);
        teachCourse = (Button) findViewById(R.id.teachCourse);
        toInstructorCourseEdit = (Button) findViewById(R.id.toTeachersEdit);
        confirmSearch = (Button) findViewById(R.id.searchButton);
        crsCode = (EditText) findViewById(R.id.crsCodeSearch);
        crsName = (EditText) findViewById(R.id.crsNameSearch);
        courseList = (ListView) findViewById(R.id.courseList);
        errorMsg = (TextView) findViewById(R.id.errorMessage);
        courses = new ArrayList<>();
        String code;
        String crsname;
        db = new DBHelper((CourseEnrollmentApp)getApplicationContext());
        Bundle extras = getIntent().getExtras();
        name = db.getName(extras.getString("username"));
        welcomeInstructor.setText("Welcome "+name[0]+" "+name[1]+"! you are logged in as Instructor");
        teachCourse.setOnClickListener(this::onClick);
        toInstructorCourseEdit.setOnClickListener(this::onClick);
        confirmSearch.setOnClickListener(this::onClick);
        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = courseList.getItemAtPosition(i);
                if (o.toString().equals("")) {
                    return;
                }
                String code = o.toString().split(": ")[0];
                String crsname = o.toString().split(": ")[1];
                selectCourse(code, crsname, o);
            }
        });
        displayCourses();
    }
    private void selectCourse(String code, String name, Object o) {
        teachCourse.setVisibility(View.VISIBLE);
        teachCourse.setClickable(true);
        crsName.setText(name);
        crsCode.setText(code);
        courses.clear();
        courses.add(o.toString());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        courseList.setAdapter(adapter);
    }
    private void displayCourses(){
        courses.clear();
        String[] x = db.courseListForInstructor();
        for (int i =0; i<x.length; i++) {
            courses.add(x[i]);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        courseList.setAdapter(adapter);
    }
    //format the teachers name like firstname+" "+lastname, the courseListOfTeacher() method depends on it
    private void courseTeacher(String code, String crsname) {
        db.setInstructor(crsname, code, name);
    }
    private void toCourseEdit(){
        Intent w = new Intent(this, InstructorCourseEdit.class);
        startActivity(w);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toTeachersEdit:
                String[] x = db.courseListOfTeacher(name);
                if (x.length==1&&x[0].equals("")) {
                    errorMsg.setText("You don't teach any classes at this time.");
                    teachCourse.setVisibility(View.INVISIBLE);
                    displayCourses();
                }
                else{
                    toCourseEdit();
                }
                break;
            case R.id.teachCourse:
                courseTeacher(crsName.getText().toString(),crsCode.getText().toString());
                break;
            case R.id.searchButton:
                String courseName = crsName.getText().toString();
                String courseCode = crsCode.getText().toString();
                boolean exists = db.courseExists(courseCode,courseName);
                if (courseCode.equals("")||courseName.equals("")) {
                    crsName.setText("");
                    crsCode.setText("");
                    displayCourses();
                    errorMsg.setText("Invalid Course code/name");
                }
                else if (exists) {
                    courses.clear();
                    courses.add(db.getCourse(courseCode,courseName));
                    errorMsg.setText("");
                    adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
                    courseList.setAdapter(adapter);
                }
                else {
                    crsName.setText("");
                    crsCode.setText("");
                    displayCourses();
                    errorMsg.setText("Invalid Course code/name");
                }
        }
    }


}