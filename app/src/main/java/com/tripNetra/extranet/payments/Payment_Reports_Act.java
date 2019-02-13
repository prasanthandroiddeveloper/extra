package com.tripNetra.extranet.payments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tripNetra.extranet.Progress_Dialog;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.adapters.DataAdapter;
import com.tripNetra.extranet.adapters.Payment_Recycle_Adpater;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.G_Class;
import com.tripNetra.extranet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Payment_Reports_Act extends AppCompatActivity {

    List<DataAdapter> listAdapter;
    RecyclerView recycleView;
    Payment_Recycle_Adpater rvadapter;
    String tabstatus, uid,startdate=null,enddate=null,Suppemail;
    Button allbtn, paidbtn, procsbtn;
    FloatingActionButton fab;
    Progress_Dialog pro_dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        listAdapter = new ArrayList<>();

        setSupportActionBar(findViewById(R.id.toolbar));

        G_Class userId = (G_Class) getApplicationContext();
        G_Class suppmail = (G_Class) getApplicationContext();
        uid =   userId.getUserId();
        Suppemail =  suppmail.getMail();

        startdate = Objects.requireNonNull(getIntent().getExtras()).getString("sdate");
        enddate = getIntent().getExtras().getString("edate");

        recycleView = findViewById(R.id.recyclerView);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 || dy<0 && fab.isShown())
                    fab.hide();
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        allbtn = findViewById(R.id.allbutton);
        paidbtn = findViewById(R.id.cnfmbutton);
        procsbtn = findViewById(R.id.processingbutton);

        pro_dlg = new Progress_Dialog(this);
        Objects.requireNonNull(pro_dlg.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pro_dlg.setCancelable(false);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> sendmail());

        allbtn.setBackgroundResource(R.drawable.rounded_corner_blue_fill);allbtn.setTextColor(Color.parseColor("#ffffff"));
        tabstatus = "ALL";
        getreports();
    }

    public void mclear(){
        listAdapter.clear();
        if(rvadapter != null) {
            rvadapter.notifyDataSetChanged();
        }
    }

    public void all(View v){
        mclear();
        allbtn.setBackgroundResource(R.drawable.rounded_corner_blue_fill);allbtn.setTextColor(Color.parseColor("#ffffff"));
        paidbtn.setBackgroundResource(R.drawable.rounded_corner);paidbtn.setTextColor(Color.parseColor("#000000"));
        procsbtn.setBackgroundResource(R.drawable.rounded_corner);procsbtn.setTextColor(Color.parseColor("#000000"));
        tabstatus = "ALL";
        getreports();
    }

    public void paid(View v){
        mclear();
        paidbtn.setBackgroundResource(R.drawable.rounded_corner_blue_fill);paidbtn.setTextColor(Color.parseColor("#ffffff"));
        allbtn.setBackgroundResource(R.drawable.rounded_corner);allbtn.setTextColor(Color.parseColor("#000000"));
        procsbtn.setBackgroundResource(R.drawable.rounded_corner);procsbtn.setTextColor(Color.parseColor("#000000"));

        tabstatus = "PAID";
        getreports();
    }

    public void processing(View v){
        mclear();
        procsbtn.setBackgroundResource(R.drawable.rounded_corner_blue_fill);procsbtn.setTextColor(Color.parseColor("#ffffff"));
        allbtn.setBackgroundResource(R.drawable.rounded_corner);allbtn.setTextColor(Color.parseColor("#000000"));
        paidbtn.setBackgroundResource(R.drawable.rounded_corner);paidbtn.setTextColor(Color.parseColor("#000000"));

        tabstatus = "PROCESSING";
        getreports();
    }

    public void getreports(){

        pro_dlg.show();

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("status", tabstatus);
        params.put("startdate", startdate);
        params.put("enddate", enddate);

        new VolleyRequester(this).ParamsRequest(1, Config.PAYMENT_REPS_URL, pro_dlg, params, false, response -> {
            pro_dlg.dismiss();

            if(response.toLowerCase().contains("no result")){
                findViewById(R.id.nopetv).setVisibility(View.VISIBLE);
                recycleView.setVisibility(View.GONE);
                return;
            }

            findViewById(R.id.nopetv).setVisibility(View.GONE);
            recycleView.setVisibility(View.VISIBLE);

            try {
                JSONArray jarr = new JSONArray(response);
                for(int i = 0; i<jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject json = jarr.getJSONObject(i);

                    dataAdapter.setPnrNo(json.getString("pnr_no"));
                    dataAdapter.setBookStat(json.getString("status"));
                    dataAdapter.setNeftImps(json.getString("neft_imps"));
                    dataAdapter.setRType(json.getString("booking_room_type"));
                    dataAdapter.setCinDate(json.getString("check_in_date"));

                    if (json.getString("total_gst").equals("")) {
                        dataAdapter.setPrice(json.getString("total_sgl_price"));
                    } else {
                        DecimalFormat df = new DecimalFormat("0.0");
                        float price = Float.parseFloat(json.getString("total_sgl_price")),
                                commision = Float.parseFloat(json.getString("commission"));
                        float Hcomm = price * commision / 100, HcPrice = 0, HGst = 0;

                        if (!json.getString("bh_gstin").equals("NotAvailable")) {
                            if (commision != 0) {HcPrice = Hcomm * 18 / 100;}
                            HGst = Float.parseFloat(json.getString("total_gst"));
                        }

                        dataAdapter.setPrice(df.format(price - Hcomm - HcPrice + HGst));
                    }
                    dataAdapter.setRefId(json.getString("ref_id"));

                    listAdapter.add(dataAdapter);

                }

                rvadapter = new Payment_Recycle_Adpater(listAdapter);
                recycleView.setAdapter(rvadapter);

            }catch (JSONException e) {
                //e.printStackTrace();
                Utils.setSingleBtnAlert(Payment_Reports_Act.this,"No details Found","Ok",true);
            }
        });

    }

    public void sendmail(){

        pro_dlg.show();

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("suppliermail", Suppemail);
        params.put("startdate", startdate);
        params.put("enddate", enddate);

        new VolleyRequester(this).ParamsRequest(1, Config.SEND_MAIL_URL, pro_dlg, params, false, response -> {
            pro_dlg.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    Utils.setSingleBtnAlert(Payment_Reports_Act.this,"Mail Sent to \n" + Suppemail,"Ok",false);
                } else {
                    Utils.setSingleBtnAlert(Payment_Reports_Act.this,"Mail Sending Failed to \n" + Suppemail,"Ok",false);
                }
            }catch (JSONException e) {
                //e.printStackTrace();
                Utils.setSingleBtnAlert(Payment_Reports_Act.this,"Something Went Wrong Please Try Again","Ok",false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(rvadapter!=null) {
                    rvadapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}