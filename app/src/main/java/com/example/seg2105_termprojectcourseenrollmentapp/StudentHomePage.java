package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentHomePage extends AppCompatActivity {
    private TextView wlcm, errorMsg;
    private Button back, enroll, toEnrolledCourses, search;
    private ListView courses,enrolledCourses;;
    private String[] name;
    private EditText crsCSearch, crsNSearch;
    private DBHelper db;
    //private DBHelper db2;
    private String crsName, crsCode, username;
    private ArrayList<String> course;
    private ArrayList<String> enrolledCourse;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);
        //declaring UI fields
        wlcm = (TextView) findViewById(R.id.welcomeStudent);
        errorMsg =  (TextView) findViewById(R.id.error);
        back = (Button) findViewById(R.id.backBtn);
        enroll = (Button) findViewById(R.id.enroll);
        toEnrolledCourses = (Button) findViewById(R.id.goToUsersClasses);
        courses = (ListView) findViewById(R.id.courseList);
        search = (Button) findViewById(R.id.search);
        crsNSearch  = (EditText) findViewById(R.id.crsNSearch);
        crsCSearch = (EditText) findViewById(R.id.crsCSearch);
        //other private vars
        crsName=crsCode="";
        db = new DBHelper((CourseEnrollmentApp)getApplicationContext());
        Bundle extras = getIntent().getExtras();
        course = new ArrayList<>();
        name = db.getName(extras.getString("username"));
        username = extras.getString("username");
        wlcm.setText("Welcome "+name[0]+" "+name[1]+"! you are logged in as a student");
        displayCourses();
        courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = courses.getItemAtPosition(i);
                if (o.toString().equals("No Courses at the moment.")) {
                    return;
                }
                crsCode = o.toString().split(": ")[0];
                crsName = o.toString().split(": ")[1];
                selectItem(o);
            }


        });

        back.setOnClickListener(this::onClick);
        enroll.setOnClickListener(this::onClick);
        toEnrolledCourses.setOnClickListener(this::onClick);
        search.setOnClickListener(this::onClick);

    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                reset();
                break;
            case R.id.goToUsersClasses:

                Intent intent = new Intent(getApplicationContext(), StudentCourses.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            case R.id.enroll: // db.validateEnrollment(crsCode,username) && db.enroll(crsCode,crsName,username)
                errorMsg.setText(crsCode+": "+crsName+", "+username);
                boolean isValid = validate(crsCode, username);
                if (isValid) {
                    boolean x = db.enroll(crsCode, crsName, username);
                    if (x) {
                        reset();
                    }
                    else {
                        errorMsg.setText("Failed to enrol");
                        displayCourses();
                        crsCSearch.setText("");
                        crsNSearch.setText("");
                        crsName = crsCode = "";
                        enroll.setVisibility(View.INVISIBLE);
                        toEnrolledCourses.setVisibility(View.VISIBLE);
                        crsNSearch.setVisibility(View.VISIBLE);
                        crsCSearch.setVisibility(View.VISIBLE);
                        search.setVisibility(View.VISIBLE);
                        wlcm.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    errorMsg.setText("Time conflict or other issue - Can't enrol");
                    displayCourses();
                    crsCSearch.setText("");
                    crsNSearch.setText("");
                    crsName = crsCode = "";
                    enroll.setVisibility(View.INVISIBLE);
                    toEnrolledCourses.setVisibility(View.VISIBLE);
                    crsNSearch.setVisibility(View.VISIBLE);
                    crsCSearch.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    wlcm.setVisibility(View.VISIBLE);
                }


                break;

            case R.id.search:
                String courseName = crsNSearch.getText().toString();
                String courseCode = crsCSearch.getText().toString();
                boolean exists = db.courseExists(courseCode,courseName);

                if (courseCode.equals("")&&courseName.equals("")) {

                    crsNSearch.setText("");
                    crsCSearch.setText("");
                    displayCourses();
                    errorMsg.setText("Invalid Course code/name");
                }
                else {
                    ArrayList<String> temp;
                    if (!courseCode.equals("")&&courseName.equals("")) {
                        temp = db.searchCourseByCode(courseCode);
                        if (temp.size()!=0) {
                            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temp);
                            courses.setAdapter(adapter);
                            errorMsg.setText("");
                        }
                        else {
                            crsNSearch.setText("");
                            crsCSearch.setText("");
                            displayCourses();
                            errorMsg.setText("Invalid Course code/name");
                        }
                    }
                    else if (courseCode.equals("")&&!courseName.equals("")) {
                        temp  = db.searchCourseByName(courseName);
                        if (temp.size()!=0) {
                            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temp);
                            courses.setAdapter(adapter);
                            errorMsg.setText("");
                        }
                        else {
                            crsNSearch.setText("");
                            crsCSearch.setText("");
                            displayCourses();
                            errorMsg.setText("Invalid Course code/name");
                        }
                    }
                    else {
                        if (exists) {
                            temp  =db.searchCourse(courseCode, courseName);
                            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temp);
                            courses.setAdapter(adapter);
                            errorMsg.setText("");
                        }
                        else {
                            crsNSearch.setText("");
                            crsCSearch.setText("");
                            displayCourses();
                            errorMsg.setText("Invalid Course code/name");
                        }
                    }
                }
                break;
        }
    }
    private void reset() {
        displayCourses();
        errorMsg.setText("");
        crsCSearch.setText("");
        crsNSearch.setText("");
        crsName = crsCode = "";
        enroll.setVisibility(View.INVISIBLE);
        toEnrolledCourses.setVisibility(View.VISIBLE);
        crsNSearch.setVisibility(View.VISIBLE);
        crsCSearch.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        wlcm.setVisibility(View.VISIBLE);
    }
    private boolean validate(String crsCode, String username) {
        return db.validateEnrollment(crsCode, username);
    }
    private void selectItem(Object o) {
        course.clear();
        course.add(o.toString());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, course);
        courses.setAdapter(adapter);
        enroll.setVisibility(View.VISIBLE);
        crsNSearch.setVisibility(View.INVISIBLE);
        crsCSearch.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
        back.setVisibility(View.VISIBLE);
        wlcm.setVisibility(View.INVISIBLE);
    }
    private void displayCourses() {
        course.clear();
        String[] x = db.courseListForInstructor();
        if (x.length==1&&x[0].equals("")) {
            course.add("No Courses at the moment.");
            return;
        }

        for (String q : x) {
            course.add(q);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, course);
        courses.setAdapter(adapter);
    }

    private void search(String crsC, String crsN) {
        course.clear();
        if (crsC.equals("") && crsN.equals("")) {
            displayCourses();
            errorMsg.setText("Please input a course code or course name");
            return;
        }
        else if (!crsC.equals("") && !crsN.equals("")) {
            course = db.searchCourse(crsC, crsN);
        }
        else if (!crsC.equals("")) {
          course = db.searchCourseByCode(crsC);
        }
        else {
            course = db.searchCourseByName(crsN);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, course);
        courses.setAdapter(adapter);
        errorMsg.setText("");

    }
}