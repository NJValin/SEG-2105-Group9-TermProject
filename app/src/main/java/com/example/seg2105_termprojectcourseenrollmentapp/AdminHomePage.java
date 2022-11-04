package com.example.seg2105_termprojectcourseenrollmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.seg2105_termprojectcourseenrollmentapp.databinding.ActivityHomePageBinding;

import org.w3c.dom.Text;

public class AdminHomePage extends AppCompatActivity {


    private AppBarConfiguration appBarConfiguration;
    private ActivityHomePageBinding binding;
    private TextView wlcmAdminmessage, errorMessage;
    private Button createClass, deleteClass, edtClass, searchUsers, searchCourse, returnToHome;
    private EditText searchforUsers, oldClassCode, oldClassName, newClassCode, newClassName;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        wlcmAdminmessage = (TextView) findViewById(R.id.welcomeAdmin);
        createClass = (Button) findViewById(R.id.createClass);
        deleteClass = (Button) findViewById(R.id.deleteClass);
        edtClass = (Button) findViewById(R.id.editClass);
        searchUsers = (Button) findViewById(R.id.searchUsers);
        searchforUsers = (EditText) findViewById(R.id.searchBar);
        oldClassCode = (EditText) findViewById(R.id.newCourseCode);
        oldClassName = (EditText) findViewById(R.id.newNameBar);
        newClassName = (EditText) findViewById(R.id.newName);
        newClassCode = (EditText) findViewById(R.id.newCode);
        errorMessage = (TextView) findViewById(R.id.errorCode);
        searchCourse = (Button) findViewById(R.id.searchCourses);
        returnToHome = (Button) findViewById(R.id.returnToHome);
        db = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        String[] name = db.getName(extras.getString("username"));
        wlcmAdminmessage.setText("Welcome "+name[0]+" "+name[1]+"! you are logged in as admin");

        createClass.setOnClickListener(this::onClick);
        deleteClass.setOnClickListener(this::onClick);
        edtClass.setOnClickListener(this::onClick);
        searchUsers.setOnClickListener(this::onClick);
        searchCourse.setOnClickListener(this::onClick);
        returnToHome.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        String oldcode;
        String oldName;
        switch(view.getId()){
            case R.id.createClass:
                if(validInput((oldClassCode).toString(),(oldClassName).toString())){
                    createCourse((oldClassCode).toString(),(oldClassName).toString());
                }

                break;

            case R.id.searchUsers:
                if (validInput(searchforUsers.getText().toString())) {
                    deleteUsers(searchforUsers.getText().toString());
                }
                else {
                    errorMessage.setText("");
                    oldClassCode.setText("");
                    oldClassName.setText("");
                }
                break;
            case R.id.searchCourses:
                oldcode = oldClassCode.getText().toString();
                oldName = oldClassName.getText().toString();
                if (validInput(oldcode, oldName)) {
                    searchforUsers.setVisibility(View.GONE);
                    edtClass.setVisibility(View.VISIBLE);
                    deleteClass.setVisibility(View.VISIBLE);
                    returnToHome.setVisibility(View.VISIBLE);
                    searchUsers.setVisibility(View.GONE);
                    createClass.setVisibility(View.GONE);
                    newClassCode.setVisibility(View.VISIBLE);
                    newClassName.setVisibility(View.VISIBLE);
                }
                else {
                    errorMessage.setText("Class doesn't exist");
                    searchforUsers.setText("");
                    oldClassName.setText("");
                    oldClassCode.setText("");
                }
                break;
            case R.id.returnToHome:
                searchforUsers.setVisibility(view.VISIBLE);
                searchUsers.setVisibility(view.VISIBLE);
                createClass.setVisibility(view.VISIBLE);
                edtClass.setVisibility(view.GONE);
                deleteClass.setVisibility(view.GONE);
                returnToHome.setVisibility(view.GONE);
                newClassCode.setVisibility(view.GONE);
                newClassName.setVisibility(view.GONE);
                oldClassName.setText("");
                oldClassCode.setText("");
                errorMessage.setText("");
                break;
            case R.id.editClass:
                if (validInput(newClassCode.getText().toString(), newClassName.getText().toString())) {
                    editCourse(newClassCode.getText().toString(), newClassName.getText().toString(), oldClassCode.getText().toString(), oldClassName.getText().toString());
                }

                break;
            case R.id.deleteClass:
                if (validInput(oldClassCode.getText().toString(), oldClassName.getText().toString())) {
                    deleteCourse(oldClassCode.getText().toString(), oldClassName.getText().toString());
                }
                break;
 
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void editCourse(String classCode, String className, String oldCode, String oldName){
        db.editCourse(classCode, className, oldCode, oldName);
    }
    /**
     *
     * @param classCode The code of the class to be created by the admin
     * @param className The name of the class to be created by the admin
     */
    private void createCourse(String classCode, String className){
        boolean x = db.addCourse(classCode,className);
        if(x == false) {
            errorMessage.setText("Cannot add course. Please check if course code / course name is already in use");
        }
        else {

            errorMessage.setText("");
            oldClassCode.setText("");
            oldClassName.setText("");
        }
    }

    private void deleteCourse(String classCode, String className){
        db.removeCourse(classCode, className);
    }

    private void deleteUsers(String username){

        if (db.userExists(searchforUsers.getText().toString())) {
            db.removeUser(username);
            searchforUsers.setText("");
            oldClassCode.setText("");
            oldClassName.setText("");
        }
        else {
            errorMessage.setText("user doesn't exist");
            searchforUsers.setText("");
            oldClassCode.setText("");
            oldClassName.setText("");
        }

    }
    /**
     *
     * @param input1 The input of the admin
     * @return a boolean value that is true if the insertion is successful (values are unique to table), else false
     */

    private boolean validInput(String input1){
        boolean returnValue = true;
        if (input1.equals("")) {
            errorMessage.setText("Please enter a valid input");
            searchforUsers.setText("");
            returnValue=false;
        }
        return returnValue;
    }

    /**
     *
     * @param input1 The input of the admin
     * @param input1 The input of the admin
     * @return a boolean value that is true if the insertion is successful (values are unique to table), else false
     */
    //Overload
    private boolean validInput(String input1, String input2){
        boolean valReturn = true;
        if (input1.equals("")||input2.equals("")) {
            errorMessage.setText("Please enter a valid course code and name");
            oldClassCode.setText("");
            oldClassName.setText("");
            valReturn=false;
        }
        return valReturn;
    }
}