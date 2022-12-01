package com.example.seg2105_termprojectcourseenrollmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Array;
import java.util.ArrayList;

public class InstructorCourseEdit extends AppCompatActivity {
    private TextView errorMsg;
    private ListView courseList;
    private Button setDay, studentLimit, setDescription, backButton, cnfrmStdLim, cnfrmDesc, cnfrmDay, dropCrs;
    private EditText stdLimit, description, dayonetime, daytwotime;
    private Spinner dayone, daytwo;
    private ArrayList<String> course;
    private ArrayAdapter<String> adapter;

    private String code, crsName, dayOne, dayTwo;
    private String[] name;
    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_course_edit);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
        courseList = (ListView) findViewById(R.id.courseList);
        setDay = (Button) findViewById(R.id.setDay);
        studentLimit = (Button) findViewById(R.id.studentLimitBtn);
        setDescription = (Button) findViewById(R.id.setDescription);
        backButton = (Button) findViewById(R.id.backButton);
        stdLimit = (EditText) findViewById(R.id.studentLimitTextBox);
        cnfrmStdLim = (Button) findViewById(R.id.confirmStudentLimit);
        description = (EditText) findViewById(R.id.descriptionTextBox);
        cnfrmDesc = (Button) findViewById(R.id.confirmDescription);
        dayonetime = (EditText) findViewById(R.id.dayonetime);
        daytwotime = (EditText) findViewById(R.id.daytwotime);
        dayone = (Spinner) findViewById(R.id.dayone);
        daytwo = (Spinner) findViewById(R.id.daytwo);
        cnfrmDay = (Button) findViewById(R.id.confirmDay);
        dropCrs = (Button) findViewById(R.id.dropCourse);

        ArrayAdapter adapterDayOne = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        ArrayAdapter adapterDayTwo = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        adapterDayOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDayTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayone.setAdapter(adapterDayOne);
        daytwo.setAdapter(adapterDayTwo);

        db = new DBHelper((CourseEnrollmentApp)getApplicationContext());
        Bundle extras = getIntent().getExtras();
        name = extras.getStringArray("username");
        course = new ArrayList<>();
        code = crsName = dayOne = dayTwo = "";


        dayone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                Object o = dayone.getItemAtPosition(position);
                setDayOne(o.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        daytwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object o  =daytwo.getItemAtPosition(i);
                setDayTwo(o.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setDay.setOnClickListener(this::onClick);
        studentLimit.setOnClickListener(this::onClick);
        setDescription.setOnClickListener(this::onClick);
        backButton.setOnClickListener(this::onClick);
        cnfrmStdLim.setOnClickListener(this::onClick);
        cnfrmDay.setOnClickListener(this::onClick);
        cnfrmDesc.setOnClickListener(this::onClick);
        dropCrs.setOnClickListener(this::onClick);
        displayCourses();
        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = courseList.getItemAtPosition(i);
                code = o.toString().split(": ")[0];
                crsName = o.toString().split(": ")[1];
                selectItem(o);
            }
        });

    }
    private void setDayOne(String s) {
        dayOne = s;
    }
    private void setDayTwo(String s) {
        dayTwo = s;
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                setDay.setVisibility(View.INVISIBLE);
                studentLimit.setVisibility(View.INVISIBLE);
                setDescription.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                cnfrmDay.setVisibility(View.INVISIBLE);
                cnfrmStdLim.setVisibility(View.INVISIBLE);
                cnfrmDesc.setVisibility(View.INVISIBLE);
                stdLimit.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                dayone.setVisibility(View.INVISIBLE);
                daytwo.setVisibility(View.INVISIBLE);
                daytwotime.setVisibility(View.INVISIBLE);
                dayonetime.setVisibility(View.INVISIBLE);
                dropCrs.setVisibility(View.INVISIBLE);
                errorMsg.setText("");
                dayOne = "";
                dayTwo = "";
                code = "";
                crsName = "";
                displayCourses();
                break;
            case R.id.setDay:
                errorMsg.setText("");
                backButton.setVisibility(View.VISIBLE);
                dayone.setVisibility(View.VISIBLE);
                daytwo.setVisibility(View.VISIBLE);
                dayonetime.setVisibility(View.VISIBLE);
                daytwotime.setVisibility(View.VISIBLE);
                cnfrmDay.setVisibility(View.VISIBLE);
                break;
            case R.id.setDescription:
                errorMsg.setText("");
                backButton.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                cnfrmDesc.setVisibility(View.VISIBLE);
                break;
            case R.id.studentLimitBtn:
                errorMsg.setText("");
                backButton.setVisibility(View.VISIBLE);
                stdLimit.setVisibility(View.VISIBLE);
                cnfrmStdLim.setVisibility(View.VISIBLE);
                break;
            case R.id.confirmDescription:
                if (description.getText().toString().equals("")) {
                    errorMsg.setText("Please enter a description");
                }
                else {
                    db.setDescription(crsName, code, description.getText().toString());
                    setDay.setVisibility(View.INVISIBLE);
                    studentLimit.setVisibility(View.INVISIBLE);
                    setDescription.setVisibility(View.INVISIBLE);
                    backButton.setVisibility(View.INVISIBLE);
                    cnfrmDay.setVisibility(View.INVISIBLE);
                    cnfrmStdLim.setVisibility(View.INVISIBLE);
                    cnfrmDesc.setVisibility(View.INVISIBLE);
                    stdLimit.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.INVISIBLE);
                    dayone.setVisibility(View.INVISIBLE);
                    daytwo.setVisibility(View.INVISIBLE);
                    daytwotime.setVisibility(View.INVISIBLE);
                    dayonetime.setVisibility(View.INVISIBLE);
                    dropCrs.setVisibility(View.INVISIBLE);
                    errorMsg.setText("");
                    dayOne = "";
                    dayTwo = "";
                    code = "";
                    crsName = "";
                    displayCourses();
                }
                break;
            case R.id.confirmStudentLimit:
                if (stdLimit.getText().toString().equals("")) {
                    errorMsg.setText("Please enter a student limit");
                }
                else {
                    db.setStudentLimit(crsName, code, Integer.parseInt(stdLimit.getText().toString()));
                    setDay.setVisibility(View.INVISIBLE);
                    studentLimit.setVisibility(View.INVISIBLE);
                    setDescription.setVisibility(View.INVISIBLE);
                    backButton.setVisibility(View.INVISIBLE);
                    cnfrmDay.setVisibility(View.INVISIBLE);
                    cnfrmStdLim.setVisibility(View.INVISIBLE);
                    cnfrmDesc.setVisibility(View.INVISIBLE);
                    stdLimit.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.INVISIBLE);
                    dayone.setVisibility(View.INVISIBLE);
                    daytwo.setVisibility(View.INVISIBLE);
                    daytwotime.setVisibility(View.INVISIBLE);
                    dayonetime.setVisibility(View.INVISIBLE);
                    dropCrs.setVisibility(View.INVISIBLE);
                    errorMsg.setText("");
                    dayOne = "";
                    dayTwo = "";
                    code = "";
                    crsName = "";
                    displayCourses();
                }
                break;
            case R.id.confirmDay:
                if (dayonetime.getText().toString().equals("")||daytwotime.getText().toString().equals("")||dayone.getSelectedItem().toString().equals("")||daytwo.getSelectedItem().equals("")) {
                    errorMsg.setText("Enter all fields ");
                }
                else {
                    int x = Integer.parseInt(dayonetime.getText().toString().split(":")[0]);
                    int x2= Integer.parseInt(dayonetime.getText().toString().split(":")[1]);
                    int y = Integer.parseInt(daytwotime.getText().toString().split(":")[0]);
                    int y2 = Integer.parseInt(daytwotime.getText().toString().split(":")[1]);

                    if ((x>24||x2>59)||(y>24||y2>59)) {
                        errorMsg.setText("Invalid times");
                        setDay.setVisibility(View.INVISIBLE);
                        studentLimit.setVisibility(View.INVISIBLE);
                        setDescription.setVisibility(View.INVISIBLE);
                        backButton.setVisibility(View.INVISIBLE);
                        cnfrmDay.setVisibility(View.INVISIBLE);
                        cnfrmStdLim.setVisibility(View.INVISIBLE);
                        cnfrmDesc.setVisibility(View.INVISIBLE);
                        stdLimit.setVisibility(View.INVISIBLE);
                        description.setVisibility(View.INVISIBLE);
                        dayone.setVisibility(View.INVISIBLE);
                        daytwo.setVisibility(View.INVISIBLE);
                        daytwotime.setVisibility(View.INVISIBLE);
                        dayonetime.setVisibility(View.INVISIBLE);
                        dropCrs.setVisibility(View.INVISIBLE);
                    }
                    else {
                        db.setCourseDayOne(crsName, code, dayone.getSelectedItem().toString());
                        db.setCourseDaytwo(crsName, code, daytwo.getSelectedItem().toString());
                        db.setCourseTimeOne(crsName, code, dayonetime.getText().toString());
                        db.setCourseTimeTwo(crsName, code, daytwotime.getText().toString());
                        setDay.setVisibility(View.INVISIBLE);
                        studentLimit.setVisibility(View.INVISIBLE);
                        setDescription.setVisibility(View.INVISIBLE);
                        backButton.setVisibility(View.INVISIBLE);
                        cnfrmDay.setVisibility(View.INVISIBLE);
                        cnfrmStdLim.setVisibility(View.INVISIBLE);
                        cnfrmDesc.setVisibility(View.INVISIBLE);
                        stdLimit.setVisibility(View.INVISIBLE);
                        description.setVisibility(View.INVISIBLE);
                        dayone.setVisibility(View.INVISIBLE);
                        daytwo.setVisibility(View.INVISIBLE);
                        daytwotime.setVisibility(View.INVISIBLE);
                        dayonetime.setVisibility(View.INVISIBLE);
                        dropCrs.setVisibility(View.INVISIBLE);
                        errorMsg.setText("");
                    }
                    dayOne = "";
                    dayTwo = "";
                    code = "";
                    crsName = "";
                    displayCourses();

                }
                break;
            case R.id.dropCourse:
                db.resetCourse(code, crsName);
                setDay.setVisibility(View.INVISIBLE);
                studentLimit.setVisibility(View.INVISIBLE);
                setDescription.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                cnfrmDay.setVisibility(View.INVISIBLE);
                cnfrmStdLim.setVisibility(View.INVISIBLE);
                cnfrmDesc.setVisibility(View.INVISIBLE);
                stdLimit.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                dayone.setVisibility(View.INVISIBLE);
                daytwo.setVisibility(View.INVISIBLE);
                daytwotime.setVisibility(View.INVISIBLE);
                dayonetime.setVisibility(View.INVISIBLE);
                dropCrs.setVisibility(View.INVISIBLE);
                errorMsg.setText("");
                dayOne = "";
                dayTwo = "";
                code = "";
                crsName = "";
                displayCourses();
        }

    }
    private void selectItem(Object o) {
        setDay.setVisibility(View.VISIBLE);
        studentLimit.setVisibility(View.VISIBLE);
        setDescription.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        dropCrs.setVisibility(View.VISIBLE);
        course.clear();
        course.add(o.toString());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, course);
        courseList.setAdapter(adapter);
    }

    private void displayCourses() {
        course.clear();
        String[] x = db.courseListOfTeacher(name);
        if (x.length==1&&x[0].equals("")) {
            finish();
        }
        for (String q: x) {
            course.add(q);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, course);
        courseList.setAdapter(adapter);
    }
}