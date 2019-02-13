package com.tripNetra.extranet.payments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tripNetra.extranet.Progress_Dialog;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.rest.VolleyRequester;
import com.tripNetra.extranet.utils.Config;
import com.tripNetra.extranet.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Payment_Voucher_Act extends AppCompatActivity {

    TextView PaystatusTV,BookStatTV,CinTV, CoutTV, TotpriceTV, RefidTV, TranstypTV, TrasdateTV, RemarksTV;
    String pnrno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pnrno = Objects.requireNonNull(getIntent().getExtras()).getString("pnrnumber");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_voucher);

        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Supplier Reports");

        PaystatusTV = findViewById(R.id.paystatusTV);
        BookStatTV = findViewById(R.id.BookstatTV);
        CinTV = findViewById(R.id.cindateTV);
        CoutTV = findViewById(R.id.coutTV);
        TotpriceTV = findViewById(R.id.totamtTV);
        RefidTV = findViewById(R.id.refidTV);
        TranstypTV = findViewById(R.id.transtypTV);
        TrasdateTV = findViewById(R.id.trasdateTV);
        RemarksTV = findViewById(R.id.remarksTV);

        reportvi();
    }

    @SuppressLint("SetTextI18n")
    public void reportvi() {
        final Progress_Dialog pd = new Progress_Dialog(this);
        pd.setCancelable(false);
        Objects.requireNonNull(pd.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pd.show();

        Map<String, String> params = new HashMap<>();
        params.put("pnrno", pnrno);

        new VolleyRequester(this).ParamsRequest(1, Config.PAYMENT_VOUCH_URL, pd, params, false, response -> {
            pd.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);

                switch (jObj.getString("status")) {
                    case "Paid":
                        PaystatusTV.setText("PAID");
                        PaystatusTV.setTextColor(Color.parseColor("#FF1D7117"));
                        break;
                    default:
                        PaystatusTV.setText("Processing");
                        PaystatusTV.setTextColor(Color.parseColor("#FFFF0000"));
                        break;
                }

                String status = jObj.getString("booking_status").toLowerCase();
                if (status.contains("confirm")) {
                    BookStatTV.setTextColor(Color.parseColor("#FF1D7117"));
                    BookStatTV.setText(R.string.confirmed);
                } else if (status.contains( "cancel")) {
                    BookStatTV.setTextColor(Color.parseColor("#FFFF0000"));
                    BookStatTV.setText(R.string.cancelled);
                } else {
                    BookStatTV.setText(status);
                }

                ((TextView) findViewById(R.id.BookidTV)).setText(jObj.getString("pnr_no"));
                ((TextView) findViewById(R.id.hotelnameTV)).setText(jObj.getString("hotel_name"));
                ((TextView) findViewById(R.id.roomtypeTV)).setText(jObj.getString("booking_room_type"));
                ((TextView) findViewById(R.id.nonightsTV)).setText(jObj.getString("no_of_nights"));
                ((TextView) findViewById(R.id.ebedsTV)).setText(jObj.getString("total_extra_beds"));

                if (jObj.getString("ref_id").equals("null") || jObj.getString("ref_id").equals("")) {
                    RefidTV.setText("-");
                    TranstypTV.setText("-");
                    TrasdateTV.setText("-");
                    RemarksTV.setText("-");
                } else {
                    RefidTV.setText(jObj.getString("ref_id"));
                    TranstypTV.setText(jObj.getString("neft_imps"));
                    TrasdateTV.setText(jObj.getString("date_and_time"));
                    RemarksTV.setText(jObj.getString("Remarks"));
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date d1 = sdf.parse(jObj.getString("check_in_date"));
                    Date d2 = sdf.parse(jObj.getString("check_out_date"));
                    sdf.applyPattern("dd-MMM-yyyy");
                    CinTV.setText(sdf.format(d1));
                    CoutTV.setText(sdf.format(d2));
                } catch (ParseException e) {
                    CinTV.setText(jObj.getString("check_in_date"));
                    CoutTV.setText(jObj.getString("check_out_date"));
                    e.printStackTrace();
                }

                if (jObj.getString("total_gst").equals("")) {
                    TotpriceTV.setText(jObj.getString("total_sgl_price") + " /-");
                } else {
                    DecimalFormat df = new DecimalFormat("0.0");
                    float price = Float.parseFloat(jObj.getString("total_sgl_price")),
                            commision = Float.parseFloat(jObj.getString("commission"));
                    float Hcomm = price*commision/100,HcPrice = 0,HGst = 0;

                    if (!jObj.getString("bh_gstin").equals("NotAvailable")) {
                        if (commision != 0) {HcPrice = Hcomm * 18 / 100;}
                        HGst = Float.parseFloat(jObj.getString("total_gst"));
                    }

                    TotpriceTV.setText(df.format(price - Hcomm - HcPrice + HGst) + " /-");
                }

            } catch (JSONException e) {
                //e.printStackTrace();
                Utils.setSingleBtnAlert(Payment_Voucher_Act.this,"Something Went Wrong Try Again","Ok",false);
            }
        });

    }

}