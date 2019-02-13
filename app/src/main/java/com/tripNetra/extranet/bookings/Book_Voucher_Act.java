package com.tripNetra.extranet.bookings;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripNetra.extranet.Progress_Dialog;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Book_Voucher_Act extends AppCompatActivity implements View.OnClickListener {

    String pnrno, pnr_Id, fcmpnrno,fcmpnrid,status;
    TextView InvoicedateTv, BdateTv, CindateTv, CoutTv, NoNighTv, StatusTv, TripPaybTv, PaybrknameTv, NoRoomTv,datetv;
    Button CancelBtn,confirm;
    Progress_Dialog pdlg;
    LinearLayout fv;
    Integer cid;
    String stats="CANCELLED",constats="CONFIRMED";
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_voucher);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Supplier Voucher");

        assert getIntent().getExtras() != null;
        pnrno = getIntent().getExtras().getString("pnrnumber");

        pnr_Id = getIntent().getExtras().getString("id");

        fcmpnrno = getIntent().getExtras().getString("pnr");
        fcmpnrid = getIntent().getExtras().getString("fid");

        requestQueue = Volley.newRequestQueue(Book_Voucher_Act.this);


        pdlg = new Progress_Dialog(this);
        pdlg.setCancelable(false);
        Objects.requireNonNull(pdlg.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

        InvoicedateTv = findViewById(R.id.invoicedateTV);
        BdateTv = findViewById(R.id.bookdateTV);
        CindateTv = findViewById(R.id.cindateTV);
        CoutTv = findViewById(R.id.coutTV);
        NoNighTv = findViewById(R.id.nonightsTV);
        NoRoomTv = findViewById(R.id.noroomsTV);
        StatusTv = findViewById(R.id.statusTV);
        PaybrknameTv = findViewById(R.id.PaybrkNameTv);
        TripPaybTv = findViewById(R.id.trippayblTV);
        CancelBtn = findViewById(R.id.cancelbtn);
        confirm = findViewById(R.id.finalc);
        fv = findViewById(R.id.footerView);
        vouchervi();
        confirm.setOnClickListener(this);
        CancelBtn.setOnClickListener(v -> cancelbook());


    }

    @Override
    public void onClick(View v) {
        cid=v.getId();
        Log.i("cid", String.valueOf(cid));

        fnotconfirm();
    }

    @SuppressLint("SetTextI18n")
    private void vouchervi() {

        pdlg.show();
        Map<String, String> params = new HashMap<>();
        if (pnrno != null && pnr_Id != null) {
            params.put("pnrno", pnrno);
            params.put("pnrid", pnr_Id);

        } else {
            params.put("pnrno", fcmpnrno);
            params.put("pnrid", fcmpnrid);
        }

        new VolleyRequester(this).ParamsRequest(1, Config.BOOK_VOUCHER_URL, pdlg, params, false, response -> {

            pdlg.dismiss();
            try {
                JSONObject jobj = new JSONObject(response);

                pnr_Id = jobj.getString("id");
                status = jobj.getString("booking_status");
                String Supplier_conf = jobj.getString("supplier_confirmation");
                String cdate = jobj.getString("check_in_date");

                Log.i("sta",status );
                ((TextView) findViewById(R.id.invoicenoTV)).setText(jobj.getString("id"));
                ((TextView) findViewById(R.id.hotgstnoTV)).setText("Hotel GSTIN No : " + jobj.getString("hotel_gstin"));
                ((TextView) findViewById(R.id.hotelnameTV)).setText(jobj.getString("hotel_name"));
                ((TextView) findViewById(R.id.guestnameTV)).setText(jobj.getString("contact_fname") + " " + jobj.getString("contact_lname"));
                ((TextView) findViewById(R.id.mobileTV)).setText(jobj.getString("contact_mobile_number"));
                ((TextView) findViewById(R.id.expectcinTV)).setText(jobj.getString("exp_checkin_time"));
                 NoNighTv.setText(jobj.getString("no_of_nights"));
                ((TextView) findViewById(R.id.BookidTV)).setText(jobj.getString("pnr_no"));
                ((TextView) findViewById(R.id.roomtypeTV)).setText(jobj.getString("booking_room_type"));
                 NoRoomTv.setText(jobj.getString("no_of_rooms"));
                ((TextView) findViewById(R.id.noguestsTV)).setText(jobj.getString("adult") + " ADULTS & " + jobj.getString("child") + " CHILD");
                ((TextView) findViewById(R.id.ebedsTV)).setText(jobj.getString("extra_bed_count"));

                LinearLayout PriceNogstLayt = findViewById(R.id.pricelayt), PriceGstLayT = findViewById(R.id.pricegstlayt);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String todaysdate = dateFormat.format(date);

                Log.i("todaysdate",todaysdate);
                Log.i("Supplier_conf",Supplier_conf);

                if (status != null ) {

                    if(Supplier_conf.equals("") && status.contains("CONFIRM") && cdate.compareTo(todaysdate) >= 0) {
                        fv.setVisibility(View.VISIBLE);
                    }
                }else {
                    fv.setVisibility(View.GONE);
                }

                if (jobj.getString("total_gst").equals("")) {

                    PriceGstLayT.setVisibility(View.GONE);
                    PriceNogstLayt.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.priceTV)).setText(jobj.getString("total_sgl_price") + "/-");
                    TripPaybTv.setText(jobj.getString("total_sgl_price") + "/-");

                } else {

                    PriceNogstLayt.setVisibility(View.GONE);
                    PriceGstLayT.setVisibility(View.VISIBLE);
                    DecimalFormat df = new DecimalFormat("0.0");
                    float price = Float.parseFloat(jobj.getString("total_sgl_price")),
                            commision = Float.parseFloat(jobj.getString("commission"));
                    float Hcomm = price * commision / 100;
                    float HcPrice = 0, HGst = 0;
                    ((TextView) findViewById(R.id.totalchargeTV)).setText(df.format(price) + " /-");

                    int dumm = Integer.parseInt(NoNighTv.getText().toString()) * Integer.parseInt(NoRoomTv.getText().toString());
                    if (jobj.getString("bh_gstin").equals("NotAvailable") && !jobj.getString("hotel_type_id").equals("4")
                            && (price / dumm >= 1000)) {
                        TextView pricegstif = findViewById(R.id.totpricegstifTv);
                        pricegstif.setVisibility(View.VISIBLE);
                    }

                    LinearLayout TGstLayT = findViewById(R.id.TgstLayt),
                            HgstLayT = findViewById(R.id.HgstLayt),
                            TcommLayT = findViewById(R.id.TcommLayt);

                    if (commision != 0) {

                        TcommLayT.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.tripcomsnTV)).setText(df.format(Hcomm) + " /-");
                        if (!jobj.getString("bh_gstin").equals("NotAvailable")) {
                            TGstLayT.setVisibility(View.VISIBLE);
                            HcPrice = Hcomm * 18 / 100;
                            ((TextView) findViewById(R.id.tripgstTV)).setText(df.format(HcPrice) + " /-");
                        }

                    }

                    if (!jobj.getString("bh_gstin").equals("NotAvailable")) {
                        HgstLayT.setVisibility(View.VISIBLE);
                        HGst = Float.parseFloat(jobj.getString("total_gst"));
                        ((TextView) findViewById(R.id.hotgstTV)).setText(df.format(HGst) + " /-");
                    }

                    float dummTotal = price - Hcomm - HcPrice + HGst;
                    TripPaybTv.setText(df.format(dummTotal) + " /-");

                    if (!jobj.getString("complimentary_amount").equals("") && !jobj.getString("complimentary_amount").equals("0")) {
                        LinearLayout HCompLayt1 = findViewById(R.id.HCompLayt1), HCompLayt2 = findViewById(R.id.HCompLayt2);
                        HCompLayt1.setVisibility(View.VISIBLE);
                        HCompLayt2.setVisibility(View.VISIBLE);
                        TextView totalTv = findViewById(R.id.CompTotTV), CompChargeTv = findViewById(R.id.CompRChargeTV);
                        float dcomp = Float.parseFloat(jobj.getString("complimentary_amount"));
                        totalTv.setText(df.format(dummTotal) + " /-");
                        CompChargeTv.setText(df.format(dcomp) + " /-");
                        TripPaybTv.setText(df.format(dummTotal - dcomp) + " /-");
                    }
                }

                if (jobj.getString("hotel_type_id").equals("4")) {
                    PaybrknameTv.setText("Supplier Donation");
                } else {
                    PaybrknameTv.setText("Supplier Payment Breakup");
                }


                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    Date d1 = sdf.parse(jobj.getString("book_date"));
                    Date d2 = sdf.parse(jobj.getString("check_in_date"));
                    Date d3 = sdf.parse(jobj.getString("check_out_date"));
                    sdf.applyPattern("dd-MMM-yyyy");

                    BdateTv.setText(sdf.format(d1));
                    InvoicedateTv.setText(sdf.format(d1));
                    CindateTv.setText(sdf.format(d2));
                    CoutTv.setText(sdf.format(d3));
                } catch (ParseException e) {
                    BdateTv.setText(jobj.getString("book_date"));
                    InvoicedateTv.setText(jobj.getString("book_date"));
                    CindateTv.setText(jobj.getString("check_in_date"));
                    CoutTv.setText(jobj.getString("check_out_date"));

                }


                if (status.toLowerCase().contains("confirm")) {

                    StatusTv.setTextColor(Color.parseColor("#FF1D7117"));
                    StatusTv.setText(R.string.confirmed);

                    if (jobj.getString("cancel_request").equals("true")) {
                        CancelBtn.setText("Cancellation Raised");
                        CancelBtn.setEnabled(false);
                    }

                    try {
                        Date d2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(jobj.getString("check_in_date"));

                        Date today = Calendar.getInstance().getTime();

                        if (d2.compareTo(today) >= 0) {
                            CancelBtn.setVisibility(View.VISIBLE);
                        }

                    } catch (ParseException ignored) {
                    }



                } else if (status.toLowerCase().contains("cancel")) {
                    StatusTv.setTextColor(Color.parseColor("#FFFF0000"));
                    StatusTv.setText(R.string.cancelled);
                } else {
                    StatusTv.setText(status);
                }



            } catch (JSONException e) {
                Utils.setSingleBtnAlert(Book_Voucher_Act.this, "Something Went Wrong Try Again", "Ok", true);
                Toast.makeText(this,"json"+ e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void cancelbook() {

        pdlg.show();

        Map<String, String> params = new HashMap<>();
        params.put("pnr_id", pnr_Id);
        params.put("supplier_confirmation",stats);
        Log.i("params", String.valueOf(params));
        new VolleyRequester(this).ParamsRequest(1, "https://tripnetra.com/cpanel_admin/booking/cancellation_request/"+pnr_Id+"/req/hotel/Supplier/6865446727eae9cbd513/"+stats, pdlg, params, false, response -> {
            pdlg.dismiss();

            if (response.toLowerCase().contains("success")) {
                fv.setVisibility(View.GONE);
            } else {
                Utils.setSingleBtnAlert(Book_Voucher_Act.this, "Booking Not Cancelled", "Ok", false);
            }
        });

    }



    public void fnotconfirm() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/cpanel_admin/booking/cancellation_request/"+pnr_Id+"/req/hotel/Supplier/6865446727eae9cbd513/"+constats , ServerResponse -> {
            if( ServerResponse.toLowerCase().contains("success")){
                fv.setVisibility(View.GONE);
            }
            Toast.makeText(Book_Voucher_Act.this, ServerResponse, Toast.LENGTH_LONG).show();
        },
                volleyError -> Toast.makeText(Book_Voucher_Act.this, volleyError.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                switch (cid) {
                    case R.id.finalc:
                        params.put("supplier_confirmation",constats);
                        break; }

                if ( pnr_Id != null) {
                    params.put("id", pnr_Id);
                } else {
                    params.put("id", fcmpnrid);
                }

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Book_Voucher_Act.this);
        requestQueue.add(stringRequest);

    }


}