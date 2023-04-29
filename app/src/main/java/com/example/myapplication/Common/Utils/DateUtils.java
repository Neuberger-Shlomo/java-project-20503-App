package com.example.myapplication.Common.Utils;

import androidx.core.util.Pair;

import java.sql.Date;
import java.util.Calendar;

/**
 * DateUtils provides methods for get and convert dates
 */
final public class DateUtils {

    /**
     * convert  date in sql TO date string
     * --> INPUT - YEAR-MONTH-DAY   <--
     *
     * @param date date in SQL format
     * @return date in STRING format
     */

    static public String fromDate(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date.getTime()));
        int y = calendar.get(Calendar.YEAR),
                m = calendar.get(Calendar.MONTH) + 1, d = calendar.get(Calendar.DAY_OF_MONTH);
        //
        String startDate = String.format("%s-%s-%s", y,
                                         (m <= 9 ? "0" : "") + m,
                                         (d <= 9 ? "0" : "") + d);

        return startDate;
    }

    /**
     * convert date string to date in sql
     * <p>
     * --> INPUT - YEAR-MONTH-DAY   <--
     *
     * @param date date in string format
     * @return date in sql format
     */


    static public java.sql.Date toDate(String date) {
        String[] strings = date.split("-");
        if (strings.length != 3)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(strings[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(strings[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strings[2]));

        return new Date(calendar.getTime().getTime());
    }

    /**
     * convert date string to date in sql
     * <p>
     * --> INPUT - DAY-MONTH-YEAR  <--
     *
     * @param date date in string format
     * @return date in sql format
     */
    static public java.sql.Date toDateRegularFormat(String date) {
        String[] strings = date.split("-");
        if (strings.length != 3)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(strings[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(strings[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strings[0]));

        return new Date(calendar.getTime().getTime());
    }

    /**
     * convert pair of timestamps to a pair of date as strings.
     *
     * @param longLongPair pair of timestamps to to string
     * @return a pair of strings that represent start and end dates.
     */
    static public Pair<String, String> stringFromDialog(Pair<Long, Long> longLongPair) {

        Date start = new Date(longLongPair.first);
        Date end   = new Date(longLongPair.second);

        Calendar calendar = Calendar.getInstance();
        //get start date to string:
        // set calendar time to start date
        calendar.setTime(new Date(start.getTime()));

        // format of start date string is- "dd/mm/yyyy"
        String startDate = String.format("%s/%s/%s",
                                         calendar.get(Calendar.DAY_OF_MONTH),
                                         calendar.get(Calendar.MONTH) + 1,
                                         calendar.get(Calendar.YEAR)
                                        );

        // get end date to string:
        // set calendar time to end date
        calendar.setTime(new Date(end.getTime()));

        // format of end date string is- "dd/mm/yyyy"
        String endDate = String.format("%s/%s/%s",
                                       calendar.get(Calendar.DAY_OF_MONTH),
                                       calendar.get(Calendar.MONTH) + 1,
                                       calendar.get(Calendar.YEAR)
                                      );

        return new Pair<>(startDate, endDate);
    }
}
