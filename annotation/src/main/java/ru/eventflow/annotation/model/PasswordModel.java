package ru.eventflow.annotation.model;

public class PasswordModel {
    private String password;

    public PasswordModel() {
        password = "password"; //just set a default password.
    }

    public void setPassword(String pass) {
        password = pass;
    }

    public String getPassword() {
        return password;
    }
}

