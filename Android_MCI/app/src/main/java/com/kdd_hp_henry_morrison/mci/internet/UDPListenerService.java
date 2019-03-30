package com.kdd_hp_henry_morrison.mci.internet;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.kdd_hp_henry_morrison.mci.R;
import com.kdd_hp_henry_morrison.mci.rescue.rescue;
import com.kdd_hp_henry_morrison.mci.rescue.rescueclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UDPListenerService extends Service implements LocationListener {
    public static String UDP_BROADCAST = "UDPBroadcast";
    public static String UDPBroadcastbrocast = "uploadbrocast";
    String url1 = "http://140.120.183.205:8000/addhealthdata/";
    HashMap<String,Boolean> devices = new HashMap<>();
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000;
    long gps_interval = 100000;
    String address = "192.168.43.1";
    int rescuekey;
    Thread UDPBroadcastThread;

    //Boolean shouldListenForUDPBroadcast = false;
    DatagramSocket socket;

    private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {
        byte[] recvBuf = new byte[15000];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(port, broadcastIP);
            socket.setBroadcast(true);
        }
        //socket.setSoTimeout(1000);
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        Log.e("UDP", "Waiting for UDP broadcast");
        socket.receive(packet);

        String senderIP = packet.getAddress().getHostAddress();
        String message = new String(packet.getData()).trim();
//        String d[] = message.split("\n");
//        String data = d[1];
        message = message +","+String.valueOf(latitude) +","+String.valueOf(latitude);
        Log.e("UDP", "Got UDB broadcast from " + senderIP + ", message: " + message);

        String proce[] = message.split(",");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        String deveid = proce[0];
        if(devices.containsKey(deveid)){
            if(devices.get(deveid)){
                try {
                    jsonObject.put("device_id", proce[0]);
                    jsonObject.put("healthtype",Integer.valueOf(proce[0]));
                    jsonObject.put("respirations",Double.valueOf(proce[1]));
                    jsonObject.put("perfusion",Double.valueOf(proce[2]));
                    jsonObject.put("lon",proce[4]);
                    jsonObject.put("lat",proce[3]);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        url1,
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
            }
        }
        else{
            devices.put(deveid,false);
        }


//        Log.e("Gps", "lat" + latitude + ", lon: " + longitude);


        broadcastIntent(senderIP, message);
        socket.close();
    }
    private void broadcastIntent(String senderIP, String message) {
        Intent intent = new Intent(UDPListenerService.UDP_BROADCAST);
        intent.putExtra("sender", senderIP);
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }
    private BroadcastReceiver uploadrecevicer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            devices.put(intent.getStringExtra("devices"),true);

//            refreashRecyclerView();



        }
    };
    void startListenForUDPBroadcast() {
        UDPBroadcastThread = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress broadcastIP = InetAddress.getByName(address); //172.16.238.42 //192.168.1.255
                    InetAddress broadcastIP1 = InetAddress.getLocalHost();
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    Log.i("UDP", broadcastIP.getHostAddress());
                    Log.i("UDP", broadcastIP1.getHostAddress());
                    Integer port = 11111;
                    while (shouldRestartSocketListen) {
                        listenAndWaitAndThrowIntent(broadcastIP, port);
                    }
                    //if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
                } catch (Exception e) {
                    Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
                }
            }
        });
        UDPBroadcastThread.start();
    }

    private Boolean shouldRestartSocketListen = true;

    void stopListen() {
        shouldRestartSocketListen = false;
        socket.close();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(),5,notify_interval);


    }

    ;

    @Override
    public void onDestroy() {
        stopListen();
        getApplicationContext().unregisterReceiver(uploadrecevicer);
        mTimer.cancel();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shouldRestartSocketListen = true;
        address = intent.getStringExtra("ipaddress");
        rescuekey = intent.getIntExtra("rescuekey",0);
        startListenForUDPBroadcast();
        Log.i("UDP", "Service started");
        IntentFilter filter = new IntentFilter(UDPBroadcastbrocast);
        getApplicationContext().registerReceiver(uploadrecevicer,filter);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, gps_interval, 0, this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,gps_interval,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                    }
                }
            }
        }
    }
    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }


}