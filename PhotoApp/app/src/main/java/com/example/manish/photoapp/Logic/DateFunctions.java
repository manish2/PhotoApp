package com.example.manish.photoapp.Logic;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by manish on 2017-06-04.
 */

public class DateFunctions {
    private static DateFunctions instance = null;
    //prevent instantiation
    private DateFunctions() {

    }
    public static DateFunctions getInstance() {
        if(instance == null) {
            instance = new DateFunctions();
        }
        return instance;
    }
    private String formattedTime(final Calendar c) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }
    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return formattedTime(c);
    }
    public String getYesterday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return formattedTime(c);
    }
    /**
     * Checks if date is valid by comparing two dates
     * @param c1
     * @param c2
     * @return
     */
    public boolean isValidDate(Calendar c1, Calendar c2) {
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        if(year2 > year1) {
            return true;
        }
        else if(year2 == year1) {
            if(month2 > month1 && (month1 <= 12 && month2 <= 12)) {
                return true;
            }
            else if(month2 == month1) {
                if(day2 >= day1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Converts the given input string to a Calendar object
     * @param date
     * @return
     * @throws ParseException
     */
    public Calendar convertFromStringToCalendar(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(date));
        return cal;
    }
}
