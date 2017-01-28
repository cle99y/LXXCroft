package com.clackityclack.lxxcroft;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by cle99 on 22/01/2017.
 */

public class DateSelector extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public String date;

    public DateSelector() {
        date = "yy/mm/dd";
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);

    //Create a new instance of Date Picker Dialog and return it
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day){

        Log.i("date",Integer.toString(year));
        Log.i("date",Integer.toString(month+1));
        Log.i("date",Integer.toString(day));
        date = year + "/" + month + "/" + day;

    }
}
