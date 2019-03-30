package com.kdd_hp_henry_morrison.mci.rescue;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kdd_hp_henry_morrison.mci.R;
import com.kdd_hp_henry_morrison.mci.commander.DatePickerFragment;
import com.kdd_hp_henry_morrison.mci.commander.NumberPickerDialog;
import com.kdd_hp_henry_morrison.mci.internet.devices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

import static android.content.Context.MODE_MULTI_PROCESS;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_WRITEABLE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link device.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link device#newInstance} factory method to
 * create an instance of this fragment.
 */
public class device extends Fragment {
    String sharepackage = "com.kdd_hp_henry_morrison.mci.rescue";
    Context sharedAppContext = null;    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "pk";
    private String url2 = "http://140.120.183.205:8000/personaldetail/";
    private String urlupload = "http://140.120.183.205:8000/personaldetailedit/";
    // TODO: Rename and change types of parameters
    private int pk;
    private TextView deviceId;
    private EditText device_name;
    private Spinner device_sex,device_hospital,device_rescue;
    private Button device_birthday,device_upload;
    private Button device_age,device_height,device_weight;
    String sexlist[] = {"Female","Male","Unsure"};
    String sexlist1[] = {"F","M","U"};
    String sexlist2[] = {"女","男","不確定"};
    String hospitalist[] = { "請選擇","第一醫院", "台新醫院", "林森醫院", "宏恩醫院", "勝美醫院"};
    private OnFragmentInteractionListener mListener;
    deviceconstructor deviceconstructor1 = new deviceconstructor();
   

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment device.
     */
    // TODO: Rename and change types and number of parameters
    public static device newInstance(String param1, String param2) {
        device fragment = new device();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        Bundle bundle=getArguments();
        pk = bundle.getInt(ARG_PARAM1);
        device_rescue = (Spinner)view.findViewById(R.id.device_rescue);
        device_name= (EditText)view.findViewById(R.id.device_name);
        deviceId =  (TextView)view.findViewById(R.id.device_Deviceid);
        device_sex = (Spinner)view.findViewById(R.id.device_sex);
        device_hospital = (Spinner)view.findViewById(R.id.device_hospital);
        device_birthday = (Button)view.findViewById(R.id.device_birthday);
        device_age = (Button)view.findViewById(R.id.device_age);
        device_height = (Button)view.findViewById(R.id.device_height);
        device_weight = (Button)view.findViewById(R.id.device_weight);
        device_upload= (Button)view.findViewById(R.id.device_upload);
        device_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("pk",pk);
                    jsonObject.put("name",device_name.getText().toString());
                    jsonObject.put("rescue",device_rescue.getSelectedItem().toString());
                    jsonObject.put("sex",sexlist1[device_sex.getSelectedItemPosition()]);
                    jsonObject.put("birthdate",device_birthday.getText().toString());
                    jsonObject.put("age",Integer.valueOf(device_age.getText().toString()));
                    jsonObject.put("height",Double.valueOf(device_height.getText().toString()));
                    jsonObject.put("weight",Double.valueOf(device_weight.getText().toString()));
                    jsonObject.put("hospital",device_hospital.getSelectedItem().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        urlupload,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                savesharedata();
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
                        Toast.makeText(getContext(),"成功編輯",Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
        // Inflate the layout for this fragment
        getdevice(pk);
        return view;
    }

    private JSONObject jsonstring(JSONObject jsonObject,String mclass,String data) throws JSONException {

        if(data == ""){
            jsonObject.put(mclass,null);
        }
        else{
            jsonObject.put(mclass,data);
        }
        return jsonObject;
    }
    private JSONObject jsonname(JSONObject jsonObject,String mclass,String data) throws JSONException {

        if(data.equals("請輸入") ){
            jsonObject.put(mclass,null);
        }
        else{
            jsonObject.put(mclass,data);
        }
        return jsonObject;
    }
    private JSONObject jsonhospital(JSONObject jsonObject,String mclass,String data) throws JSONException {

        if(data.equals("請輸入")){
            jsonObject.put(mclass,null);
        }
        else{
            jsonObject.put(mclass,data);
        }
        return jsonObject;
    }
    private JSONObject jsonint(JSONObject jsonObject,String mclass,String data) throws JSONException {

        if(data.equals("請輸入")){
            jsonObject.put(mclass,null);
        }
        else{
            jsonObject.put(mclass,Integer.valueOf(data));
        }
        return jsonObject;
    }
    private JSONObject jsondouble(JSONObject jsonObject,String mclass,String data) throws JSONException {

        if(data.equals("請輸入")){
            jsonObject.put(mclass,null);
        }
        else{
            jsonObject.put(mclass,Double.valueOf(data));
        }
        return jsonObject;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void savesharedata(){
        try {
            SharedPreferences devicesshare = getContext().getSharedPreferences("devicesshare",0);
            String devicestr = devicesshare.getString("share","");
            ArrayList<devices> deviceslist = new ArrayList<>();
            Log.d("data",devicestr);
//        devicestr = "";
            if(devicestr!=""){
                deviceslist.clear();
                String sa[] = devicestr.split("\n");

                for(String s: sa){
                    String sd[] = s.split(",");
                    String devicearray[] = {"","","","","","","",""};
                    for(int i = 0 ;i < sd.length;i++){
                        devicearray[i] = sd[i] ;
                    }
//                    Log.d("dsdd",String.valueOf(devicearray.length));
                    devices devices = new devices();
                    devices.setId(devicearray[0]);
                    devices.setUpload(false);
                    if(devices.getPk()!=""){
                        devices.setPk(devicearray[1]);
                    }

                    else if(devicearray[2].equals("true")){
                        devices.setUpload(true);
                    }


                    devices.setName(devicearray[3]);


                    devices.setAge(devicearray[4]);


                    devices.setSex(devicearray[5]);


                    devices.setPerfusion(devicearray[6]);


                    devices.setRespirations(devicearray[7]);



                    deviceslist.add(devices);
                }

            }
            String save = "";
            for(devices devices: deviceslist){
                if(deviceconstructor1.getDeviceId().equals(devices.getId())){
                    String add = "";
                    add += devices.getId()+",";
                    add += String.valueOf(pk)+","+"true,";
                    add += device_name.getText().toString()+",";
                    add += device_age.getText().toString()+",";
//                    String sde = deviceconstructor1.getSex();
                    add += sexlist2[device_sex.getSelectedItemPosition()]+",";
                    add += devices.getPerfusion()+",";
                    add += devices.getRespirations();
                    save+=add+"\n";




                }
                else{
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

            }
            devicesshare.edit()
                    .putString("share", save)
                    .commit();
        } catch (Exception e) {
            Log.d("dsd","notsave");
            e.printStackTrace();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void getdevice(int pk){
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2+pk+"/",null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject1) {
                Log.d("tag", jsonObject1.toString());
                try {


                    try{
                        String resarray = jsonObject1.getString("rescuearray");
                        resarray = resarray.replace("[","");
                        resarray = resarray.replace("]","");
                        resarray = resarray.replace("\"","");
                        String[] split_line = resarray.split(",");
                        deviceconstructor1.rescuearray = new String[split_line.length];
                        for(int i =0;i < split_line.length;i++){
                            deviceconstructor1.rescuearray[i] =split_line[i];
                        }
                        deviceconstructor1.setRescuename(jsonObject1.getString("rescue"));
                        ArrayAdapter<String> rescueadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,deviceconstructor1.getRescuearray());
                        device_rescue.setAdapter(rescueadapter);
                        device_rescue.setSelection(rescueadapter.getPosition(deviceconstructor1.getRescuename()));
                    }catch (Exception e){

                    }
                    try {
                        deviceconstructor1.setDeviceId(jsonObject1.getString("DeviceId"));
                        deviceId.setText("DeviceId:"+" "+deviceconstructor1.getDeviceId());
                    }catch (Exception e){

                    }
                    try {
                        if(jsonObject1.getString("sex").equals("")) {
                            deviceconstructor1.setSex(sexlist1[2]);
                        }
                        else{
                            deviceconstructor1.setSex(jsonObject1.getString("sex"));
                        }
                        ArrayAdapter<String> sexadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,sexlist2);
                        device_sex.setAdapter(sexadapter);
                        device_sex.setSelection(sexadapter.getPosition(deviceconstructor1.getSex()));
                    }catch (Exception e){

                    }

                    try {
                        if(jsonObject1.get("name").equals(null))
                        {
                            deviceconstructor1.setName("");
                            device_name.setText("請輸入");
                        }
                        else{
                            deviceconstructor1.setName(jsonObject1.getString("name"));
                            device_name.setText(deviceconstructor1.getName());
                        }
                    }catch (Exception e){

                    }
                    try {

                        if(jsonObject1.get("birthdate").equals(null))
                        {
                            deviceconstructor1.setBirthday("");
                            device_birthday.setText("請輸入");

                        }
                        else{
                            deviceconstructor1.setBirthday(jsonObject1.getString("birthdate"));
                            device_birthday.setText(deviceconstructor1.getBirthday());

                        }


                        device_birthday.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deviceconstructor1.setBirthday(device_birthday.getText().toString());
                                device_DatePickerFragment newFragment = new device_DatePickerFragment();
                                int year = 1970,month = 1,day = 1;
                                Bundle bundle = new Bundle();
                                try {
                                    String data[] = deviceconstructor1.getBirthday().split("/");
                                    year = Integer.valueOf(data[0]);
                                    month = Integer.valueOf(data[1])-1;
                                    day = Integer.valueOf(data[2]);
                                }catch (Exception E){

                                }

                                bundle.putInt("year", year);
                                bundle.putInt("month", month);
                                bundle.putInt("day", day);
                                newFragment.setArguments(bundle);
                                newFragment.show(getActivity().getFragmentManager(),"DatePicker");

                            }
                        });

                    }catch (Exception e){

                    }
                    try {

                        if(jsonObject1.get("age").equals(null))
                        {
                            deviceconstructor1.setAge("");
                            device_age.setText("請輸入");
                        }
                        else{
                            deviceconstructor1.setAge(jsonObject1.getString("age"));
                            device_age.setText(deviceconstructor1.getAge());
                        }
                        device_age.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                numberpicker("年齡",120,1,deviceconstructor1.getAge(),device_age,1);
                            }
                        });
                    }catch (Exception e){

                    }
                    try {

                        if(jsonObject1.get("height").equals(null))
                        {
                            deviceconstructor1.setHeight("");
                            device_height.setText("請輸入");
                        }
                        else{
                            deviceconstructor1.setHeight(jsonObject1.getString("height"));
                            device_height.setText(deviceconstructor1.getHeight());
                        }
                        device_height.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                numberpicker("身高",220,60,deviceconstructor1.getHeight(),device_height,0);
                            }
                        });
                    }catch (Exception e){

                    }
                    try {

                        if(jsonObject1.get("weight").equals(null))
                        {
                            deviceconstructor1.setWeight("");
                            device_weight.setText("請輸入");
                        }
                        else{
                            deviceconstructor1.setWeight(jsonObject1.getString("weight"));
                            device_weight.setText(deviceconstructor1.getWeight());
                        }
                        device_weight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                numberpicker("體重",150,30,deviceconstructor1.getWeight(),device_weight,2);
                            }
                        });
                    }catch (Exception e){

                    }
                    try {
                        if(jsonObject1.get("hospital")==null){
                            deviceconstructor1.setHospital(hospitalist[0]);
                        }
                        else{
                            deviceconstructor1.setHospital(jsonObject1.getString("hospital"));
                        }

                        ArrayAdapter<String> hospitaldapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,hospitalist);
                        device_hospital.setAdapter(hospitaldapter);
                        device_hospital.setSelection(hospitaldapter.getPosition(deviceconstructor1.getHospital()));
                    }catch (Exception e){

                    }



                } catch (Exception e) {
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



//                DeviceId.setText(deviceconstructor1.getDeviceId());
                pDialog.hide();
            }
        });
    }
    private void numberpicker(String title, int max, int min, final String data, final Button button, final int type){
        int defaultvalue = 0;
        try{
            Double d = Double.valueOf(data);
            defaultvalue =d.intValue();
            Log.d("tag",""+defaultvalue);

        }catch (Exception e){
            defaultvalue = 0;

        }
        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getContext())
                .minValue(min)
                .maxValue(max)
                .defaultValue(defaultvalue)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        button.setText(String.valueOf(numberPicker.getValue()));
                        switch (type){
                            case 0:
                                deviceconstructor1.setHeight(String.valueOf(numberPicker.getValue()));
                            case 1:
                                deviceconstructor1.setAge(String.valueOf(numberPicker.getValue()));
                            case 2:
                                deviceconstructor1.setWeight(String.valueOf(numberPicker.getValue()));
                        }


                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }
    private class deviceconstructor{
        String rescuename,DeviceId,sex,hospital,name,birthday;
        String age;
        String height,weight;
        String rescuearray[];

        public String[] getRescuearray() {
            return rescuearray;
        }

        public void setRescuearray(String[] rescuearray) {
            this.rescuearray = rescuearray;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getWeight() {
            return weight;
        }

        public String getHeight() {
            return height;

        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getDeviceId() {
            return DeviceId;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setDeviceId(String deviceId) {
            DeviceId = deviceId;
        }


        public String getRescuename() {
            return rescuename;
        }

        public void setRescuename(String rescuename) {
            this.rescuename = rescuename;
        }
    }

}

