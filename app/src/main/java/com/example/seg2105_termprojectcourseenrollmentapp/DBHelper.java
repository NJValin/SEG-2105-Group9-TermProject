package com.example.seg2105_termprojectcourseenrollmentapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
        db.execSQL("create Table courses(courseCode Text primary key, courseName Text, firstDay Text, firstDayTime Text, secondDay Text, secondDayTime Text," +
                " instructorName Text, description Text, capacity Integer)");
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
        if (userType.equals("student")) {
            db.execSQL("create Table "+username+"Classes(crsCode Text, crsName Text, dayOne Text, timeOne Text, dayTwo Text, timeTwo Text)");
        }
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
     * A method meant for the admin course page.
     *
     * @param crsCode - The code of the course
     * @param crsName - The name of the course
     * @return A string that holds the basic information of the course i.e. <b>crsCode</b> &
     *         <b>crsName</b>
     */
    public String getCourse (String crsCode, String crsName) {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from courses where courseCode=? and courseName=?", new String[] {crsCode, crsName});
        if (c.moveToFirst()) {
            return c.getString(0)+": "+c.getString(1);
        }
        else {
            return "";
        }
    }
    public String getUser(String username) {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from users where userName=?", new String[] {username});
        if (c.moveToFirst()) {

            return c.getString(0)+": "+c.getString(3)+" "+c.getString(4)+" | "+c.getString(2);
        }
        else {
            c.close();

            return "";
        }
    }
    /**
     *
     * @param crsCode
     * @param crsName
     * @return
     */
    public boolean addCourse(String crsCode, String crsName) {
        db = this.getWritableDatabase();
        ContentValues cntntVal = new ContentValues();
        cntntVal.put("courseCode", crsCode);
        cntntVal.put("courseName", crsName);
        cntntVal.put("firstDay", "N/A");
        cntntVal.put("firstDayTime", "N/A");
        cntntVal.put("secondDay", "N/A");
        cntntVal.put("secondDayTime", "N/A");
        cntntVal.put("instructorName", "N/A");
        cntntVal.put("description", "N/A");
        cntntVal.put("capacity", 0);
        long result = db.insert("courses", null, cntntVal);//returns -1 if insertion isn't successful
        //create the table of students in the course
        db.execSQL("create table "+crsCode+"Students(student Text, studentName Text)");
        return result!=-1;
    }
    //db.execSQL("create Table "+username+"Classes(crsCode Text, crsName Text, dayOne Text, timeOne Text, dayTwo Text, timeTwo Text)");
    public boolean enroll(String crsCode, String crsName, String userName) {
        db = this.getWritableDatabase();
        ContentValues s = new ContentValues();
        s.put("student", userName);
        String[] name = getName(userName);
        s.put("studentName", name[0]+" "+name[1]);
        ContentValues k = new ContentValues();
        k.put("crsCode", crsCode);
        k.put("crsName", crsName);
        String[] classCred = getClassInfo(crsCode);
        k.put("dayOne", classCred[0]);
        k.put("timeOne", classCred[1]);
        k.put("dayTwo", classCred[2]);
        k.put("timeTwo", classCred[3]);
        long result = db.insert(crsCode+"Students", null, s);
        long result2 = db.insert(userName+"Classes", null, k);
        return result!=-1&&result2!=-1;
    }
    public boolean validateEnrollment(String crsCode, String userName) {
        db = this.getWritableDatabase();
        //check that the student is not in the class.
        Cursor c = db.rawQuery("select * from "+crsCode+"Students where student=?", new String[] {userName});
        boolean b=c.getCount()==0;
        //check that there are no time conflicts
        String[] times = getClassInfo(crsCode);

        Cursor c2 = db.rawQuery("select * from "+userName+"Classes where dayOne=? and timeOne=?",new String[] {times[0], times[1]});
        boolean b2 = c2.getCount()==0;
        Cursor c3 = db.rawQuery("select * from "+userName+"Classes where dayTwo=? and timeTwo=?", new String[] {times[2], times[3]});
        boolean b3 = c3.getCount()==0;

        c.close();
        c2.close();
        c3.close();
        return b&&(b2||b3);
    }
    //db.execSQL("create Table courses(courseCode Text primary key, courseName Text, firstDay Text, firstDayTime Text, secondDay Text, secondDayTime Text," +
    //                " instructorName Text, description Text, capacity Integer)");
    private String[] getClassInfo(String crsCode) {
        String[] toReturn;
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from courses where courseCode=?",new String[] {crsCode});
        if (c.moveToNext()) {
            toReturn = new String[] {c.getString(2), c.getString(3), c.getString(4), c.getString(5)};
        }
        else {
            toReturn = new String[] {""};
        }
        return toReturn;
    }
    public void dropClass(String courseCode, String userName) {
        db = this.getWritableDatabase();
        db.execSQL("delete from "+userName+"Classes where crsCode=?", new String[] {courseCode});
        db.execSQL("delete from "+courseCode+"Students where student=?", new String[] {userName});
    }
    public ArrayList<String> getStudents(String crsCode) {
        ArrayList<String> x = new ArrayList<>();
        db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from users", null);
        while(c.moveToNext()) {
            x.add(c.getString(0)+": "+c.getString(1));
        }
        return x;
    }
    //db.execSQL("create Table "+username+"Classes(crsCode Text, crsName Text, dayOne Text, timeOne Text, dayTwo Text, timeTwo Text)");
    public String[] getSchoolSchedule(String username) {
        ArrayList<String> x = new ArrayList<>();
        db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+username+"Classes" , null);
        if (c.getCount()==0) {
            return new String[] {""};
        }
        while(c.moveToNext()) {
            x.add(c.getString(0)+": "+c.getString(1)+": "+c.getString(2)+": "+c.getString(3)+", "+
                    c.getString(4)+": "+c.getString(5));
        }
        String[] toReturn = new String[x.size()];
        int i =0;
        for (String q:x) {
            toReturn[i]=q;
            i++;
        }
        return toReturn;

    }
    //db.execSQL("create table "+crsCode+"Students(student Text, studentName Text)");
    public String[] getStudentList(String courseCode) {
        ArrayList<String> x = new ArrayList<>();
        db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+courseCode+"Students" , null);
        if (c.getCount()==0) {
            return new String[] {""};
        }
        while(c.moveToNext()) {
            x.add(c.getString(0)+": "+c.getString(1));
        }
        String[] toReturn = new String[x.size()];
        int i =0;
        for (String q:x) {
            toReturn[i]=q;
            i++;
        }
        return toReturn;
    }
    public boolean inClass(String crsCode, String username) {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+crsCode+"Students where student=?", new String[] {username});
        return c.getCount()!=0;
    }
    public void resetCourse(String crsC, String crsN) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set firstDay=? where courseCode=? and courseName=?", new String[] {"N/A", crsC, crsN});
        db.execSQL("update courses set secondDay=? where courseCode=? and courseName=?", new String[] {"N/A", crsC, crsN});
        db.execSQL("update courses set instructorName = ? where courseCode=? and courseName=?", new String[] {"N/A", crsC, crsN});
        db.execSQL("update courses set firstDayTime=? where courseCode=? and courseName=?", new String[] {"N/A", crsC, crsN});
        db.execSQL("update courses set secondDayTime=? where courseCode=? and courseName=?", new String[] {"N/A", crsC, crsN});
        db.execSQL("update courses set description=? where courseCode=? and courseName=?", new String[] {"N/A", crsC, crsN});
        db.execSQL("update courses set capacity=? where courseCode=? and courseName=?", new String[] {"N/A", crsC, crsN});
        db.execSQL("drop table if exists "+crsC+"Students");
        db.execSQL("create table "+crsC+"Students(student Text, studentName Text)");
    }
    public void setCourseDayOne(String crsName, String crsCode, String dayOne) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set firstDay=? where courseCode=? and courseName=?", new String[] {dayOne, crsCode, crsName});
    }
    public void setCourseDaytwo(String crsName, String crsCode, String dayTwo) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set secondDay=? where courseCode=? and courseName=?", new String[] {dayTwo, crsCode, crsName});
    }

    public void setInstructor(String crsName, String crsCode, @NonNull String[] name) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set instructorName = ? where courseCode=? and courseName=?", new String[] {name[0]+" "+name[1], crsName, crsCode});
    }
    public void setCourseTimeOne(String crsName, String crsCode, String timeOne) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set firstDayTime=? where courseCode=? and courseName=?", new String[] {timeOne, crsCode, crsName});
    }
    public void setCourseTimeTwo(String crsName, String crsCode, String timetwo) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set secondDayTime=? where courseCode=? and courseName=?", new String[] {timetwo, crsCode, crsName});
    }
    public void setDescription(String crsName, String crsCode, String description) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set description=? where courseCode=? and courseName=?", new String[] {description, crsCode, crsName});
    }
    public void setStudentLimit(String crsName, String crsCode, int limit) {
        db = this.getWritableDatabase();
        db.execSQL("update courses set capacity=? where courseCode=? and courseName=?", new String[] {limit+"", crsCode, crsName});

    }
    public String getInstructor(String crsName, String crsCode) {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from courses where courseCode=? and courseName=?", new String[] {crsName, crsCode});
        c.moveToFirst();
        return c.getString(6);
    }
    public String[] userList() {
        ArrayList<String> x = new ArrayList<>();
        db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from users", null);
        while(c.moveToNext()) {
            x.add(c.getString(0)+": "+c.getString(3)+" "+c.getString(4)+" | "+c.getString(2));
        }
        String[] toReturn = new String[x.size()];
        int i =0;
        for (String q:x) {
            toReturn[i]=q;
            i++;
        }
        return toReturn;
    }
    public String[] courseList() {
        ArrayList<String> x = new ArrayList<>();
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from courses", null);
        if (c.getCount()==0) {
            return new String[] {""};
        }
        while(c.moveToNext()) {
            x.add(c.getString(0)+": "+c.getString(1));
        }

        return x.toArray(new String[0]);
    }

    public String[] courseListForInstructor() {
        ArrayList<String> x = new ArrayList<>();
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from courses", null);
        if (c.getCount()==0) {
            return new String[] {""};
        }
        while (c.moveToNext()) {
            x.add(c.getString(0)+": "+c.getString(1)+": Dr."+c.getString(6)+", Days: "+c.getString(2)+" @ "+c.getString(3)+", "
            +c.getString(4)+" @ "+c.getString(5)+" student limit: "+c.getString(8)+"\n"+c.getString(7));
        }
        return x.toArray(new String[0]);
    }

    public String[] courseListOfTeacher(String[] name) {
        ArrayList<String> x = new ArrayList<>();
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from courses where instructorName=?", new String[] {name[0]+" "+name[1]});
        if (c.getCount()==0) {
            return new String[] {""};
        }
        while (c.moveToNext()) {
            x.add(c.getString(0)+": "+c.getString(1)+": Dr."+c.getString(6)+", Days: "+c.getString(2)+" @ "+c.getString(3)+", "
                    +c.getString(4)+" @ "+c.getString(5)+" student limit: "+c.getString(8)+"\n"+c.getString(7));
        }
        return x.toArray(new String[0]);
    }
    public boolean userExists(String username) {
        Cursor crsr= db.rawQuery("select userName from users where userName=?", new String[] {username});
        if (crsr.getCount()>0) {
            crsr.close();
            return true;
        }
        else {
            crsr.close();

            return false;
        }
    }
    public boolean courseExists(String crsCode, String crsName) {
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from courses where courseCode=? and courseName=?", new String[] {crsCode,crsName});
        if (c.getCount()>0) {
            c.close();

            return true;
        }
        else {
            c.close();
            return false;
        }
    }
    public boolean removeUser(String username) {
        db = this.getWritableDatabase();
        String userType = getUserType(username);
        if (userType.equals("admin")|| userType.equals("null")) {
            return false;
        }
        db.execSQL("drop table if exists "+username+"Classes");
        db.execSQL("delete from users where userName=?", new String[] {username});

        return true;
    }

    public void removeCourse(String crsCode, String crsName) {
        db.execSQL("delete from courses where courseCode=? and courseName=?", new String[] {crsCode, crsName});
        db.execSQL("drop table if exists "+crsCode+"Students");

    }
    public void editCourse(String newCrsCode, String oldCrsCode, String newCrsName, String oldCrsName) {
        db.execSQL("update courses set courseCode=? where courseName=?", new String[] {newCrsCode, oldCrsName});
        db.execSQL("update courses set courseName=? where courseCode=?", new String[] {newCrsName, newCrsCode});

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
        if (crsr.moveToFirst()) {
            return crsr.getString(2);
        }
        else {

            crsr.close();
            return "null";
        }
    }
    public ArrayList<String> searchCourseByCode(String crsC) {
        ArrayList<String> toReturn = new ArrayList<>();
        String[] x = this.courseListForInstructor();
        String eCode;
        for (String e:x) {
            eCode = e.split(": ")[0];
            if (crsC.equals(eCode)) {
                toReturn.add(e);
            }
        }
        return toReturn;
    }
    public ArrayList<String> searchCourseByName(String crsN) {
        ArrayList<String> toReturn = new ArrayList<>();
        String[] x = this.courseListForInstructor();
        String eName;
        for (String e:x) {
            eName = e.split(": ")[1];
            if (crsN.equals(eName)) {
                toReturn.add(e);
            }
        }
        return toReturn;
    }
    public ArrayList<String> searchCourse(String crsC, String crsN) {
        ArrayList<String> toReturn = new ArrayList<>();
        String[] x = this.courseListForInstructor();
        String eName;
        String eCode;
        for (String e:x) {
            eCode = e.split(": ")[0];
            eName = e.split(": ")[1];
            if (crsN.equals(eName) && crsC.equals(eCode)) {
                toReturn.add(e);
            }
        }
        return toReturn;
    }
    //db.execSQL("create Table courses(courseCode Text primary key, courseName Text, firstDay Text, firstDayTime Text, secondDay Text, secondDayTime Text," +
    //                " instructorName Text, description Text, capacity Integer)");
    public ArrayList<String> searchCourseByDay(String day) {
        db = this.getWritableDatabase();
        ArrayList<String> toReturn = new ArrayList<>();
        String[] x = this.courseListForInstructor();
        Cursor c = db.rawQuery("select * from courses where firstDay=?", new String[] {day});
        Cursor c2 = db.rawQuery("select * from courses where secondDay=?", new String[] {day});
        while (c.moveToNext()) {
            toReturn.add(c.getString(0)+": "+c.getString(1)+": Dr."+c.getString(6)+", Days: "+c.getString(2)+" @ "+c.getString(3)+", "
                    +c.getString(4)+" @ "+c.getString(5)+" student limit: "+c.getString(8)+"\n"+c.getString(7));
        }
        while (c2.moveToNext()) {
            toReturn.add(c.getString(0)+": "+c.getString(1)+": Dr."+c.getString(6)+", Days: "+c.getString(2)+" @ "+c.getString(3)+", "
                    +c.getString(4)+" @ "+c.getString(5)+" student limit: "+c.getString(8)+"\n"+c.getString(7));
        }

        return toReturn;
    }
    public String[] getName(String username) {
        db = this.getWritableDatabase();
        Cursor crsr = db.rawQuery("select * from users where userName = ?", new String[] {username});
        String[] x = new String[2];
        if (crsr.moveToFirst()) {

            return new String[] {crsr.getString(3), crsr.getString(4)};
        }
        else {

            return new String[] {"", ""};
        }

    }
    public int getMyCoursesSize(String username) {
        int r;
        db= this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+username+"Classes",null);
        r = c.getCount();
        return r;
    }
    public  void deleteAllUsers() {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select userName from users", null);
        while (c.moveToNext()) {
            removeUser(c.getString(0));
        }
        //db.execSQL("drop table users");
        //db.execSQL("create Table users(userName Text primary key, password Text, userType Text, firstname Text, lastname Text)");

    }
    public void deleteAllCourses() {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select courseCode, courseName from courses", null);
        while (c.moveToNext()) {
            removeCourse(c.getString(0), c.getString(1));
        }
        //db.execSQL("drop table courses");
        //db.execSQL("create Table courses(courseCode Text primary key, courseName Text, firstDay Text, firstDayTime Text, secondDay Text, secondDayTime Text," +
        //        " instructorName Text, description Text, capacity Integer)");
    }


}
