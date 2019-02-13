package com.tripNetra.extranet.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

public class Utils {

    private static AlertDialog myAlertDialog;
    private static AlertDialog.Builder alertDialogBuilder = null;
    public static boolean isDataConnectionAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void setSingleBtnAlert(final Activity activity, String msg, String btnName, final boolean finshact) {
        if (myAlertDialog != null && myAlertDialog.isShowing()) {
            myAlertDialog.dismiss();
            myAlertDialog = null;
        }
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setCancelable(false).setMessage(msg).setPositiveButton(btnName, (dialog, which) -> {
            dialog.dismiss();
            if (finshact) {
                activity.finish();
            }
        });
        activity.runOnUiThread(() -> {
            if (!activity.isFinishing()) {
                Utils.myAlertDialog = Utils.alertDialogBuilder.create();
                Utils.myAlertDialog.show();
            }
        });
    }
}
