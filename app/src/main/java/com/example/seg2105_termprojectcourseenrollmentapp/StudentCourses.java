package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StudentCourses extends AppCompatActivity {
    private ListView courses;
    private Button back, dropCrs;
    private TextView error;
    private ArrayList<String> course;
    private ArrayAdapter<String> adapter;
    private String crsCToDrop, crsNToDrop, username;
    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_courses);
       courses = (ListView) findViewById(R.id.myCourseList);
       back = (Button) findViewById(R.id.toBack);
       dropCrs = (Button) findViewById(R.id.dropCourse);
       error = (TextView) findViewById(R.id.errMsg);
       course = new ArrayList<>();
       Bundle extras = getIntent().getExtras();
       username = extras.getString("username");
       crsCToDrop = crsNToDrop = "";

       db = new DBHelper((CourseEnrollmentApp)getApplicationContext());

        courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = courses.getItemAtPosition(i);
                if (o.toString().equals("No Courses at the moment.")) {
                    return;
                }
                crsCToDrop = o.toString().split(" ")[0];
                crsNToDrop = o.toString().split(" ")[1];
                selectItem(o);
            }

        });
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
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.toBack:
                reset();
                break;
            case R.id.dropCourse:
                db.dropClass(crsCToDrop, username);
                reset();
                break;
        }
    }

    private void reset() {
        crsCToDrop = crsNToDrop = "";
        back.setVisibility(View.INVISIBLE);
        dropCrs.setVisibility(View.INVISIBLE);
        error.setText("");
        displayCourses();
    }

    private void selectItem(Object o) {
        course.clear();
        course.add(o.toString());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,course);
        courses.setAdapter(adapter);
        back.setVisibility(View.VISIBLE);
        dropCrs.setVisibility(View.VISIBLE);
    }

}