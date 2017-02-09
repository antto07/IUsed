package com.iused.introduction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.iused.R;
import com.iused.fcm.Config;
import com.iused.fcm.NotificationUtils;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Antto on 06-10-2016.
 */
public class RegisterActivity extends AppCompatActivity implements AsyncTaskListener{

    private EditText edt_name= null;
    private EditText edt_email= null;
    private EditText edt_mobile_number= null;
    private EditText edt_country= null;
    private Button btn_sign_up= null;;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    private String str_json_message= null;
    private SharedPreferences pref = null;
    String regId = null;
    private CircleImageView imageView_profile_pic=null;
    String currentDateTimeString = "";
    String locale ="";
    String timezone="";
    Intent intent= null;
    private static final int MY_PERMISSIONS_CALL = 104;
    private TextView txt_mobile_code=null;

//    private TelephonyManager tel;
//    private static String imei = null;

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressDialog progressDialog= null;
    private String str_photo;
    private String str_fbid;
    private String str_signup_by;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        edt_name= (EditText) findViewById(R.id.edt_name);
        edt_email= (EditText) findViewById(R.id.edt_email);
        edt_mobile_number= (EditText) findViewById(R.id.edt_mobile);
        edt_country= (EditText) findViewById(R.id.edt_country);
        btn_sign_up= (Button) findViewById(R.id.btn_sign_up);
        imageView_profile_pic= (CircleImageView) findViewById(R.id.img_profile_pic);
        txt_mobile_code= (TextView) findViewById(R.id.txt_mobile_code);

        progressDialog= new ProgressDialog(RegisterActivity.this);
        listener = RegisterActivity.this;
        intent=getIntent();
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (pref != null)


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message);
//                    Log.e("fcm_message",message);
                }
            }
        };

        displayFirebaseRegId();

        locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentDateTimeString = formatter.format(today);
        timezone=TimeZone.getDefault().getDisplayName();

//        Log.e("locale",locale);
//        Log.e("time",currentDateTimeString);
//        Log.e("timezone",timezone);

        str_photo=intent.getStringExtra("photo");
        str_fbid=intent.getStringExtra("id");
        str_signup_by=intent.getStringExtra("Type");

        edt_name.setText(intent.getStringExtra("name"));
        edt_email.setText(intent.getStringExtra("email"));
        try {
            edt_country.setText(locale);
        }catch (Exception e){
            edt_country.setText("India");
        }
        txt_mobile_code.setText(pref.getString("country_code",""));
//        Log.e("curr_code",pref.getString("country_code",""));


//        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        imei = tel.getDeviceId().toString();

//        TimeZone timezone = TimeZone.getDefault();
//        String TimeZoneName = timezone.getDisplayName();
//        int TimeZoneOffset = timezone.getRawOffset()/(60 * 60 * 1000);
//        Log.e("Time_zone","My Time Zone" + TimeZoneName + " : " +String.valueOf(TimeZoneOffset));

        try {
            Picasso.with(getApplicationContext())
                    .load(intent.getStringExtra("photo"))
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView_profile_pic,new PicassoCallback(intent.getStringExtra("photo")));
        }catch (Exception e){
            Picasso.with(getApplicationContext())
                    .load("http://52.41.70.254/pics/user.jpg")
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView_profile_pic,new PicassoCallback("http://52.41.70.254/pics/user.jpg"));
        }

