package com.tripNetra.extranet.payments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tripNetra.extranet.R;
import com.tripNetra.extranet.utils.Date_dialog;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Payment_Main_Act extends AppCompatActivity {

    Button tobtn,frombtn;
    String startdate="",enddate="";
    long ftdate ;
    SimpleDateFormat dbFormat,dispFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Supplier Payment Reports");

        tobtn = findViewById(R.id.tobtn);
        frombtn = findViewById(R.id.frombtn);

        dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dispFormat = new SimpleDateFormat("dd-MMM\nyyyy", Locale.getDefault());
    }

    public void stdate(View v) {
        ftdate = System.currentTimeMillis() - 31536000000L;
        new Date_dialog(this,ftdate,System.currentTimeMillis() - 1000).DateDialog(date ->{

            ftdate = date.getTime();
            startdate = dbFormat.format(date);
            enddate = dbFormat.format(date);
            frombtn.setText(dispFormat.format(date));
            tobtn.setText(dispFormat.format(date));

        });
    }

    public void eddate(View v) {
        if (startdate.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Select From Date", Toast.LENGTH_SHORT).show();
        } else {
            new Date_dialog(this,ftdate,System.currentTimeMillis() - 1000).DateDialog(date ->{

                enddate = dbFormat.format(date);
                tobtn.setText(dispFormat.format(date));

            });
        }
    }

    public void verifydata(View view) {
        if (startdate.equals("")){
            Toast.makeText(getApplicationContext(),"Select From Date", Toast.LENGTH_SHORT).show();
        } else if (enddate.equals("")){
            Toast.makeText(getApplicationContext(),"Select To Date", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(Payment_Main_Act.this, Payment_Reports_Act.class);
            intent.putExtra("sdate",startdate);
            intent.putExtra("edate",enddate);
            startActivity(intent);
        }
    }

}