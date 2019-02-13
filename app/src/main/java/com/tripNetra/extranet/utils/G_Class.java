package com.tripNetra.extranet.utils;

import android.app.Application;

public class G_Class extends Application {

    String userId,suppmail;

    public String getUserId(){
        return userId;
    }

    public void setUserId(String user_id){
        this.userId = user_id;
        }

    public String getMail(){
        return suppmail;
    }

    public void setMail(String user_mail){
        this.suppmail = user_mail;
    }

}