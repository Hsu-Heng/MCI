package com.kdd_hp_henry_morrison.mci.rescue;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;


public class resue_device extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "pk";
    private static final String ARG_PARAM2 = "name";
    private String url2 = "http://140.120.183.205:8000/queryDevicepk/";
    private RecyclerView recyclerView;
    List<device_card> data = new ArrayList<>();
    private adapter_card_device adapter;
    // TODO: Rename and change types of parameters
    private int pk;
    private String name;
    private OnFragmentInteractionListener mListener;

    public resue_device() {
        // Required empty public constructor
    }
    private void getdata2(int pk){
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2+pk+"/",null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject1) {
                Log.d("tag", jsonObject1.toString());
                try {
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    data.clear();
                    for(int i =0;i < jsonArray.length();i++){
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        device_card device = new device_card();
                        device.setDeviceId(jsonobject.getString("DeviceId") + "   :"+jsonobject.getString("name"));
                        device.setPk(jsonobject.getInt("pk"));
                        Log.d("status",String.valueOf(jsonobject.getInt("status")));
                        device.setStatus(jsonobject.getInt("status"));
                        data.add(device);
                    }

                    pDialog.hide();

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
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JsonObjectRequest>() {
            @Override
            public void onRequestFinished(Request<JsonObjectRequest> request) {
                adapter = new adapter_card_device(getContext(),data);
                recyclerView.setAdapter(adapter);
                adapter.setListnear(new adapter_rescue.MListener() {
                    @Override
                    public void onClick(int postition) {
                        Log.d("d",String.valueOf(postition));
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("pk",data.get(postition).getPk());
                        FragmentTransaction fragmentTransaction;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        device device1 = new device();
                        device1.setArguments(bundle1);
                        fragmentTransaction.replace(R.id.fragmentcontainer, device1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        });

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
        // Inflate the layout for this fragment
        Bundle bundle=getArguments();
        pk = bundle.getInt(ARG_PARAM1);
        name = bundle.getString(ARG_PARAM2);
        getdata2(pk);

        getActivity().setTitle(name);
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_rescue,
                container,false);

        adapter = new adapter_card_device(getContext(),data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setListnear(new adapter_rescue.MListener() {
            @Override
            public void onClick(int postition) {
                Log.d("d",String.valueOf(postition));

            }
        });
        // Inflate the layout for this fragment

        return recyclerView;
    }


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
