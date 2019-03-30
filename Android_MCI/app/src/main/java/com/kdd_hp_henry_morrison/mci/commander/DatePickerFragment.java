package com.kdd_hp_henry_morrison.mci.commander;
import com.kdd_hp_henry_morrison.mci.R;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.DatePicker;
import android.app.Dialog;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK,this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_TRADITIONAL, this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Do something with the date chosen by the user
        TextView date_textview = (TextView) getActivity().findViewById(R.id.date_textview);
          date_textview.setText(year + "-" + String.format("%02d", (month+1)) + "-" + String.format("%02d", day));
    }
}