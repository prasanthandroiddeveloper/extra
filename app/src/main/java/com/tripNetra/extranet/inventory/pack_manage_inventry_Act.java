package com.tripNetra.extranet.inventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tripNetra.extranet.R;

public class pack_manage_inventry_Act extends AppCompatActivity {
    Bundle b;
    String dprc,daval,sightid,sightname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pack_manage_inventry);
        b=getIntent().getExtras();
        assert b != null;
        dprc=b.getString("darshanprice");
        daval=b.getString("darshancunt");
        sightid=b.getString("sightseenid");
        sightname=b.getString("sightseenname");

        Toast.makeText(this,"values are"+dprc+"\n"+daval+"\n"+"\n"+sightid+"\n"+sightname,Toast.LENGTH_LONG).show();

    }
}