//        if(intent.getStringExtra("photo").equalsIgnoreCase("")){
//            Picasso.with(getApplicationContext())
//                    .load("http://52.41.70.254/pics/user.jpg")
//                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
//                    //.error(R.drawable.user_placeholder_error)
//                    .into(imageView_profile_pic,new PicassoCallback("http://52.41.70.254/pics/user.jpg"));
//        }


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_mobile_number.getWindowToken(), 0);

                if(edt_mobile_number.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please Enter the Mobile Number",Toast.LENGTH_SHORT).show();
                }
                else {

                    if (Build.VERSION.SDK_INT >= 23) {
                        //Marshmallow
                        // demo();
                        marshmellowPermission();
                        //  mainTask();
                    } else {

                        para = new HashMap<>();
                        para.put("Email", edt_email.getText().toString());
                        para.put("Phone", txt_mobile_code.getText().toString()+edt_mobile_number.getText().toString());
                        para.put("Name",edt_name.getText().toString());
                        para.put("Longitude",pref.getString("user_lang",""));
                        para.put("Latitude",pref.getString("user_lat",""));
                        para.put("DeviceType","2");
                        para.put("DeviceId",pref.getString("imei",""));
                        para.put("DeviceToken",regId);
                        para.put("Photo",str_photo);
                        para.put("Timezon",timezone);
                        para.put("Created_dt",currentDateTimeString);
                        para.put("FbId",str_fbid);
                        para.put("SignupBy",str_signup_by);
                        progressDialog.setMessage("Registering...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Log.e("para_sign_up", para.toString());
                        HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"customerSignup", para, 2, "sign_up");
                        httpAsync1.execute();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for Activity#requestPermissions for more details.
//                            return;
//                        }
                        }

                    }

                }
            }
        });



    }

    private void marshmellowPermission() {

        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_CALL);
        } else {
            para = new HashMap<>();
            para.put("Email", edt_email.getText().toString());
            para.put("Phone", txt_mobile_code.getText().toString()+edt_mobile_number.getText().toString());
            para.put("Name",edt_name.getText().toString());
            para.put("Longitude",pref.getString("user_lang",""));
            para.put("Latitude",pref.getString("user_lat",""));
            para.put("DeviceType","2");
            para.put("DeviceId",pref.getString("imei",""));
            para.put("DeviceToken",regId);
            para.put("Photo",str_photo);
            para.put("Timezon",timezone);
            para.put("Created_dt",currentDateTimeString);
            para.put("FbId",str_fbid);
            para.put("SignupBy",str_signup_by);
            progressDialog.setMessage("Registering...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.e("para_sign_up", para.toString());
            HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"customerSignup", para, 2, "sign_up");
            httpAsync1.execute();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return;
//                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    para = new HashMap<>();
                    para.put("Email", edt_email.getText().toString());
                    para.put("Phone", txt_mobile_code.getText().toString()+edt_mobile_number.getText().toString());
                    para.put("Name",edt_name.getText().toString());
                    para.put("Longitude",pref.getString("user_lang",""));
                    para.put("Latitude",pref.getString("user_lat",""));
                    para.put("DeviceType","2");
                    para.put("DeviceId",pref.getString("imei",""));
                    para.put("DeviceToken",regId);
                    para.put("Photo",str_photo);
                    para.put("Timezon",timezone);
                    para.put("Created_dt",currentDateTimeString);
                    para.put("FbId",str_fbid);
                    para.put("SignupBy",str_signup_by);
                    progressDialog.setMessage("Registering...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Log.e("para_sign_up", para.toString());
                    HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"customerSignup", para, 2, "sign_up");
                    httpAsync1.execute();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for Activity#requestPermissions for more details.
//                            return;
//                        }
                    }

                } else {
//                    finish();
                    Toast.makeText(getApplicationContext(),"Allow permission to continue",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    class PicassoCallback implements Callback {

        String thumbnail_poster;

        public PicassoCallback(String thumbnail) {
            this.thumbnail_poster=thumbnail;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {
//            loadRottenPoster=true;
            Picasso.with(getApplicationContext()).load("http://52.41.70.254/pics/user.jpg").into(imageView_profile_pic);
        }
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
        Log.e("firebase_id","Firebase Reg Id: " + regId);
        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
        Log.e("firebase_id","Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        progressDialog.dismiss();
        if(result.equalsIgnoreCase("fail")){
            Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
        }
        else {
            if(tag.equalsIgnoreCase("sign_up")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        str_json_message= jsonObject.getString("errFlag");
                        if(str_json_message.equalsIgnoreCase("0")){

//                            SharedPreferences.Editor edit = pref.edit();
//                            edit.putString("user_id",jsonObject.getString("UserId"));
//                            edit.commit();

                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_SHORT).show();


                            if(intent.getStringExtra("offer_negotiable").equalsIgnoreCase("negotiable")){

                                Intent intent = new Intent(getApplicationContext(),MobileVerifyActivity.class);
                                intent.putExtra("offer_negotiable","negotiable");
                                intent.putExtra("user_id",jsonObject.getString("UserId"));
                                intent.putExtra("mobile",edt_mobile_number.getText().toString());
                                intent.putExtra("email",edt_email.getText().toString());
                                intent.putExtra("photo",str_photo);
                                intent.putExtra("user_name",edt_name.getText().toString());
                                intent.putExtra("regid",regId);
                                intent.putExtra("mobile_code",txt_mobile_code.getText().toString());
                                intent.putExtra("timezone",timezone);
                                intent.putExtra("created_at",currentDateTimeString);
                                intent.putExtra("fbid",str_fbid);
                                intent.putExtra("signup",str_signup_by);
//                            Log.e("intent_reg",intent.toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else if(intent.getStringExtra("offer_negotiable").equalsIgnoreCase("sell_product")){
                                Intent intent = new Intent(getApplicationContext(),MobileVerifyActivity.class);
                                intent.putExtra("offer_negotiable","sell_product");
                                intent.putExtra("user_id",jsonObject.getString("UserId"));
                                intent.putExtra("mobile",edt_mobile_number.getText().toString());
                                intent.putExtra("email",edt_email.getText().toString());
                                intent.putExtra("photo",str_photo);
                                intent.putExtra("user_name",edt_name.getText().toString());
                                intent.putExtra("regid",regId);
                                intent.putExtra("mobile_code",txt_mobile_code.getText().toString());
                                intent.putExtra("timezone",timezone);
                                intent.putExtra("created_at",currentDateTimeString);
                                intent.putExtra("fbid",str_fbid);
                                intent.putExtra("signup",str_signup_by);
//                            Log.e("intent_reg",intent.toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else if(intent.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_product")){
                                Intent intent = new Intent(getApplicationContext(),MobileVerifyActivity.class);
                                intent.putExtra("offer_negotiable","donate_product");
                                intent.putExtra("user_id",jsonObject.getString("UserId"));
                                intent.putExtra("mobile",edt_mobile_number.getText().toString());
                                intent.putExtra("email",edt_email.getText().toString());
                                intent.putExtra("photo",str_photo);
                                intent.putExtra("user_name",edt_name.getText().toString());
                                intent.putExtra("regid",regId);
                                intent.putExtra("mobile_code",txt_mobile_code.getText().toString());
                                intent.putExtra("timezone",timezone);
                                intent.putExtra("created_at",currentDateTimeString);
                                intent.putExtra("fbid",str_fbid);
                                intent.putExtra("signup",str_signup_by);
//                            Log.e("intent_reg",intent.toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else if(intent.getStringExtra("offer_negotiable").equalsIgnoreCase("non-negotiable")){
                                Intent intent = new Intent(getApplicationContext(),MobileVerifyActivity.class);
                                intent.putExtra("offer_negotiable","non-negotiable");
                                intent.putExtra("user_id",jsonObject.getString("UserId"));
                                intent.putExtra("mobile",edt_mobile_number.getText().toString());
                                intent.putExtra("email",edt_email.getText().toString());
                                intent.putExtra("photo",str_photo);
                                intent.putExtra("user_name",edt_name.getText().toString());
                                intent.putExtra("regid",regId);
                                intent.putExtra("mobile_code",txt_mobile_code.getText().toString());
                                intent.putExtra("timezone",timezone);
                                intent.putExtra("created_at",currentDateTimeString);
                                intent.putExtra("fbid",str_fbid);
                                intent.putExtra("signup",str_signup_by);
//                            Log.e("intent_reg",intent.toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else if(intent.getStringExtra("offer_negotiable").equalsIgnoreCase("none")){
                                Intent intent = new Intent(getApplicationContext(),MobileVerifyActivity.class);
                                intent.putExtra("offer_negotiable","none");
                                intent.putExtra("user_id",jsonObject.getString("UserId"));
                                intent.putExtra("mobile",edt_mobile_number.getText().toString());
                                intent.putExtra("email",edt_email.getText().toString());
                                intent.putExtra("photo",str_photo);
                                intent.putExtra("user_name",edt_name.getText().toString());
                                intent.putExtra("regid",regId);
                                intent.putExtra("mobile_code",txt_mobile_code.getText().toString());
                                intent.putExtra("timezone",timezone);
                                intent.putExtra("created_at",currentDateTimeString);
                                intent.putExtra("fbid",str_fbid);
                                intent.putExtra("signup",str_signup_by);
//                            Log.e("intent_reg",intent.toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else if(intent.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_offer")){
                                Intent intent = new Intent(getApplicationContext(),MobileVerifyActivity.class);
                                intent.putExtra("offer_negotiable","donate_offer");
                                intent.putExtra("user_id",jsonObject.getString("UserId"));
                                intent.putExtra("mobile",edt_mobile_number.getText().toString());
                                intent.putExtra("email",edt_email.getText().toString());
                                intent.putExtra("photo",str_photo);
                                intent.putExtra("user_name",edt_name.getText().toString());
                                intent.putExtra("regid",regId);
                                intent.putExtra("mobile_code",txt_mobile_code.getText().toString());
                                intent.putExtra("timezone",timezone);
                                intent.putExtra("created_at",currentDateTimeString);
                                intent.putExtra("fbid",str_fbid);
                                intent.putExtra("signup",str_signup_by);
//                            Log.e("intent_reg",intent.toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
