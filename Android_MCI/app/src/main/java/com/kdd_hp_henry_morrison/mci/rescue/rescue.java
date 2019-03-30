package com.kdd_hp_henry_morrison.mci.rescue;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.kdd_hp_henry_morrison.mci.R;
import com.kdd_hp_henry_morrison.mci.internet.volleyrequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class rescue extends Fragment {

    private adapter_rescue adapter;
    private String url = "http://140.120.183.205:8000/queryRescue/";
    private String url2 = "http://140.120.183.205:8000/queryDevicepk/";
    List<rescueclass> data = new ArrayList<>();
    List<device_card> bundledata = new ArrayList<>();
    private RecyclerView recyclerView;
    public rescue() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getdata(String url){
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject1) {
                try {
                    data.clear();
                    JSONArray jsonArray = jsonObject1.getJSONArray("rows");
                    Log.d("cylog",jsonArray.toString());
                    for(int i =0 ;i < jsonArray.length();i++){
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        rescueclass rescueclass1 = new rescueclass();
                        rescueclass1.setName(jsonobject.getString("name"));
                        rescueclass1.setCommander(jsonobject.getString("commander"));
                        LatLng latLng = new LatLng(jsonobject.getDouble("lat"),jsonobject.getDouble("lon"));
                        rescueclass1.setLatLng(latLng);
                        rescueclass1.setTimestart(jsonobject.getString("timestart"));
                        rescueclass1.setTimestop(jsonobject.getString("timeend"));
                        rescueclass1.setPk(jsonobject.getInt("pk"));
                        data.add(rescueclass1);
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
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JsonObjectRequest>() {
            @Override
            public void onRequestFinished(Request<JsonObjectRequest> request) {

                adapter = new adapter_rescue(getContext(),data);

                recyclerView.setAdapter(adapter);
                adapter.setListnear(new adapter_rescue.MListener() {
                    @Override
                    public void onClick(int postition) {
                        serial_rescuecard obj = new serial_rescuecard();
                        obj.setPk(data.get(postition).getPk());
                        obj.setName(data.get(postition).getName());
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("pk",data.get(postition).getPk());
                        bundle1.putString("name",data.get(postition).getName());
                        FragmentTransaction fragmentTransaction;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        resue_device resue_device_fragment = new resue_device();
                        resue_device_fragment.setArguments(bundle1);
                        fragmentTransaction.replace(R.id.fragmentcontainer, resue_device_fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                pDialog.hide();
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
            }
        }
        );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_rescue,
                container,false);


        Log.d("size",String.valueOf(data.size()));
        getdata(url);
        adapter = new adapter_rescue(getContext(),data);
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
        getActivity().setTitle("救援行動");
        return recyclerView;
    }




    @Override
    public void onDetach() {
        super.onDetach();

    }
    public class serial_rescuecard implements Serializable {
        private static final long serialVersionUID = -7060210544600464481L;
        int pk;
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPk() {
            return pk;
        }

        public void setPk(int pk) {
            this.pk = pk;
        }
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

}
