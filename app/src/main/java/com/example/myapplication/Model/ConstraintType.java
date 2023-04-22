package com.example.myapplication.Model;

public class ConstraintType {
    private int id;
    private int constraintLevel;
    private String description;

    public ConstraintType(int id, int constraintLevel, String description) {
        this.id = id;
        this.constraintLevel = constraintLevel;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConstraintLevel() {
        return constraintLevel;
    }

    public void setConstraintLevel(int constraintLevel) {
        this.constraintLevel = constraintLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
