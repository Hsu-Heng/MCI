package com.kdd_hp_henry_morrison.mci.edit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kdd_hp_henry_morrison.mci.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Edit_device extends Fragment {
    String genderList[] = {"男", "女"};


    public Edit_device() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_device, container,false);
        Spinner gender = (Spinner) view.findViewById(R.id.gender);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, genderList);
        gender.setAdapter(adapter);
        return view;



    }

}


