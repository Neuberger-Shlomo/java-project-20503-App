
/**
 represents types of constraints of the user - LIST THE POSSIBLE CONSTRAINTS ON SHIFT
 veriables:
 constraintLevel = rank of constraint (1-5), for constaints importance hierarchy
 description = a string describing of the constraint

 */
package com.example.myapplication.Model;

import androidx.annotation.Nullable;

import com.example.myapplication.Common.Views.Fragments.IModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ConstraintType implements IModel {
    @Nullable
    private Integer id;
    @Nullable
    private Integer constraintLevel;
    private String description;

    public ConstraintType(Integer constraintLevel, String description) {
        this.id = null;
        this.constraintLevel = constraintLevel;
        this.description = description;
    }

    public ConstraintType(int id, int constraintLevel, String description) {
        this.id = id;
        this.constraintLevel = constraintLevel;
        this.description = description;
    }

    public static ConstraintType fromJson(JSONObject object) throws JSONException {
        return new ConstraintType(object.getInt("id"),object.getInt("constraintLevel"),
                                  object.getString("description"));
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

    @Override
    public String toString() {
        return description.toLowerCase(Locale.ROOT);
    }

    @Override
    public String toPrettyString() {

        return description;
    }
}
