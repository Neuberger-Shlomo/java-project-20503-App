package com.example.myapplication.Common.Utils;

import androidx.core.util.Pair;

import java.sql.Date;
import java.util.Calendar;

final public class DateUtils {
    static public String fromDate(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date.getTime()));
        int y = calendar.get(Calendar.YEAR),
                m = calendar.get(Calendar.MONTH) + 1, d = calendar.get(Calendar.DAY_OF_MONTH);

        String startDate = String.format("%s-%s-%s", y,
                                         (m <= 9 ? "0" : "") + m,
                                         (d <= 9 ? "0" : "") + d);


        // yyyy-MM-dd
        return startDate;
    }

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
    static public Pair<String,String> stringFromDialog(Pair<Long, Long> longLongPair) {

        Date start = new Date(longLongPair.first);
        Date end   = new Date(longLongPair.second);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(start.getTime()));


        String startDate = String.format("%s/%s/%s",
                                         calendar.get(Calendar.DAY_OF_MONTH),
                                         calendar.get(Calendar.MONTH) + 1,
                                         calendar.get(Calendar.YEAR)
                                        );
        calendar.setTime(new Date(end.getTime()));
        String endDate = String.format("%s/%s/%s",
                                       calendar.get(Calendar.DAY_OF_MONTH),
                                       calendar.get(Calendar.MONTH)+1,
                                       calendar.get(Calendar.YEAR)
                                      );


        return new Pair<>(startDate,endDate);

    }
}
