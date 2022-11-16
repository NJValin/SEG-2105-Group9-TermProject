package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class InstructorCourseEdit extends AppCompatActivity {
    private TextView errorMsg;
    private TimePicker setTime;
    private ListView courseList;
    private Button setDay, studentLimit, setDescription, backButton, cnfrmStdLim, cnfrmDesc, cnfrmDay;
    private EditText stdLimit, description, dayone, daytwo;
    private ArrayList<String> course;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_course_edit);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
        setTime = (TimePicker) findViewById(R.id.timePick);
        courseList = (ListView) findViewById(R.id.courseList);
        setDay = (Button) findViewById(R.id.setDay);
        studentLimit = (Button) findViewById(R.id.studentLimitBtn);
        setDescription = (Button) findViewById(R.id.setDescription);
        backButton = (Button) findViewById(R.id.backButton);
        stdLimit = (EditText) findViewById(R.id.studentLimitTextBox);
        cnfrmStdLim = (Button) findViewById(R.id.confirmStudentLimit);
        description = (EditText) findViewById(R.id.descriptionTextBox);
        cnfrmDesc = (Button) findViewById(R.id.confirmDescription);
        dayone = (EditText) findViewById(R.id.dayone);
        daytwo = (EditText) findViewById(R.id.daytwo);
        cnfrmDay = (Button) findViewById(R.id.confirmDay);

        course = new ArrayList<>();
        


    }
}