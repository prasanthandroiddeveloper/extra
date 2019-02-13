package com.tripNetra.extranet.utils;

import com.tripNetra.extranet.BuildConfig;

public class Config {
  private static final String BASE_URL = BuildConfig.Baserl;
 // private static final String BASE_URL = "https://tripnetra.com/prasanth/androidphpfiles/extranet/";
  public static final String PKG_SPINNER = BASE_URL+"packspinner.php";
    public static final String BOOK_REPORTS_URL = BASE_URL+"bookingreports.php";
    public static final String BOOK_VOUCHER_URL = BASE_URL+"bookingvoucher.php";
    //public static final String CANCEL_URL = BASE_URL+"cancelbooking.php";
   // public static final String CANCEL_URL = https://tripnetra.com/mamatha/cpanel_admin/booking/cancellation_request/".$pnr_id."/req/hotel/Supplier/6865446727eae9cbd513/".$supplier_confirmation";
    public static final String LOGIN_URL = BASE_URL+"extralogin.php";
    public static final String HOURBASE_URL = BASE_URL+"hoursbase.php";
    public static final String HOTEL_SPINNER_URL = BASE_URL+"hotelspinner.php";
    public static final String ROOM_SPINNER_URL = BASE_URL+"roomspinner.php";
    public static final String UPDATE_ROOM_URL = BASE_URL+"updateroomavail.php";
    public static final String PAYMENT_REPS_URL = BASE_URL+"paymentreports.php";
    public static final String PAYMENT_VOUCH_URL = BASE_URL+"paymentvoucher.php";
    public static final String SEND_MAIL_URL = BASE_URL+"sendmail.php";
    public static final String CHANGE_PASS = BASE_URL+"changepassword.php";
    public static final String PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.tripnetra.tripnetraextranet&hl=en";
}
