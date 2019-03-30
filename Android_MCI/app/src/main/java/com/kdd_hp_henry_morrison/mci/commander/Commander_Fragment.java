package com.kdd_hp_henry_morrison.mci.commander;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kdd_hp_henry_morrison.mci.MainActivity;
import com.kdd_hp_henry_morrison.mci.R;
import com.kdd_hp_henry_morrison.mci.rescue.rescue;
import com.kdd_hp_henry_morrison.mci.rescue.resue_device;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Commander_Fragment extends Fragment implements OnMapReadyCallback{
    String postAPI = "http://140.120.183.205:8000/createRescue/";
    Button date_btn,upload_btn;
    Button time_btn;
    Button GPS_btn;
    MapView mapView;
    Bundle mBundle;
    EditText ed_name;
    LocationManager locationManager;
    TextView Commender_title;
    LatLng latLng = new LatLng(0,0);
    private static final int REQUEST_LOCATION = 1;
    TextView gps_textview;
    TextView date_textview,time_textview;
    private GoogleMap googlemap;
    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String commander = "commander";

    public void onDateClicked(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"DatePicker");
    }

    public void onTimeClicked(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }



    private void showLocation(Location location){
        TextView GPS_textview = (TextView) getActivity().findViewById(R.id.GPS_textview);
        String currentLocation = "經度:" + location.getLongitude() + "緯度:" + location.getLatitude();
        GPS_textview.setText(currentLocation);
    }

    public void onGPSClicked(View v){
    }


    public Commander_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Boolean change = false;
        View view = inflater.inflate(R.layout.fragment_commander, container,false);
        getActivity().setTitle("新增救援事件");
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        date_btn = (Button)view.findViewById(R.id.date_button);
        GPS_btn = (Button)view.findViewById(R.id.getgps);
        date_textview = (TextView)view.findViewById(R.id.date_textview);
        time_textview = (TextView)view.findViewById(R.id.time_textview);
        Commender_title = (TextView)view.findViewById(R.id.Commender_title);
        settings = getActivity().getSharedPreferences(data, 0);
        Commender_title.setText("救護車"+settings.getString(commander, ""));
        ed_name = (EditText)view.findViewById(R.id.Commender_name);
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat datee=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=datee.format(d1);
        Log.e("msg", t1);
        date_textview.setText(t1);
        SimpleDateFormat timmme=new SimpleDateFormat("HH:mm:ss");
        Date d2=new Date(time);
        String t2=timmme.format(d2);
        Log.e("msg", t2);
        time_textview.setText(t2);
        date_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onDateClicked(v);}
        });
        time_btn = (Button)view.findViewById(R.id.time_button);
        time_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onTimeClicked(v);}
        });

        gps_textview = (TextView)view.findViewById(R.id.GPS_textview);
        upload_btn = (Button)view.findViewById(R.id.upload);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                String stime = date_textview.getText().toString()+" "+time_textview.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name",ed_name.getText().toString());
                    jsonObject.put("commander","polt");
                    jsonObject.put("lon",latLng.longitude);
                    jsonObject.put("lat",latLng.latitude);
                    jsonObject.put("timestart",stime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        postAPI,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("err", error.toString());
                            }
                        }
                );
                requestQueue.add(mJsonObjectRequest);
                requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JsonObjectRequest>() {
                    @Override
                    public void onRequestFinished(Request<JsonObjectRequest> request) {
                        FragmentTransaction fragmentTransaction;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        rescue rescue1 = new rescue();
                        fragmentTransaction.replace(R.id.fragmentcontainer, rescue1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });


            }
        });


        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map_rescue);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
            GPS_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();

                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        getLocation();
                    }
                }
            });
        }
    }
    public void saveLating(LatLng lating) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lating);
        markerOptions.title(lating.latitude + " : " + lating.longitude);

        googlemap.addMarker(markerOptions);

    }
    private void moveMap(LatLng place) {
        //建立地圖攝影機的位置元件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();
        //使用動畫的效果移動地圖
        googlemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),500,null);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    @Override
    public void onMapReady(final GoogleMap Map) {
        this.googlemap = Map;

        LatLng sydney = new LatLng(-34, 151);
        Map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        Map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                latLng = new LatLng(latti,longi);
                moveMap(latLng);
                gps_textview.setText("Your current location is"+ "\n" + "Lattitude = " + latti
                        + "\n" + "Longitude = " + longi);
                saveLating(latLng);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                latLng = new LatLng(latti,longi);

                moveMap(latLng);
                gps_textview.setText("Your current location is"+ "\n" + "Lattitude = " + latti
                        + "\n" + "Longitude = " + longi);
                saveLating(latLng);

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                latLng = new LatLng(latti,longi);

                moveMap(latLng);
                gps_textview.setText("Your current location is"+ "\n" + "Lattitude = " + latti
                        + "\n" + "Longitude = " + longi);
                saveLating(latLng);
            }else{

                Toast.makeText(getContext(),"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}