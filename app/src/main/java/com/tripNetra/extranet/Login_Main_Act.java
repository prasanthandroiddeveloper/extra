package com.tripNetra.extranet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.Session;
import com.tripNetra.extranet.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login_Main_Act extends Activity {

    Session session;
    String password,supplier,FcmToken="";
    TextInputLayout supTil,pwdTil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        supTil = findViewById(R.id.supidTIL);
        pwdTil = findViewById(R.id.passwdTIL);

       // FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this, instanceIdResult -> FcmToken = instanceIdResult.getToken());
        session = new Session(this);
        if(session.loggedin()){
            startActivity(new Intent(this,Dashboard_Main_Act.class));
            finish();
        }

      /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this, instanceIdResult -> FcmToken = instanceIdResult.getToken());*/


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Login_Main_Act.this, instanceIdResult -> {
             FcmToken = instanceIdResult.getToken();
            Log.e("newToken",FcmToken);
          //  Toast.makeText(this, "token is:"+FcmToken, Toast.LENGTH_LONG).show();

        });

        Log.i("FcmToken",FcmToken);
    }

    public void verifydata(View v) {
        supplier = ((EditText) findViewById(R.id.suppET)).getText().toString();
        password = ((EditText) findViewById(R.id.pwdET)).getText().toString();

        supTil.setErrorEnabled(false);
        pwdTil.setErrorEnabled(false);

        if (supplier.equals("")){
            supTil.setError(" Enter Supplier Id");
        } else if(password.equals("")){
            pwdTil.setError(" Enter Password");
        }else if(FcmToken.equals("")){
            Toast.makeText(this,"System Busy",Toast.LENGTH_SHORT).show();
        }else {
            Map<String, String> params = new HashMap<>();
            params.put("supplier", supplier);
            params.put("password", password);
            params.put("FireToken", FcmToken);
            Log.i("pa", String.valueOf(params));

            final Progress_Dialog pd = new Progress_Dialog(this);
            pd.setCancelable(false);
            Objects.requireNonNull(pd.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pd.show();

            new VolleyRequester(this).ParamsRequest(1, Config.LOGIN_URL, pd, params, false, response -> {
                pd.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        session.setDets(jObj.getString("uid"), supplier);
                        session.setLoggedin(true);
                        startActivity(new Intent(Login_Main_Act.this, Dashboard_Main_Act.class));
                    } else {
                        Toast.makeText(Login_Main_Act.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Utils.setSingleBtnAlert(Login_Main_Act.this, "Something Went Wrong Try Again", "Ok", false);

                }
            });
        }
    }

    public void privacypol(View v) {startActivity(new Intent(this, Privacy_Policy_Act.class));}

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }

}