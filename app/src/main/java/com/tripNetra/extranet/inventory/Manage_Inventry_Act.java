package com.tripNetra.extranet.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tripNetra.extranet.Progress_Dialog;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.Date_dialog;
import com.tripNetra.extranet.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Manage_Inventry_Act extends AppCompatActivity {

    TextView FDateTv,TDateTv,HnameTv,RtypeTv;
    EditText AvailEt,PriceEt;
    String Hid,Hname,Rid,RName,endDate,startDate,Availbty,RPrice;
    SimpleDateFormat sdfmain,sdfdisp;
    Boolean blockinv=false;
    int cur=0;long ftdate,maxdate = System.currentTimeMillis() - 1000+31536000000L;
    Progress_Dialog pdlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);

        FDateTv = findViewById(R.id.fromTV);
        TDateTv = findViewById(R.id.toTV);
        HnameTv = findViewById(R.id.hnameTv);
        RtypeTv = findViewById(R.id.RtypeTV);
        AvailEt = findViewById(R.id.AvlRoomET);
        PriceEt = findViewById(R.id.RoomPriceET);
        Switch BlockSwitch = findViewById(R.id.blockswitch);

        Hid = Objects.requireNonNull(getIntent().getExtras()).getString("hotelid");
        Hname = getIntent().getExtras().getString("hotelname");
        Rid = getIntent().getExtras().getString("hotelroomid");
        RName = getIntent().getExtras().getString("roomname");

        pdlg = new Progress_Dialog(this);
        pdlg.setCancelable(false);
        Objects.requireNonNull(pdlg.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pdlg.show();

        HnameTv.setText(Hname);
        RtypeTv.setText(RName);

        sdfmain = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdfdisp = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        startDate = sdfmain.format(cal.getTime());
        FDateTv.setText(sdfdisp.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        endDate = sdfmain.format(cal.getTime());
        TDateTv.setText(sdfdisp.format(cal.getTime()));

        BlockSwitch.setOnCheckedChangeListener((compoundButton, on) -> {
            if(on){
                AvailEt.setEnabled(false);PriceEt.setEnabled(false);
                AvailEt.setText("0");
                blockinv=true;
            } else {
                AvailEt.setEnabled(true);PriceEt.setEnabled(true);
                blockinv=false;
            }
        });

        getroomdata();

    }

    public void fromdatemthd(View v){
        cur = 1;
        ftdate = System.currentTimeMillis() - 1000;
        new Date_dialog(this,ftdate,maxdate).DateDialog(date ->{

                startDate = sdfmain.format(date);
                ftdate = date.getTime();

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, 1);
                endDate = sdfmain.format(cal.getTime());
                FDateTv.setText(sdfdisp.format(date));
                TDateTv.setText(sdfdisp.format(cal.getTime()));

                todatemthd(v);

        });
    }

    public void todatemthd(View v){
        if(cur==0) { ftdate = System.currentTimeMillis() - 1000+86400000L; }
        new Date_dialog(this,ftdate,maxdate).DateDialog(date ->{
            endDate = sdfmain.format(date);
            TDateTv.setText(sdfdisp.format(date));
            getroomdata();
        });
    }

    public void getroomdata(){
        pdlg.show();

        Map<String, String> params = new HashMap<>();
        params.put("type", "get");
        params.put("fromdate", startDate);
        params.put("hotelid", Hid);
        params.put("roomtypeid",Rid);

        new VolleyRequester(this).ParamsRequest(1, Config.UPDATE_ROOM_URL, pdlg, params, true, response -> {
            pdlg.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                AvailEt.setText(jObj.getString("no_of_room_available"));
                PriceEt.setText(jObj.getString("sgl_price"));
            }catch (JSONException e) {
                //e.printStackTrace();
                Utils.setSingleBtnAlert(Manage_Inventry_Act.this,"Something Went Wrong Try Again","Ok",false);
            }
        });
    }

    public void submitdata(View v){

        Availbty=AvailEt.getText().toString();
        RPrice=PriceEt.getText().toString();

        if (Availbty.equals("")) {
            Toast.makeText(getApplicationContext(),"Please Enter Availability", Toast.LENGTH_SHORT).show();
        }else if(RPrice.equals("")) {
            Toast.makeText(getApplicationContext(),"Please Enter Room Price", Toast.LENGTH_SHORT).show();
        }else{

            pdlg.show();

            Map<String, String> params = new HashMap<>();
            params.put("type", "post");
            params.put("fromdate", startDate);
            params.put("hotelid", Hid);
            params.put("roomtypeid",Rid);
            params.put("todate", endDate);
            params.put("roomprice", RPrice);
            params.put("roomcount",Availbty);
            params.put("blockhotel", blockinv ? "true" : "false");

            new VolleyRequester(this).ParamsRequest(1, Config.UPDATE_ROOM_URL, pdlg, params, true, response -> {
                pdlg.dismiss();
                Utils.setSingleBtnAlert(Manage_Inventry_Act.this,response,"Ok",false);

                new AlertDialog.Builder(Manage_Inventry_Act.this)
                        .setCancelable(true).setMessage(response)
                        .setPositiveButton("Ok", (dialog, which) -> {
                            startActivity(new Intent(Manage_Inventry_Act.this,Inventory_Main_Act.class));
                            dialog.dismiss();
                        });

            });

        }
    }

}