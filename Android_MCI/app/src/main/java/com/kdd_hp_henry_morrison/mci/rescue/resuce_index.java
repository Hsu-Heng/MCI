package com.kdd_hp_henry_morrison.mci.rescue;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kdd_hp_henry_morrison.mci.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class resuce_index extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "pk";
    private String url2 = "http://140.120.183.205:8000/queryDevicepk/";

    // TODO: Rename and change types of parameters
    private int pk;


    private OnFragmentInteractionListener mListener;

    public resuce_index() {
        // Required empty public constructor
    }
    private void getdata2(int pk){

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2+pk+"/",null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject1) {
                Log.d("tag", jsonObject1.toString());
                try {
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for(int i =0;i < jsonArray.length();i++){
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        device_card device = new device_card();
                        device.setDeviceId(jsonobject.getString("DeviceId"));
                        device.setPk(jsonobject.getInt("pk"));
                        device.setStatus(jsonobject.getInt("status"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }
        });

        requestQueue.add(jsonObjectRequest);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle=getArguments();
            pk = bundle.getInt(ARG_PARAM1);



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getdata2(pk);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resuce_index, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
