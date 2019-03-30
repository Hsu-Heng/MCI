package com.kdd_hp_henry_morrison.mci.internet;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.kdd_hp_henry_morrison.mci.R;
import com.kdd_hp_henry_morrison.mci.rescue.adapter_rescue;
import com.kdd_hp_henry_morrison.mci.rescue.device;
import com.kdd_hp_henry_morrison.mci.rescue.rescue;
import com.kdd_hp_henry_morrison.mci.rescue.rescueclass;
import com.kdd_hp_henry_morrison.mci.rescue.resue_device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.whitebyte.hotspotmanager.WIFI_AP_STATE;
import info.whitebyte.hotspotmanager.WifiApManager;

import static android.content.Context.MODE_MULTI_PROCESS;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_WRITEABLE;
import static android.content.Context.WIFI_SERVICE;
import static com.android.volley.VolleyLog.TAG;


public class checkreceive extends Fragment {
    String sharepackage = "com.kdd_hp_henry_morrison.mci.rescue";
    Context sharedAppContext = null;
    private UDPListenerService udpListenerService;
    private Button hotspot, udp;
    private TextView receiver_receive;
    private SharedPreferences settings;
    private SharedPreferences devicesshare;
    SwipeController swipeController = null;
    Handler mHandler;
    String rescue ="";
    int rescuekey;
    List<rescuepkclass> data = new ArrayList<>();
    List<devices> deviceslist = new ArrayList<>();
    private Spinner udpspineer;
    udpadapter udpadapter ;
    Double lat = 0.0, lon = 0.0;
    private RecyclerView recyclerView;
    private String rescueurl = "http://140.120.183.205:8000/queryRescuepk/";
    String adddata = "http://140.120.183.205:8000/addhealthdata/";
    private BroadcastReceiver udpreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String data = intent.getStringExtra("sender") + "\n"+ intent.getStringExtra("message");
            refrashdata(data);
//            refreashRecyclerView();



        }
    };
    private void refrashdata(String data){
        Log.d("sdsd",data);
        try {
            String sdata[] = data.split("\n");
            String d[] = sdata[1].split(",");
            boolean haveadd = false;
            for(int i = 0 ; i < deviceslist.size();i++){
                if(d[0].equals(deviceslist.get(i).getId())){
                    deviceslist.get(i).setStatus(Integer.valueOf(d[1]));
                    deviceslist.get(i).setRespirations(d[2]);
                    deviceslist.get(i).setPerfusion(d[3]);
                    lat = Double.valueOf(d[4]);
                    lon = Double.valueOf(d[5]);
                    haveadd = true;
                    udpadapter.notifyDataSetChanged();

                    break;
                }
            }
            if(haveadd==false){
                devices devices = new devices();
                devices.setStatus(Integer.valueOf(d[1]));
                devices.setRespirations(d[2]);
                devices.setPerfusion(d[3]);
                devices.setId(d[0]);
                devices.setUpload(false);
                devices.setSex("");
                devices.setAge("");
                devices.setName("");
                deviceslist.add(devices);
                udpadapter.notifyItemInserted(deviceslist.size()-1);

            }
        }catch (Exception e){

        }





    }
    WifiApManager wifiApManager;
    boolean hotspotstatus;
    boolean udpstatus;



    private void savesharedata(){
        String save = "";
        for(devices devices: deviceslist){
            String add = "";
            add += devices.getId()+",";
            if(devices.isUpload()){
                add += devices.getPk()+","+"true,";
            }
            else{
                add += ",false,";
            }
            add += devices.getName()+",";
            add += devices.getAge()+",";
            add += devices.getSex()+",";
            add += devices.getPerfusion()+",";
            add += devices.getRespirations();
            save+=add+"\n";
            }
        devicesshare.edit()
                .putString("share", save)
                .commit();
    }
    private void refreashRecyclerView() {
        udpadapter = new udpadapter(getContext(),deviceslist);
        recyclerView.setAdapter(udpadapter);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));

    }

    private void setupRecyclerView() {
        String devicestr = devicesshare.getString("share","");
        Log.d("dd",devicestr);
        if(devicestr!=""){
            deviceslist.clear();
            String sa[] = devicestr.split("\n");

            for(String s: sa){
                String sd[] = s.split(",");
                String devicearray[] = {"","","","","","","",""};
                for(int i = 0 ;i < sd.length;i++){
                    devicearray[i] = sd[i] ;
                }
                Log.d("dd",String.valueOf(devicearray.length));
                    devices devices = new devices();
                    devices.id = devicearray[0];
                    devices.upload=false;
//                    if(devices.pk!=""){
//                        devices.pk = devicearray[1];
//                    }

                    if(devicearray[2].equals("true")){
                        devices.pk = devicearray[1];
                        Log.d("pk",devices.pk);
                        devices.upload=true;
                    }
                    devices.setName(devicearray[3]);
                    devices.setAge(devicearray[4]);
                    devices.setSex(devicearray[5]);
                    devices.setPerfusion(devicearray[6]);
                    devices.setRespirations(devicearray[7]);
                deviceslist.add(devices);
            }
        }
        udpadapter = new udpadapter(getContext(),deviceslist);
        recyclerView.setAdapter(udpadapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                if(deviceslist.get(position).isUpload()){
                    Toast.makeText(getContext(),"開啟上傳",Toast.LENGTH_LONG).show();
                }
                else{
                    deviceslist.get(position).setUpload(true);
                    broadcastIntent(deviceslist.get(position).getId());
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("create", deviceslist.get(position).getId());
                        jsonObject.put("device_id", deviceslist.get(position).getId());
                        jsonObject.put("healthtype",Integer.valueOf(deviceslist.get(position).getStatus()));
                        jsonObject.put("respirations",Double.valueOf(deviceslist.get(position).getRespirations()));
                        jsonObject.put("perfusion",Double.valueOf(deviceslist.get(position).getPerfusion()));
                        jsonObject.put("lon",lon);
                        jsonObject.put("lat",lat);
                        jsonObject.put("rescue",rescue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                            adddata,
                            jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.d("sd",response.toString());
                                        deviceslist.get(position).setPk(response.getString("pk"));
                                        Log.d("deviceslist",deviceslist.get(position).getPk());
                                        savesharedata();
                                        udpadapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(),"開啟上傳",Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

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

            @Override
            public void onLeftClicked(int position) {
                if(deviceslist.get(position).isUpload()){
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("pk",Integer.valueOf(deviceslist.get(position).getPk()));

                    FragmentTransaction fragmentTransaction;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    device device1 = new device();
                    device1.setArguments(bundle1);
                    fragmentTransaction.replace(R.id.fragmentcontainer, device1);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getContext(),"請先確認手環是否連線",Toast.LENGTH_LONG).show();
                }
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        savesharedata();
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
                        rescuepkclass rescueclass1 = new rescuepkclass();
                        rescueclass1.setName(jsonobject.getString("name"));
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
                                                        final ArrayList<String> dsting = new ArrayList<>();
                                                        for(int i =0; i < data.size();i++){
                                                            dsting.add(data.get(i).getName());
                                                        }


                                                        if(rescue.equals("")){
                                                            if(data.size()==0){
                                                                String s[] = {"請去新增事件"};
                                                                ArrayAdapter<String> rescueadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,s);
                                                                udpspineer.setAdapter(rescueadapter);
                                                                udpspineer.setSelection(0);
                                                            }
                                                            else{
                                                                ArrayAdapter<String> rescueadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,dsting);
                                                                udpspineer.setAdapter(rescueadapter);
                                                                udpspineer.setSelection(0);
                                                            }

                                                        }
                                                        else{
                                                            ArrayAdapter<String> rescueadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,dsting);
                                                            udpspineer.setAdapter(rescueadapter);
                                                            int postition = 0;
                                                            for(int i = 0 ; i < data.size();i++){
                                                                if(data.get(i).getName().equals(rescue)){
                                                                    postition = i;
                                                                    break;
                                                                }
                                                            }
                                                            udpspineer.setSelection(postition);
                                                        }
                                                        pDialog.hide();
                                                    }
                                                }
        );
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences("rescue",MODE_PRIVATE);
        rescue = settings.getString("rescue", "");
        rescuekey = settings.getInt("rescuepk", 0);
        hotspotstatus = settings.getBoolean("hotspot", false);
        udpstatus = settings.getBoolean("udp", false);
    }
    private void broadcastIntent(String device) {
        Intent intent = new Intent(UDPListenerService.UDPBroadcastbrocast);
        intent.putExtra("devices", device);
        getActivity().sendBroadcast(intent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkreceive, container,false);
        udpspineer = (Spinner)view.findViewById(R.id.udpspineer);
        hotspot = (Button) view.findViewById(R.id.ap_status);
        recyclerView = (RecyclerView) view.findViewById(R.id.checkreceive_recycler);
        wifiApManager = new WifiApManager(getContext());
        setHasOptionsMenu(true);
        devicesshare = getContext().getSharedPreferences("devicesshare",0);
        getdata(rescueurl);
        setupRecyclerView();
        udp = (Button)view.findViewById(R.id.udpstatus);




        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_openAp:
                wifiApManager.setWifiApEnabled(null, true);
                break;
            case R.id.action_closeap:
                wifiApManager.setWifiApEnabled(null, false);
                break;
            case R.id.action_openUdp:
                WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
                String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                Intent gattServiceIntent = new Intent(getContext(), UDPListenerService.class);
                gattServiceIntent.putExtra("ipaddress",ipAddress);
                gattServiceIntent.putExtra("pk",rescuekey);
                getContext().startService(gattServiceIntent);
                IntentFilter filter = new IntentFilter(UDPListenerService.UDP_BROADCAST);
                getContext().registerReceiver(udpreceiver,filter);
                rescue = udpspineer.getItemAtPosition(udpspineer.getSelectedItemPosition()).toString();

                Log.d("asdad",rescue);
                for(int r = 0 ; r < data.size();r++){
                    if(data.get(r).getName().equals(rescue) ){
                        rescuekey = data.get(r).getPk();
//                                                                            settings = getActivity().getSharedPreferences("rescue",MODE_PRIVATE);
                        settings.edit()
                                .putString("rescue", rescue)
                                .putInt("rescuepk", rescuekey)
                                .commit();
                        break;
                    }
                }
                break;
            case R.id.action_closeUdp:

                Intent gattServiceItent = new Intent(getContext(), UDPListenerService.class);
                getContext().stopService(gattServiceItent);
                getContext().unregisterReceiver(udpreceiver);
                break;
                // do stuff, like showing settings fragment
            case R.id.action_clear:
                deviceslist.clear();
                 devicesshare.edit()
                .putString("share", "")
                .commit();
                 refreashRecyclerView();
                 break;

        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }




}
