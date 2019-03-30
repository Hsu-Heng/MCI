package com.kdd_hp_henry_morrison.mci.commander;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPickerDialog extends DialogFragment {
    private NumberPicker numberPicker = new NumberPicker(getActivity());
    private NumberPicker.OnValueChangeListener valueChangeListener;
    private int maxvalue, minvalue, value;



    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final NumberPicker numberPicker = new NumberPicker(getActivity());
        maxvalue = bundle.getInt("maxvalue");
        minvalue = bundle.getInt("minvalue");
        numberPicker.setMaxValue(maxvalue);
        numberPicker.setMinValue(minvalue);
        try{
            value = bundle.getInt("value");
            numberPicker.setValue(value);
        }catch (Exception e){

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(bundle.getString("title"));
        builder.setMessage("請選澤");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }


}
