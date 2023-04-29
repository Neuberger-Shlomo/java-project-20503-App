package com.example.myapplication.Model;

import com.example.myapplication.Common.Views.Fragments.IModel;

import java.sql.Date;
import java.util.Calendar;
import java.util.Objects;

/**
 * a scheduled shift (user assign to the shift)
 * VERIABLES:
 * id =  shift id
 * userId = the first name of the user
 * startDate = the last name of the user
 * endDate = the user email
 * done = is the shift fully assigned
 */
public class ScheduleJob implements IModel {
    Integer id;

    Integer userId;

    String startDate;

    String endDate;

    Boolean done = false; // is the shift fully assigned

    /**
     * convert Date to string "yyyy-mm-dd".
     *
     * @param date - the date as input
     * @return the output string
     */

    private String fromDate(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date.getTime()));
        int y = calendar.get(Calendar.YEAR),
                m = calendar.get(Calendar.MONTH) + 1, d = calendar.get(Calendar.DAY_OF_MONTH);

        String startDate = String.format("%s-%s-%s", y,
                                         (m <= 9 ? "0" : "") + m,
                                         (d <= 9 ? "0" : "") + d);


        return startDate;
    }

    /**
     * convert string "yyyy-mm-dd" to Date.
     *
     * @param date - the date as input
     * @return the output Date
     */


    private java.sql.Date toDate(String date) {
        String[] strings = date.split("-");
        if (strings.length != 3)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(strings[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(strings[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strings[2]));

        return new Date(calendar.getTime().getTime());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return toDate(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = fromDate(startDate);
    }

    public Date getEndDate() {
        return toDate(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = fromDate(endDate);
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ScheduleJob))
            return false;
        ScheduleJob that = (ScheduleJob) o;
        return Objects.equals(getId(), that.getId())
               && startDate.equals(that.startDate)
               && endDate.equals(that.endDate);
    }

    //string of the shift details

    @Override
    public String toString() {
        return "ScheduleJob{" +
               "id=" + id +
               ", userId=" + userId +
               ", startDate='" + getStartDate() + '\'' +
               ", endDate='" + getEndDate() + '\'' +
               ", done=" + done +
               '}';
    }
    //string of the shift details do not include user details

    @Override
    public String toPrettyString() {
        return "id: " + id + "\n" +
               "start date: " + getStartDate() + "\n" +
               "end date: " + getEndDate() + "\n" +
               "done: " + getDone();
    }
}
