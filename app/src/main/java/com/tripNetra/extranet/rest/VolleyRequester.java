package com.tripNetra.extranet.rest;

import android.app.Activity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripNetra.extranet.Progress_Dialog;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.utils.Utils;

import java.util.Map;

public class VolleyRequester {

    private Activity activity;
    private RequestQueue requestQueue;

    public VolleyRequester(Activity activit) {
        this.activity = activit;
        requestQueue = Volley.newRequestQueue(activit);
    }

    public void ParamsRequest(int method, String JsonURL, final Progress_Dialog loaddlg, final Map<String, String> params, final Boolean close,
                              final callback_interface callback) {

        StringRequest stringRequest = new StringRequest(method, JsonURL,
                callback::getResponse, error -> {
                    //error.printStackTrace();
                    if(loaddlg != null ){if(loaddlg.isShowing())loaddlg.dismiss();}
                    String ss ;
                    if( error instanceof NetworkError) { ss = "Network ";
                    } else if( error instanceof ServerError) { ss = "Server ";
                    } else if( error instanceof TimeoutError) {ss = "TimeOut ";
                    }else {ss = ""; }
                    Utils.setSingleBtnAlert(activity,ss+activity.getResources().getString(R.string.error_occur),"Ok",close);

                }) {
            @Override
            public Map<String, String> getParams() {//getParams()
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(80000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}