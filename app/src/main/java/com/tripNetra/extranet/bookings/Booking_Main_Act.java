package com.tripNetra.extranet.bookings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tripNetra.extranet.Dashboard_Main_Act;
import com.tripNetra.extranet.Progress_Dialog;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.adapters.Booking_Recycle_Adapter;
import com.tripNetra.extranet.adapters.DataAdapter;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.Date_dialog;
import com.tripNetra.extranet.utils.G_Class;
import com.tripNetra.extranet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Booking_Main_Act extends AppCompatActivity  {

    List<DataAdapter> listAdapter;
    RecyclerView recyclerView;
    Booking_Recycle_Adapter recycleAdapter;
    Button b1,b2,b3;Boolean Gtemp = true;
    String tabstatus,uid,date;
    JSONArray Confirm_response,All_reponse,Cancel_response;
    FloatingActionButton fab;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        G_Class userId = (G_Class) getApplicationContext();
        uid =   userId.getUserId();

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fabbtn);
        b1 = findViewById(R.id.allbutton);
        b2 = findViewById(R.id.cnfmbutton);
        b3 = findViewById(R.id.cancelbutton);

        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        listAdapter = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 || dy<0 && fab.isShown()){fab.hide();}
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){fab.show();}
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fab.setOnClickListener(view -> {

            long now = System.currentTimeMillis() - 1000;

            new Date_dialog(this,now-31536000000L,now+31536000000L).DateDialog(dat -> {
                date = sdf.format(dat);
                Gtemp=false;
                mclear();
                bookings();
            });
        });

        tabstatus = "CONFIRM";
        b2.setBackgroundResource(R.drawable.rounded_corner_blue_fill);b2.setTextColor(Color.parseColor("#ffffff"));

        date = sdf.format(new Date());
        bookings();

    }

    public void mclear(){
        listAdapter.clear();
        if(recycleAdapter != null) {
            recycleAdapter.notifyDataSetChanged();
        }
    }

    public void all(View v){
        mclear();
        date = "0000-00-00";
        b1.setBackgroundResource(R.drawable.rounded_corner_blue_fill);b1.setTextColor(Color.parseColor("#ffffff"));
        b2.setBackgroundResource(R.drawable.rounded_corner);b2.setTextColor(Color.parseColor("#000000"));
        b3.setBackgroundResource(R.drawable.rounded_corner);b3.setTextColor(Color.parseColor("#000000"));
        tabstatus = "PROCESS";
        if(All_reponse==null) {
            bookings();
        }else {
            parsedata(All_reponse);
        }
    }

    public void confirm(View v){
        mclear();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        b2.setBackgroundResource(R.drawable.rounded_corner_blue_fill);b2.setTextColor(Color.parseColor("#ffffff"));
        b1.setBackgroundResource(R.drawable.rounded_corner);b1.setTextColor(Color.parseColor("#000000"));
        b3.setBackgroundResource(R.drawable.rounded_corner);b3.setTextColor(Color.parseColor("#000000"));
        tabstatus = "CONFIRM";
        if(Confirm_response==null) {
            bookings();
        }else {
            parsedata(Confirm_response);
        }
    }

    public void canceled(View v){
        mclear();
        date = "0000-00-00";
        b3.setBackgroundResource(R.drawable.rounded_corner_blue_fill);b3.setTextColor(Color.parseColor("#ffffff"));
        b2.setBackgroundResource(R.drawable.rounded_corner);b2.setTextColor(Color.parseColor("#000000"));
        b1.setBackgroundResource(R.drawable.rounded_corner);b1.setTextColor(Color.parseColor("#000000"));
        tabstatus = "CANCELLED";
        if(Cancel_response==null) {
            bookings();
        }else {
            parsedata(Cancel_response);
        }
    }

    public void bookings(){
        final Progress_Dialog pd = new Progress_Dialog(this);
        pd.setCancelable(false);
        Objects.requireNonNull(pd.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pd.show();

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("status", tabstatus);
        params.put("dbdate", date);

        new VolleyRequester(this).ParamsRequest(1, Config.BOOK_REPORTS_URL, pd, params, false, response -> {
            pd.dismiss();

            if(response.toLowerCase().contains("no result")){
                findViewById(R.id.nopetv).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return;
            }

            findViewById(R.id.nopetv).setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            try {
                JSONArray jarr = new JSONArray(response);
                if(Gtemp) {
                    switch (tabstatus) {
                        case "PROCESS":
                            All_reponse = jarr;
                            break;
                        case "CONFIRM":
                            Confirm_response = jarr;
                            break;
                        case "CANCELLED":
                            Cancel_response = jarr;
                            break;
                    }
                }else{
                    Gtemp=true;
                }
                parsedata(jarr);
            } catch (JSONException e) {
                //e.printStackTrace();
                Utils.setSingleBtnAlert(Booking_Main_Act.this,"No details Found","Ok",false);
            }
        });

    }

    private void parsedata(JSONArray jsonArray) {
        try {
            for(int i = 0; i<jsonArray.length(); i++) {
                DataAdapter dataAdapter = new DataAdapter();

                JSONObject json = jsonArray.getJSONObject(i);

                dataAdapter.setFname(json.getString("contact_fname")+" "+json.getString("contact_lname"));
                dataAdapter.setBookStat(json.getString("booking_status"));
                dataAdapter.setMobile(json.getString("contact_mobile_number"));
                dataAdapter.setRType(json.getString("no_of_rooms")+" - "+json.getString("booking_room_type"));
                dataAdapter.setCinDate(json.getString("check_in_date"));
                dataAdapter.setCoutDate(json.getString("check_out_date"));
                dataAdapter.setPnrNo(json.getString("pnr_no"));
                dataAdapter.setHotelId(json.getString("id"));

                if(!json.getString("complimentary_amount").equals( "") &&!json.getString("complimentary_amount").equals( "0")) {
                    dataAdapter.setRefId("COMP");
                }else{
                    dataAdapter.setRefId("");}
                listAdapter.add(dataAdapter);

            }

            recycleAdapter = new Booking_Recycle_Adapter(listAdapter);
            recyclerView.setAdapter(recycleAdapter);


        }catch (JSONException e) {
            bookings();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                if(recycleAdapter!=null) {recycleAdapter.getFilter().filter(newText);}
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {return super.onOptionsItemSelected(item);}

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Dashboard_Main_Act.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}