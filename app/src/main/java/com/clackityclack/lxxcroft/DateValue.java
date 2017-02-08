package com.clackityclack.lxxcroft;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by cle99 on 29/01/2017.
 */

public class DateValue {

    public String date;


    public DateValue(String s) {

        date = s;

    }

    public static int getDateValue(String d) {    // d representing a date value dd mmm yyyy

        String[] months = {"Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};

        String[] dSplit = d.split(" ");
        int dVal = 0;
        dVal += Integer.parseInt(dSplit[2]);
        dVal *= 1 + Arrays.asList(months).indexOf(dSplit[1]);
        dVal += Integer.parseInt(dSplit[0]);

        return dVal;
    }


    public static String getToday() {

        String[] months = {"Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};

        String today = new SimpleDateFormat("dd MM yyyy").format(new Date());
        String[] dSplit = today.split(" ");
        dSplit[1] = months[Integer.parseInt(dSplit[1]) - 1];


        return (dSplit[0] + " " + dSplit[1] + " " + dSplit[2]);

    }

    public static String formatParadeData (String s) {

        String[] longDate = s.split(" ");
        longDate[1] = longDate[1].substring(0, longDate[1].length() - 2);
        longDate[2] = longDate[2].substring(0, 3);


        return longDate[1] + " " + longDate[2] + " " + longDate[3];

    }

}
