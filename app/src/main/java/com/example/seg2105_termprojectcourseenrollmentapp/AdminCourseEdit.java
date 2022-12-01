package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
public class AdminCourseEdit extends AppCompatActivity {
    private DBHelper db;
    private EditText code, name;
    private Button toHome,  deleteCourse, confirmEdit, confirmCreate, createCourse;
    private TextView errorMessage;
    private ListView courseList;
    private String courseCodeToDelete, courseNameToDelete;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> courses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_edit);
        code = (EditText) findViewById(R.id.newCode);
        name = (EditText) findViewById(R.id.newName);
        toHome = (Button) findViewById(R.id.returnToHome);
        deleteCourse = (Button) findViewById(R.id.deleteCourse);
        confirmEdit = (Button) findViewById(R.id.confirmEdit);
        confirmCreate = (Button) findViewById(R.id.confirmCreate);
        createCourse = (Button) findViewById(R.id.createClass);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        courseList = (ListView) findViewById(R.id.listOfCourses);
        courses = new ArrayList<>();
        db = new DBHelper((CourseEnrollmentApp)getApplicationContext());
        courseCodeToDelete = "";
        courseNameToDelete = "";
        toHome.setOnClickListener(this::onClick);
        deleteCourse.setOnClickListener(this::onClick);
        confirmEdit.setOnClickListener(this::onClick);
        confirmCreate.setOnClickListener(this::onClick);
        createCourse.setOnClickListener(this::onClick);
        displayCourses();
        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = courseList.getItemAtPosition(i);
                courseCodeToDelete = o.toString().split(": ")[0];
                courseNameToDelete = o.toString().split(": ")[1];
                courseSelected(courseCodeToDelete, courseNameToDelete);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createClass:
                code.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                confirmCreate.setVisibility(View.VISIBLE);
                confirmCreate.setClickable(true);
                errorMessage.setText("");
                break;
            case R.id.confirmCreate:
                if (!validateInput()) {
                    errorMessage.setText("Invalid input");
                    code.setText("");
                    name.setText("");
                    code.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    confirmCreate.setVisibility(View.INVISIBLE);
                    confirmCreate.setClickable(false);
                }
                else {
                    boolean x = createCourse();
                    if (!x) {
                        errorMessage.setText("Course failed to be created");
                        code.setText("");
                        name.setText("");
                        code.setVisibility(View.INVISIBLE);
                        name.setVisibility(View.INVISIBLE);
                        confirmCreate.setVisibility(View.INVISIBLE);
                        confirmCreate.setClickable(false);
                    }
                    else {
                        code.setText("");
                        name.setText("");
                        code.setVisibility(View.INVISIBLE);
                        name.setVisibility(View.INVISIBLE);
                        confirmCreate.setVisibility(View.INVISIBLE);
                        confirmCreate.setClickable(false);
                        errorMessage.setText("");
                        displayCourses();
                    }
                }
                break;
            case R.id.returnToHome:
                finish();
                break;
            case R.id.deleteCourse:
                deleteCourse();
                courseCodeToDelete="";
                courseNameToDelete="";
                displayCourses();
                code.setText("");
                name.setText("");
                code.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                confirmEdit.setVisibility(View.INVISIBLE);
                confirmEdit.setClickable(false);
                deleteCourse.setVisibility(View.INVISIBLE);
                deleteCourse.setClickable(false);
                createCourse.setVisibility(View.VISIBLE);
                createCourse.setClickable(true);
                break;
            case R.id.confirmEdit:
                if (!validateInput()) {
                    code.setText("");
                    name.setText("");
                    errorMessage.setText("Invalid input");
                }
                else {
                    errorMessage.setText("");
                    editCourse();
                    displayCourses();
                    code.setText("");
                    name.setText("");
                    code.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    confirmEdit.setVisibility(View.INVISIBLE);
                    confirmEdit.setClickable(false);
                    deleteCourse.setVisibility(View.INVISIBLE);
                    deleteCourse.setClickable(false);
                    createCourse.setVisibility(View.VISIBLE);
                    createCourse.setClickable(true);

                }
                break;
        }
    }
    private void courseSelected(String x, String y) {
        code.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        createCourse.setVisibility(View.INVISIBLE);
        createCourse.setClickable(false);
        confirmCreate.setVisibility(View.INVISIBLE);
        confirmCreate.setClickable(false);
        deleteCourse.setVisibility(View.VISIBLE);
        deleteCourse.setClickable(true);
        confirmEdit.setVisibility(View.VISIBLE);
        confirmEdit.setClickable(true);
        courses.clear();
        courses.add(db.getCourse(x, y));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        courseList.setAdapter(adapter);
    }
    private boolean validateInput() {
        String x = code.getText().toString();
        String y = name.getText().toString();
        if (x.equals("")||y.equals("")) {
            return false;
        }
        return true;
    }
    private boolean createCourse() {
        String x = code.getText().toString();
        String y = name.getText().toString();
        return db.addCourse(x,y);
    }
    public void deleteCourse() {
        db.removeCourse(courseCodeToDelete, courseNameToDelete);
    }
    private void editCourse() {
        String x = code.getText().toString();
        String y = name.getText().toString();
        db.editCourse(x, courseCodeToDelete, y, courseNameToDelete);
    }
    private void displayCourses() {
        courses.clear();
        String[] x = db.courseList();
        for (int i=0; i<x.length; i++) {
            courses.add(x[i]);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        courseList.setAdapter(adapter);
    }

}