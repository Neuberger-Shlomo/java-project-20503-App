package com.example.myapplication.Model;


import com.example.myapplication.Common.Views.Fragments.IModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Constraints implements IModel {
    private final boolean isPermanent;
    private final String  startDate;
    private final String  endDate;
    private final String  constType;

    Profile profile = new Profile();
    private final Integer id;

    private final Integer weekNumber;
    private final String  data;
    private final Integer typeId;

    private final Integer userId;

    public Constraints(String firstName, String lastName, String phoneNumber, String email,
                       int userId, String data, int weekNumber, int typeId, boolean isPermanent,
                       String startDate, String endDate, int id, String constType) {
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setEmail(email);
        profile.setPhoneNumber(phoneNumber);
        this.userId      = userId;
        this.data        = data;
        this.weekNumber  = weekNumber;
        this.typeId      = typeId;
        this.isPermanent = isPermanent;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.id          = id;
        this.constType   = constType;
    }

    public Constraints(Profile profile,
                       int userId, String data, int weekNumber, int typeId, boolean isPermanent,
                       String startDate, String endDate, int id, String constType) {
        this.profile     = profile != null ? profile : this.profile;
        this.userId      = userId;
        this.data        = data;
        this.weekNumber  = weekNumber;
        this.typeId      = typeId;
        this.isPermanent = isPermanent;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.id          = id;
        this.constType   = constType;
    }

    public static Constraints fromJSON(JSONObject object) throws JSONException {

        JSONObject users          = object.getJSONObject("users");
        JSONObject profileObj     = users.getJSONObject("profile");
        Profile    profile        = null;
        JSONObject constraintType = object.getJSONObject("constraintType");
        try {
            profile = Profile.fromJSON(profileObj);
        } catch (Exception e) {

        }
        int     id          = object.getInt("id");
        int     userId      = object.getInt("userId");
        String  data        = object.getString("data");
        int     weekNumber  = object.getInt("weekNumber");
        int     typeId      = object.getInt("typeId");
        String  constType   = constraintType.getString("description");
        boolean isPermanent = object.getBoolean("permanent");
        String  startDate   = object.getString("startDate");
        String  endDate     = object.getString("endDate");

        return new Constraints(profile, userId, data, weekNumber,
                               typeId, isPermanent, startDate, endDate, id, constType);
    }

    public String getFirstName() {
        return profile.getFirstName();
    }

    public String getLastName() {
        return profile.getLastName();
    }

    public String getPhoneNumber() {
        return profile.getPhoneNumber();
    }

    public String getEmail() {
        return profile.getEmail();
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

    public String toPrettyString() {
        return "Type: " + getConstType().toLowerCase(Locale.ROOT) +
               "\ndescription: " + getData().trim() +
               "\nis permanent: " + isPermanent() +
               "\nRange: " + getStartDate() + " to " + getEndDate();

    }

}
