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
    private SQLiteDatabase db;
    public DBHelper (Context context) {
        super(context, "CourseEnrollment.db", null, 1);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(userName Text primary key, password Text, userType Text, firstname Text, lastname Text)");
        db.execSQL("create Table courses(courseCode Text primary key, courseName Text, courseDays Text)");
    }

    /**
     *
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists users");
        db.execSQL("drop Table if exists courses");
    }

    /**
     *
     * @param userName The user name of the user
     * @param password The password for the user.
     * @param userType The type of user the
     * @return a boolean value that is true if the insertion is successful (values are unique to table), else false
     */
    public boolean addUsers(String userName, String password, String userType, String firstName, String lastName) {
        db = this.getWritableDatabase();
        ContentValues cntntVal = new ContentValues();
        cntntVal.put("userName", userName);
        cntntVal.put("password", password);
        cntntVal.put("userType", userType);
        cntntVal.put("firstname", firstName);
        cntntVal.put("lastname", lastName);
        if (checkLogin(userName, password)==true) {
            return false;
        }
        long result = db.insert("users", null, cntntVal);//returns -1 if insertion isn't successful

        return result!=-1;
    }

    /**
     *
     * @param crsCode
     * @return
     */
    public boolean addCourse(String crsCode, String crsName) {
        db = this.getWritableDatabase();
        ContentValues cntntVal = new ContentValues();
        cntntVal.put("courseCode", crsCode);
        cntntVal.put("courseName", crsName);
        long result = db.insert("courses", null, cntntVal);//returns -1 if insertion isn't successful
        return result!=-1;
    }


    public boolean removeUser(String username) {
        if (getUserType(username).equals("admin")) {
            return false;
        }
        db.execSQL("delete from users where userName=?", new String[] {username});
        return true;
    }

    /**
     *
     * @param username the username provided by the user.
     * @param password the password provided by the user.
     * @return a boolean value that determines if the login attempt is valid.
     */
    public boolean checkLogin(String username, String passWord) {
        boolean toReturn= false;
        db = this.getWritableDatabase();
        Cursor crsr = db.rawQuery("select userName, password from users where userName= ? and password = ?", new String[] {username, passWord});
        if (crsr.getCount()>0) {
            toReturn = true;
        }
        crsr.close();
        return toReturn;
    }
    public String getUserType(String username) {
        db=this.getReadableDatabase();
        Cursor crsr = db.rawQuery("select userType from users where userName = ?", new String[] {username});
        db.close();
        return crsr.toString();
    }
    public String[] getName(String username) {
        db = this.getWritableDatabase();
        Cursor crsr1 = db.rawQuery("select firstname from users where userName=?", new String[] {username});
        Cursor crsr2 = db.rawQuery("select lastname from users where userName=?", new String[] {username});
        db.close();
        return new String[] {crsr1.toString(), crsr2.toString()};
    }
    public void deleteAllUsers() {
        db = this.getWritableDatabase();
        db.execSQL("delete from users");
        db.close();
    }


}
