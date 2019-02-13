package com.tripNetra.extranet;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class Privacy_Policy_Act extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        WebView webvw = findViewById(R.id.webView);
        webvw.getSettings().setBuiltInZoomControls(true);
        webvw.loadUrl("https://tripnetra.com/privacy-policy");
    }


}