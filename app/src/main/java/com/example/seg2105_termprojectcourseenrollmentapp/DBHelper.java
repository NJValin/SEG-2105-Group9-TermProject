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
     * @param username The user name of the user
     * @param password The password for the user.
     * @param userType The type of user the
     * @return a boolean value that is true if the insertion is successful (values are unique to table), else false
     */
    public boolean addUsers(String username, String password, String userType, String firstName, String lastName) {
        db = this.getWritableDatabase();
        ContentValues cntntVal = new ContentValues();
        cntntVal.put("userName", username);
        cntntVal.put("password", password);
        cntntVal.put("userType", userType);
        cntntVal.put("firstname", firstName);
        cntntVal.put("lastname", lastName);
        if (checkLogin(username, password)==true) {
            return false;
        }
        long result = db.insert("users", null, cntntVal);//returns -1 if insertion isn't successful

        if (result==-1) {
            return false;
        }
        else {
            return true;
        }
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

    public boolean userExists(String username) {
        Cursor crsr= db.rawQuery("select userName from users where userName=?", new String[] {username});
        if (crsr.getCount()>0) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean removeUser(String username) {
        if (getUserType(username).equals("admin")) {
            return false;
        }
        db.execSQL("delete from users where userName=?", new String[] {username});
        return true;
    }

    public void removeCourse(String crsCode, String crsName) {
        db.execSQL("delete from courses where courseCode=? and courseName=?", new String[] {crsCode, crsName});
    }
    public void editCourse(String newCrsCode, String oldCrsCode, String newCrsName, String oldCrsName) {
        db.execSQL("update courses set courseCode=? where courseName=?", new String[] {newCrsCode, oldCrsName});
        db.execSQL("update courses set courseName=? where courseCode=?", new String[] {newCrsName, oldCrsCode});
    }
    /**
     *
     * @param username the username provided by the user.
     * @param passWord the password provided by the user.
     * @return a boolean value that determines if the login attempt is valid.
     */
    public boolean checkLogin(String username, String passWord) {
        boolean toReturn= false;
        db = this.getWritableDatabase();
        Cursor crsr = db.rawQuery("select * from users where userName = ? and passWord = ?", new String[] {username, passWord});
        if (crsr.getCount()>0) {
            toReturn = true;
        }
        crsr.close();
        return toReturn;
    }
    public String getUserType(String username) {
        db=this.getReadableDatabase();
        Cursor crsr = db.rawQuery("select * from users where userName = ?", new String[] {username});
        if (crsr.moveToNext()) {
            return crsr.getString(2);
        }
        else {
            db.close();
            crsr.close();
            return "null";
        }
    }
    public String[] getName(String username) {
        db = this.getWritableDatabase();
        Cursor crsr = db.rawQuery("select * from users where userName = ?", new String[] {username});
        String[] x = new String[2];
        if (crsr.moveToNext()) {
            return new String[] {crsr.getString(3), crsr.getString(4)};
        }
        else {
            return new String[] {"", ""};
        }

    }
    public void deleteAllUsers() {
        db = this.getWritableDatabase();
        db.execSQL("drop table users");
        db.execSQL("create Table users(userName Text primary key, password Text, userType Text, firstname Text, lastname Text)");
        db.close();
    }


}
