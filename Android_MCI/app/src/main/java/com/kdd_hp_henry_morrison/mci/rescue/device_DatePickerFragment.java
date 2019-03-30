package com.kdd_hp_henry_morrison.mci.rescue;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.kdd_hp_henry_morrison.mci.R;

import java.util.Date;

public class device_DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {



    @Override
public Dialog onCreateDialog(Bundle savedInstanceState){
        int year = 0;
        int month= 0;
        int day= 0;
//Use the current date as the default date in the date picker
        if(getArguments()!= null){
            Bundle bundle = getArguments();
            year = bundle.getInt("year",1970);
            month = bundle.getInt("month",1);
            day = bundle.getInt("day",1);

        }


        return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK,this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
        //return new DatePickerDialog(getActivity(), AlertDialog.THEME_TRADITIONAL, this, year, month, day);
        }


public void onDateSet(DatePicker view, int year, int month, int day) {
        //Do something with the date chosen by the user
        Button bt_birthday = (Button) getActivity().findViewById(R.id.device_birthday);
        bt_birthday.setText(year + "/" + String.format("%d", (month+1)) + "/" + String.format("%d", day));


        }
        }