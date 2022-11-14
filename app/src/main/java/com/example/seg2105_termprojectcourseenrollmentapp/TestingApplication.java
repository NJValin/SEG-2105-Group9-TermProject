package com.example.seg2105_termprojectcourseenrollmentapp;

import android.app.Application;

public class TestingApplication extends Application {
    private DBHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = new DBHelper(this);
    }
    public DBHelper getDB() {return db;}
}
