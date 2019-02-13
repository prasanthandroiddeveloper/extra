package com.tripNetra.extranet.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public Session(Context ctx){
        prefs = ctx.getSharedPreferences("Tn_Extranet", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public void setDets(String id,String name){
        editor.putString("sup_id",id);
        editor.putString("sup_name",name);
        editor.commit();
    }

    public String getSId(){ return  prefs.getString("sup_id",""); }

    public String getSName(){ return  prefs.getString("sup_name",""); }

    public boolean loggedin(){ return prefs.getBoolean("loggedInmode", false); }

    public void ClearAll(){
        editor.clear();
        editor.apply();
    }

}
