package com.kdd_hp_henry_morrison.mci.commander;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
//import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;


import com.kdd_hp_henry_morrison.mci.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        TextView time_textview = (TextView) getActivity().findViewById(R.id.time_textview);
        time_textview.setText(String.valueOf(hourOfDay) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", Calendar.getInstance().get(Calendar.SECOND)));
    }
}
