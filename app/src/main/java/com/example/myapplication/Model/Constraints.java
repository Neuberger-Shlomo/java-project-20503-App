package com.example.myapplication.Model;


import com.example.myapplication.Common.Views.Fragments.IModel;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;

public class Constraints implements IModel {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private int userId;
    private String data;
    private int weekNumber;
    private int typeId;
    private boolean isPermanent;
    private String startDate;
    private String endDate;
    private int id;

    private String constType;

    public Constraints(String firstName, String lastName, String phoneNumber, String email,
                       int userId, String data, int weekNumber, int typeId, boolean isPermanent,
                       String startDate, String endDate, int id, String constType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userId = userId;
        this.data = data;
        this.weekNumber = weekNumber;
        this.typeId = typeId;
        this.isPermanent = isPermanent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.constType = constType;
    }

    public static Constraints fromJSON(JSONObject object) throws JSONException {

        JSONObject users = object.getJSONObject("users");
        JSONObject profile = users.getJSONObject("profile");
        JSONObject constraintType = object.getJSONObject("constraintType");
        String firstName = profile.getString("firstName");
        String lastName = profile.getString("lastName");
        String email = profile.getString("email");
        String phoneNumber = profile.getString("phoneNumber");
        int id = object.getInt("id");
        int userId = object.getInt("userId");
        String data = object.getString("data");
        int weekNumber = object.getInt("weekNumber");
        int typeId = object.getInt("typeId");
        String constType = constraintType.getString("description");
        boolean isPermanent = object.getBoolean("permanent");
        String startDate = object.getString("startDate");
        String endDate = object.getString("endDate");

        return new Constraints(firstName,lastName,phoneNumber, email, userId, data, weekNumber,
                        typeId, isPermanent, startDate, endDate, id, constType);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getUserId() {
        return userId;
    }

    public String getData() {
        return data;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getTypeId() {
        return typeId;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getId() {
        return id;
    }

    public String getConstType() {
        return constType;
    }

    public String toPrettyString(){
        return "Type: " + getConstType() +
               "\nConstrain: " + getData() +
               "\nis permanent: " + isPermanent() +
               "\nEnd Date: " + getEndDate();

    }

}
