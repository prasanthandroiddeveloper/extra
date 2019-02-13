package com.tripNetra.extranet.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripNetra.extranet.Dashboard_Main_Act;
import com.tripNetra.extranet.Progress_Dialog;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.adapters.DataAdapter;
import com.tripNetra.extranet.adapters.Inventory_Pckg_Recycler_Adapter;
import com.tripNetra.extranet.adapters.Inventory_Recycler_Adapter;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.G_Class;
import com.tripNetra.extranet.utils.Utils;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Inventory_Main_Act extends AppCompatActivity {

    NiceSpinner hotelspinner,packspi;
    RecyclerView recycler;
    String uid,hotelid= "",hotelname,pname,id,SID,PName;
    Progress_Dialog prcsdlg;
    List<String> pkglist,idsList1,spid;
    //Spinner packspi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Inventory");

        hotelspinner = findViewById(R.id.hotelspiner);
        recycler = findViewById(R.id.recycler);
        packspi=findViewById(R.id.packspin);

        prcsdlg = new Progress_Dialog(this);
        Objects.requireNonNull(prcsdlg.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        prcsdlg.setCancelable(false);

        uid = ((G_Class) getApplicationContext()).getUserId();

        loadspinnerdata();
       // packspinnerdata();

    }

    public void loadspinnerdata(){

        prcsdlg.show();

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);

        Log.i("uid",uid);

        new VolleyRequester(this).ParamsRequest(1, Config.HOTEL_SPINNER_URL, prcsdlg, params, true, response -> {

            ArrayList<String> list = new ArrayList<>();
            final ArrayList<DataAdapter> datlist = new ArrayList<>();

            prcsdlg.dismiss();

            try {

                JSONArray jArray = new JSONArray(response);
                for(int i=0;i<= jArray.length();i++){
                    DataAdapter dataAdapter = new DataAdapter();
                    if(i==0){
                        dataAdapter.setHName("Select Hotel");
                        dataAdapter.setHotelId("0");
                        list.add("Select Hotel");
                    }else {
                        JSONObject jsonObject = jArray.getJSONObject(i-1);
                        dataAdapter.setHName(jsonObject.getString("hotel_name"));
                        dataAdapter.setHotelId(jsonObject.getString("hotel_details_id"));
                        list.add(jsonObject.getString("hotel_name"));
                    }
                    datlist.add(dataAdapter);
                }

                /*ArrayAdapter<String> adapter = new ArrayAdapter<>(Inventory_Main_Act.this, R.layout.textview_layout,R.id.textview,list);

                hotelspinner.setAdapter(adapter);*/
                hotelspinner.attachDataSource(list);
                hotelspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id) {
                        if(!datlist.get(position).getHotelId().equals("0")) {
                            hotelid = datlist.get(position).getHotelId();
                            hotelname = datlist.get(position).getHName();
                            roomtypes();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) { }
                });

            }catch (JSONException e) {

                e.printStackTrace();//"Something Went Wrong Try Again"
                Utils.setSingleBtnAlert(Inventory_Main_Act.this,e.getMessage(),"Ok",true);
            }
        });
    }


   /* public void packspinnerdata(){

        prcsdlg.show();

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);

        Log.i("uid",uid);

        new VolleyRequester(this).ParamsRequest(1, Config.PKG_SPINNER, prcsdlg, params, true, response -> {

            ArrayList<String> list1 = new ArrayList<>();
            final ArrayList<DataAdapter> datlist1 = new ArrayList<>();

            prcsdlg.dismiss();

            try {

                JSONArray jArray = new JSONArray(response);
                for(int i=0;i<= jArray.length();i++){
                    DataAdapter dataAdapter1 = new DataAdapter();
                    if(i==0){
                        dataAdapter1.setPkgName("Select Package");
                        dataAdapter1.setPId("0");
                        list1.add("Select package");
                    }else {
                        JSONObject jsonObject = jArray.getJSONObject(i-1);
                        dataAdapter1.setPkgName(jsonObject.getString("sightseen_name"));
                        dataAdapter1.setPId(jsonObject.getString("sightseen_id"));
                        list1.add(jsonObject.getString("sightseen_name"));
                    }
                    datlist1.add(dataAdapter1);
                }


                packspi.attachDataSource(list1);
                packspi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id) {
                        if(!datlist1.get(position).getPId().equals("0")) {
                            SID = datlist1.get(position).getPId();
                            PName = datlist1.get(position).getPkgName();
                            Log.i("s",SID);
                            Log.i("pn",PName);
                            packtypes();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) { }
                });

            }catch (JSONException e) {

                e.printStackTrace();//"Something Went Wrong Try Again"
                Utils.setSingleBtnAlert(Inventory_Main_Act.this,e.getMessage(),"Ok",true);
            }
        });
    }*/



    private void roomtypes() {

        prcsdlg.show();

        recycler.setLayoutManager(new LinearLayoutManager(this));

        Map<String, String> params = new HashMap<>();
        params.put("hotelid", hotelid);

        new VolleyRequester(this).ParamsRequest(1, Config.ROOM_SPINNER_URL, prcsdlg, params, true, response -> {
            prcsdlg.dismiss();

            if (response.contains("No Result")) {
                Utils.setSingleBtnAlert(Inventory_Main_Act.this, "No Results Found", "Ok", false);
            } else {
                ArrayList<DataAdapter> list = new ArrayList<>();

                try {
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {

                        DataAdapter dataAdapter = new DataAdapter();

                        JSONObject jsonObject = jArray.getJSONObject(i);

                        dataAdapter.setRType(jsonObject.getString("room_type_name"));
                        dataAdapter.setCinType("Availability : " + jsonObject.getString("no_of_room_available") + " Price : " + jsonObject.getString("sgl_price"));
                        dataAdapter.setRTypeId(jsonObject.getString("hotel_room_type_id"));
                        dataAdapter.setHName(hotelname);
                        dataAdapter.setRImage(jsonObject.getString("room_images"));
                        dataAdapter.setHotelId(hotelid);
                        list.add(dataAdapter);

                    }
                    recycler.setAdapter(new Inventory_Recycler_Adapter(list));

                } catch (JSONException e) {
                    //e.printStackTrace();
                    Utils.setSingleBtnAlert(Inventory_Main_Act.this, "Something Went Wrong Try Again", "Ok", true);
                }
            }
        });

    }

   /* private void packtypes() {

        prcsdlg.show();

        recycler.setLayoutManager(new LinearLayoutManager(this));

        Map<String, String> params = new HashMap<>();
        params.put("sightseen_id", SID);



        new VolleyRequester(this).ParamsRequest(1, "https://tripnetra.com/prasanth/androidphpfiles/extranet/packtype.php", prcsdlg, params, true, response -> {
            prcsdlg.dismiss();

            if (response.contains("No Result")) {
                Utils.setSingleBtnAlert(Inventory_Main_Act.this, "No Results Found", "Ok", false);
            } else {
                ArrayList<DataAdapter> list1 = new ArrayList<>();

                try {
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {

                        DataAdapter dataAdapter = new DataAdapter();

                        JSONObject jsonObject = jArray.getJSONObject(i);

                        dataAdapter.setDPrice(jsonObject.getString("darshan_price"));
                        dataAdapter.setDCount(jsonObject.getString("da_count"));
                        dataAdapter.setPId(SID);
                        dataAdapter.setPkgName(PName);
                        list1.add(dataAdapter);


                    }
                    recycler.setAdapter(new Inventory_Pckg_Recycler_Adapter(list1));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //   Utils.setSingleBtnAlert(Inventory_Main_Act.this, "Something Went Wrong Try Again", "Ok", true);
                }
            }
        });

    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Dashboard_Main_Act.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!hotelid.equals(""))roomtypes();
    }

}