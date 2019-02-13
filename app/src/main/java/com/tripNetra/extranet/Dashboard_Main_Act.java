package com.tripNetra.extranet;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tripNetra.extranet.bookings.Book_Voucher_Act;
import com.tripNetra.extranet.bookings.Booking_Main_Act;
import com.tripNetra.extranet.inventory.Inventory_Main_Act;
import com.tripNetra.extranet.payments.Payment_Main_Act;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.G_Class;
import com.tripNetra.extranet.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Dashboard_Main_Act extends AppCompatActivity {

    Session session;
    String Userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logoaction);
        actionBar.setDisplayUseLogoEnabled(true);

       /*Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String number = bundle.getString("pnr");

            Log.i("value",number);
            if (number != null) {

                startActivity(new Intent(Dashboard_Main_Act.this, Book_Voucher_Act.class));
            }
        }*/

        session = new Session(this);

        Userid = session.getSId();
        ((G_Class) getApplicationContext()).setUserId(Userid);
        ((G_Class) getApplicationContext()).setMail(session.getSName());

        new GetVersionCode(this).execute();


    }

    public void bookinrep(View v){startActivity(new Intent(this, Booking_Main_Act.class)); }

    public void inventory(View v){startActivity(new Intent(this, Inventory_Main_Act.class)); }

    public void pricingrep(View v){startActivity(new Intent(this, Payment_Main_Act.class)); }

    public void conatctmthd(View v){ new Contact_DFrag().show(getSupportFragmentManager(),"cotactus"); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        new AlertDialog.Builder(this).setMessage("Do you Want to Logout")
                .setPositiveButton("Yes", (dialog, id) -> {
                    session.setLoggedin(false);
                    session.ClearAll();

                    Intent intent = new Intent(Dashboard_Main_Act.this, Login_Main_Act.class);
                    intent.putExtra("finish", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, id) -> {})
                .setCancelable(true).create().show();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }

    public void changepass(View view) { new Change_Pass_Frag().show(getSupportFragmentManager(),"changepassword");}

    public void updatepass(Bundle bb){

        final Progress_Dialog pd = new Progress_Dialog(this);
        pd.setCancelable(false);
        Objects.requireNonNull(pd.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pd.show();

        Map<String, String> params = new HashMap<>();
        params.put("userid",Userid);
        params.put("old_password",bb.getString("old_password"));
        params.put("new_password",bb.getString("new_password"));

        new VolleyRequester(this).ParamsRequest(1, Config.CHANGE_PASS, pd, params, false, response -> {
            pd.dismiss();
            try {
                  /*if(jsonobject.getString("status").toLowerCase().contains("success")){
                       Toast.makeText(MainActivity.this,jsonobject.getString("message"),Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(MainActivity.this,"Invalid Old Password",Toast.LENGTH_SHORT).show();
                   }*/

                Toast.makeText(Dashboard_Main_Act.this,new JSONObject(response).getString("message"),Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(Dashboard_Main_Act.this,"Something Wrong , Try Again",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static class GetVersionCode extends AsyncTask<Void, String, String> {

        private WeakReference<Dashboard_Main_Act> activityReference;

        GetVersionCode(Dashboard_Main_Act context) {activityReference = new WeakReference<>(context);}

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return Jsoup.connect(Config.PLAYSTORE_URL).timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com").get().select("div[itemprop=softwareVersion]").first().ownText();
            } catch (Exception e) {return "";}
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            final Dashboard_Main_Act activity = activityReference.get();
            String Currentversion = BuildConfig.VERSION_NAME.replace(".", "");
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                onlineVersion = onlineVersion.replace(".", "");
                if (Integer.parseInt(Currentversion) < Integer.parseInt(onlineVersion)) {
                    new AlertDialog.Builder(activity).setTitle("Udapte").setMessage("A New Version is Available")
                            .setPositiveButton("Update", (dialog, id) ->
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tripnetra.tripnetraextranet"))))
                            .setNegativeButton("Not Now", (dialogInterface, i) -> {

                            }).setCancelable(false).create().show();
                }
            }
        }

    }

}