package com.tripNetra.extranet;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class Progress_Dialog extends Dialog {

    public Progress_Dialog(Context context) {
        super(context);
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.fragment_progress_dialog);
    }

}