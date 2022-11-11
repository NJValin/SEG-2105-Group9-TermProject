package com.example.seg2105_termprojectcourseenrollmentapp;

import android.app.Application;

public class CourseEnrollmentApp extends Application {
    private DBHelper db;
    @Override
    public void onCreate() {
        super.onCreate();
        db = new DBHelper(this);
    }

    public DBHelper getDatabase() {
        return db;
    }
}