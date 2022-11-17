package com.example.seg2105_termprojectcourseenrollmentapp;

public class InputValidator {
    public boolean isValidInput(String username, String password) {
        boolean toReturn = true;
        if (username.equals("") || password.equals("")) {
            errorMessage.setText("Please enter a valid username or password");
            userName.setText("");
            this.password.setText("");
            toReturn = false;
        }
        return toReturn;

    }
}