package com.kdd_hp_henry_morrison.mci;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


public class index extends Fragment {
    private EditText ed_index_ipaddress, ed_index_commander, ed_index_port;
    private TextInputLayout index_ipaddress, index_commander, index_port;
    private Button btn_signup;
    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String ipaddress = "ipaddress";
    private static final String commander = "commander";
    private static final String port = "port";
    public index() {
        // Required empty public constructor
    }

    public void readData(){

        settings = getActivity().getSharedPreferences(data, MODE_PRIVATE);
        ed_index_ipaddress.setText(settings.getString(ipaddress, ""));
        ed_index_commander.setText(settings.getString(commander, ""));
        ed_index_port.setText(settings.getString(port, ""));
    }
    public void saveData(){
        settings = getActivity().getSharedPreferences(data,MODE_PRIVATE);
        settings.edit()
                .putString(ipaddress, ed_index_ipaddress.getText().toString())
                .putString(commander, ed_index_commander.getText().toString())
                .putString(port, ed_index_port.getText().toString())
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container,false);
        getActivity().setTitle("設定");
        ed_index_ipaddress = (EditText)view.findViewById(R.id.ed_index_ipaddress);
        ed_index_commander = (EditText)view.findViewById(R.id.ed_index_commander);
        ed_index_port = (EditText)view.findViewById(R.id.ed_index_port);
        index_ipaddress = (TextInputLayout)view.findViewById(R.id.index_ipaddress);
        index_commander = (TextInputLayout)view.findViewById(R.id.index_commander);
        index_port = (TextInputLayout)view.findViewById(R.id.index_port);
        btn_signup = (Button) view.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                saveData();

            }
        });
        readData();

        // Inflate the layout for this fragment
        return view;
    }





    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
