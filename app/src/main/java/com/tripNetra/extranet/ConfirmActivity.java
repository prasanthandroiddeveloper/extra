package com.tripNetra.extranet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Bundle bundle;
        bundle = getIntent().getExtras();
        if (bundle != null) {
          String aa =  bundle.getString("getTitle");

         /* aa.substring(Integer.parseInt("@"));*/

            String substr = aa.substring(aa.indexOf("@"));
            String substr1 = substr.substring(substr.indexOf("T"));
          //  Log.i("substr1", String.valueOf(substr1));
        }

    }

    public void ok(View view) {

        Intent intent= new Intent(getApplicationContext(),Dashboard_Main_Act.class);
        startActivity(intent);
    }
}
