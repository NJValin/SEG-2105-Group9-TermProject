package com.example.seg2105_termprojectcourseenrollmentapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Neil Valin
 */
public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase crsEnrolldb;
    public DBHelper (Context context) {
        super(context, "CourseEnrollment.db", null, 1);
    }

    /**
     *
     * @param crsEnrolldb
     */
    @Override
    public void onCreate(SQLiteDatabase crsEnrolldb) {
        crsEnrolldb.execSQL("create Table users(userName Text primary key, password Text, userType Text)");
        crsEnrolldb.execSQL("create Table courses(courseCode Text primary key, courseName Text, courseDays Text)");
    }

    /**
     *
     * @param crsEnrolldb
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase crsEnrolldb, int i, int i1) {
        crsEnrolldb.execSQL("drop Table if exists users");
        crsEnrolldb.execSQL("drop Table if exists courses");
    }

    /**
     *
     * @param userName The user name of the user
     * @param password The password for the user.
     * @param userType The type of user the
     * @return a boolean value that is true if the insertion is successful (values are unique to table), else false
     */
    public boolean addUsers(String userName, String password, String userType) {
        crsEnrolldb = this.getWritableDatabase();
        ContentValues cntntVal = new ContentValues();
        cntntVal.put("userName", userName);
        cntntVal.put("password", password);
        cntntVal.put("userType", userType)
        long result = crsEnrolldb.insert("users", null, cntntVal);//returns -1 if insertion isn't successful
        return result!=-1;
    }

    /**
     *
     * @param crsCode
     * @return
     */
    public boolean addCourse(String crsCode, String crsName) {
        crsEnrolldb = this.getWritableDatabase();
        ContentValues cntntVal = new ContentValues();
        cntntVal.put("courseCode", crsCode);
        cntntVal.put("courseName", crsName);
        long result = crsEnrolldb.insert("courses", null, cntntVal);//returns -1 if insertion isn't successful
        return result!=-1;
    }

    /**
     *
     * @param username the username provided by the user.
     * @param password the password provided by the user.
     * @return a boolean value that determines if the login attempt is valid.
     */
    public boolean checkLogin(String username, String password) {
        boolean toReturn= false;
        crsEnrolldb = this.getWritableDatabase();
        Cursor crsr = crsEnrolldb.rawQuery("select * from users where userName= ? and password=?", new String[] {username, password});
        if (crsr.getCount()>0) {
            toReturn = true;
        }
        crsr.close();
        return toReturn;
    }


}
