package com.kdd_hp_henry_morrison.mci.internet;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.kdd_hp_henry_morrison.mci.rescue.rescueclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class volleyrequest {
    public List<rescueclass> getrescuelist(Context context, String httpurl)
    {
        List<rescueclass> data = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        final List<rescueclass> finalData = data;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, httpurl,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject1) {
                Log.d("cylog",jsonObject1.toString());
                try {
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

                        finalData.add(rescueclass1);
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
        data = new ArrayList<>();
        requestQueue.add(jsonObjectRequest);
        return data;
    }

}
