package com.example.myapplication.Model;

/**
 * this is a class that represent a button in the user screen
 * from this button navigate to a specific user secreen
 */
public class UserButton {
    String buttonName;
    int    fragment;

    public UserButton(String buttonName, int fragment) {
        this.buttonName = buttonName;
        this.fragment   = fragment;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }
}
