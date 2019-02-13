package com.tripNetra.extranet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tripNetra.extranet.bookings.Book_Voucher_Act;

public class SplashScreen_Act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getSupportActionBar() != null;
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        Bundle bundle = getIntent().getExtras();


        new Thread(){
            @Override
            public void run(){
                try {
                    sleep(1500);
                    startActivity(new Intent(getApplicationContext(),Login_Main_Act.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }.start();
    }
}
