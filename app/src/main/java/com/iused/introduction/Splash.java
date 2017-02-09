package com.iused.introduction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.iused.R;
import com.iused.adapters.LocationAdapter;
import com.iused.bean.LocationBean;
import com.iused.main.MainActivity;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.GPS_Service;
import com.iused.utils.HttpAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 2/11/2016.
 */
public class Splash extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AsyncTaskListener, com.google.android.gms.location.LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 200;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 101;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 102;
    private static int SPLASH_TIME_OUT = 10000;
    private static String imei = "";
//    ProgressDialog progressDialog = null;
    String Address1 = "";
    String Address2 = "";
    String City = "";
    String State = "";
    String Country = "";
    private GoogleApiClient googleApiClient = null;
    private HashMap<String, String> para = null;
    private HashMap<String, String> para_location = null;
    private AsyncTaskListener listener1 = null;
    private Context context = null;
    private LocationManager manager = null;
    private String str_latitude = "";
    private String str_longitude = "";
    private SharedPreferences mpref = null;
    private Snackbar snackbar = null;
    private View sbView = null;
    private TextView textView = null;
    private CoordinatorLayout coordinatorLayout;
    //    private Typeface face,face1=null;
    private String str_user_id = null;
    private SharedPreferences pref = null;
    private TelephonyManager tel;
    private LocationRequest locationRequest = null;
    private RelativeLayout layoutLocationSearch = null;
    private EditText edtLocationSearch = null;
    private ListView listLocation = null;
    private ImageView imgScalingLogo = null;
    private ImageView imgFetchingLocation = null;
    private Animation scalingAnimation = null;
    private Animation jumpingUp = null;
    private Animation jumpingDown = null;
    private Animation slideUp = null;
    private Animation slideDown = null;


    private String url = null;

    private List<String> sugestion = null;
    private List<LocationBean> locationBean = null;
    private Double city_latitude = 0.0;
    private Double city_longitude = 0.0;
    private HashMap<String, String> parameters_location = null;
    private HashMap<String, String> parameters_guest = null;
    public static String short_name = "";
    int status;
    Intent browserIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imgScalingLogo = (ImageView) findViewById(R.id.img_logo);
        imgFetchingLocation = (ImageView) findViewById(R.id.fetch_location);
        layoutLocationSearch = (RelativeLayout) findViewById(R.id.layout_location_search);
        edtLocationSearch = (EditText) findViewById(R.id.edt_location);
        listLocation = (ListView) findViewById(R.id.list_location);

        scalingAnimation = AnimationUtils.loadAnimation(Splash.this, R.anim.scaling_logo);
        jumpingUp = AnimationUtils.loadAnimation(Splash.this, R.anim.jumping_up);
        jumpingDown = AnimationUtils.loadAnimation(Splash.this, R.anim.jumping_down);
        slideUp = AnimationUtils.loadAnimation(Splash.this, R.anim.bottom_up);
        slideDown = AnimationUtils.loadAnimation(Splash.this, R.anim.bottom_down);

        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable( getApplicationContext());

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (pref != null)
            str_user_id = pref.getString("user_id", "");

//        nextActivity();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);

        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        listener1 = Splash.this;
//        progressDialog = new ProgressDialog(Splash.this);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        scalingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgFetchingLocation.setAnimation(jumpingUp);
                imgFetchingLocation.startAnimation(jumpingUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        jumpingUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgFetchingLocation.setAnimation(jumpingDown);
                imgFetchingLocation.startAnimation(jumpingDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        jumpingDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgFetchingLocation.setAnimation(jumpingUp);
                imgFetchingLocation.startAnimation(jumpingUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layoutLocationSearch.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(edtLocationSearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtLocationSearch.getWindowToken(), 0);
                layoutLocationSearch.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        edtLocationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s.toString().replace(" ", "%20") + "&location=&radius=50&sensor=true&key=AIzaSyDhphpswcw1UOEQ9pZRAVzv1DpBQ9if--Y";
                parser parser = new parser();
                parser.execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                para_location = new HashMap<>();
                para_location.put("placeid",locationBean.get(position).getPlace_id());
                para_location.put("key","AIzaSyDhphpswcw1UOEQ9pZRAVzv1DpBQ9if--Y");
                Log.e("parameters_location",para_location.toString());
//                progressDialog.setMessage("Loading...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
                new HttpAsync(getApplicationContext(), listener1,"https://maps.googleapis.com/maps/api/place/details/json?" , para_location, 1, "city_latitude").execute();

            }
        });

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
//            Splashtask();
        }
        else if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
        else if (Build.VERSION.SDK_INT >= 23) {
            //Marshmallow

            if(status == ConnectionResult.SUCCESS) {
                //alarm to go and install Google Play Services
//                Toast.makeText(getApplicationContext(),"you have google play service",Toast.LENGTH_SHORT).show();
                Splashtask();
            }else if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
//                Toast.makeText(getApplicationContext(),"please udpate your google play service",Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog=new AlertDialog.Builder(Splash.this).setMessage("Please Update Your Google Play Service to Continue")
                        .setCancelable(false)
                        .setPositiveButton("Update Play Service", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
                                startActivity(browserIntent);
                                dialog.dismiss();
                            }
                        }).show();
            }

        } else {
            if(status == ConnectionResult.SUCCESS) {
                //alarm to go and install Google Play Services
//                Toast.makeText(getApplicationContext(),"you have google play service",Toast.LENGTH_SHORT).show();
                Splashtask();
            }else if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
//                Toast.makeText(getApplicationContext(),"please udpate your google play service",Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog=new AlertDialog.Builder(Splash.this).setMessage("Please Update Your Google Play Service to Continue")
                        .setCancelable(false)
                        .setPositiveButton("Update Play Service", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
                                startActivity(browserIntent);
                                dialog.dismiss();
                            }
                        }).show();
            }
        }


    }

    private void Splashtask() {

            googleApiClient = new GoogleApiClient.Builder(Splash.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            findViewById(R.id.location_marker).setVisibility(View.VISIBLE);
                            imgScalingLogo.setAnimation(scalingAnimation);
                            imgScalingLogo.startAnimation(scalingAnimation);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, Splash.this);
                            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, Splash.this);
                            }
                            else

                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        Splash.this, 1000);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;


                    }
                }
            });


        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tel.getDeviceId().toString();
        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        SharedPreferences.Editor ed = mpref.edit();
        ed.putString("imei", imei);
        ed.commit();

        para = new HashMap<>();
        para.put("latlng", str_latitude + "," + str_longitude);
        para.put("sensor", "true");
        Log.e("para_loc_name", para.toString());
        HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, "http://maps.googleapis.com/maps/api/geocode/json?", para, 1, "map");
        httpAsync1.execute();


    }

    private void imeiForMarshmellow() {
        // Marshmallow+
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Splash.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Splash.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            if(status == ConnectionResult.SUCCESS) {
                //alarm to go and install Google Play Services
//                Toast.makeText(getApplicationContext(),"you have google play service",Toast.LENGTH_SHORT).show();
                Splashtask();
            }else if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
//                Toast.makeText(getApplicationContext(),"please udpate your google play service",Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog=new AlertDialog.Builder(Splash.this).setMessage("Please Update Your Google Play Service to Continue")
                        .setCancelable(false)
                        .setPositiveButton("Update Play Service", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
                                startActivity(browserIntent);
                                dialog.dismiss();
                            }
                        }).show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made

                        if(status == ConnectionResult.SUCCESS) {
                            //alarm to go and install Google Play Services
//                Toast.makeText(getApplicationContext(),"you have google play service",Toast.LENGTH_SHORT).show();
                            Splashtask();
                        }else if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
//                Toast.makeText(getApplicationContext(),"please udpate your google play service",Toast.LENGTH_SHORT).show();
                            AlertDialog alertDialog=new AlertDialog.Builder(Splash.this).setMessage("Please Update Your Google Play Service to Continue")
                                    .setCancelable(false)
                                    .setPositiveButton("Update Play Service", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
                                            startActivity(browserIntent);
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
//
//                        new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//                            @Override
//                            public void run() {
//                                // This method will be executed once the timer is over
//                                // Start your app main activity
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                        // TODO: Consider calling
//                                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                                        // here to request the missing permissions, and then overriding
//                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                        //                                          int[] grantResults)
//                                        // to handle the case where the user grants the permission. See the documentation
//                                        // for Activity#requestPermissions for more details.
//                                        return;
//                                    }
//                                }
//
//                                tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                                imei = tel.getDeviceId().toString();
//
//
//                                mpref = getSharedPreferences("user_details", MODE_PRIVATE);
//                                SharedPreferences.Editor ed = mpref.edit();
//                                ed.putString("user_lat", str_latitude);
//                                ed.putString("user_lang", str_longitude);
//                                ed.putString("imei", imei);
//                                ed.commit();
//
//                                progressDialog = new ProgressDialog(Splash.this);
//
////                                para = new HashMap<>();
////                                para.put("latlng", gps.getLatitude() + "," + gps.getLongitude());
////                                para.put("sensor", "true");
////                                Log.e("para_loc_name", para.toString());
////                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, "http://maps.googleapis.com/maps/api/geocode/json?", para, 1, "map");
////                                httpAsync1.execute();
//
//                                if (str_user_id.equalsIgnoreCase("")) {
//                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                }
//
//                                context = getApplicationContext();
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                                        // TODO: Consider calling
//                                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                                        // here to request the missing permissions, and then overriding
//                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                        //                                          int[] grantResults)
//                                        // to handle the case where the user grants the permission. See the documentation
//                                        // for Activity#requestPermissions for more details.
//                                        Toast.makeText(getApplicationContext(), "Please switch on permission in settings", Toast.LENGTH_SHORT);
//
//                                        return;
//                                    }
//                                }
////                                tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
////                                imei = tel.getDeviceId().toString();
////                                Log.e("imei", imei);
//
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//                                        // TODO: Consider calling
//                                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                                        // here to request the missing permissions, and then overriding
//                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                        //                                          int[] grantResults)
//                                        // to handle the case where the user grants the permission. See the documentation
//                                        // for Activity#requestPermissions for more details.
//                                        return;
//                                    }
//                                }
//
//
//                            }
//                        }, SPLASH_TIME_OUT);


                        break;
                    }

                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
//                        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
                        layoutLocationSearch.setAnimation(slideUp);
                        layoutLocationSearch.startAnimation(slideUp);
                        break;
                    default: {
                        break;
                    }
                }

                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imeiForMarshmellow();

                } else {

                    finish();
                    Toast.makeText(getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(status == ConnectionResult.SUCCESS) {
                        //alarm to go and install Google Play Services
//                Toast.makeText(getApplicationContext(),"you have google play service",Toast.LENGTH_SHORT).show();
                        Splashtask();
                    }else if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
//                Toast.makeText(getApplicationContext(),"please udpate your google play service",Toast.LENGTH_SHORT).show();
                        AlertDialog alertDialog=new AlertDialog.Builder(Splash.this).setMessage("Please Update Your Google Play Service to Continue")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
                                        startActivity(browserIntent);
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                } else {
                    finish();
                    Toast.makeText(getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(status == ConnectionResult.SUCCESS) {
                        //alarm to go and install Google Play Services
//                Toast.makeText(getApplicationContext(),"you have google play service",Toast.LENGTH_SHORT).show();
                        Splashtask();
                    }else if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
//                Toast.makeText(getApplicationContext(),"please udpate your google play service",Toast.LENGTH_SHORT).show();
                        AlertDialog alertDialog=new AlertDialog.Builder(Splash.this).setMessage("Please Update Your Google Play Service to Continue")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
                                        startActivity(browserIntent);
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                } else {

                    finish();
                }
                break;
        }
    }


    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

//        progressDialog.dismiss();

        if (result.equalsIgnoreCase(Constants.FAIL)) {

            snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                            HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener1, Constants.BASE_URL + Constants.PAID_ADS_UPDATE_APP, para, 2, "app");
//                            httpAsync.execute();
                            HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, "http://maps.googleapis.com/maps/api/geocode/json?", para, 1, "map");
                            httpAsync1.execute();

                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.WHITE);

            // Changing action button text color
            sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.black));
            textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
//            textView.setTypeface(face);

            snackbar.show();


        } else {

            if (tag.equalsIgnoreCase("map")) {


                String currentLocation = "";

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject != null) {
                        JSONArray Results = jsonObject.getJSONArray("results");

//                        if (mpref.getBoolean("login", false)) {
//                            try {
//                                if (mpref.getString("usertype", null).equalsIgnoreCase("buyer")) {
//                                    Intent myint = new Intent(getApplicationContext(), LoginActivity.class);
//                                    myint.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(myint);
//                                    finish();
//                                } else if (mpref.getString("usertype", null).equalsIgnoreCase("buysell")) {
//
//                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    finish();
//
//                                }
//                            } catch (Exception e) {
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
//                            }
//
//
//                        } else {
//
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//
//                        }


                        JSONObject zero = Results.getJSONObject(0);
                        JSONArray address_components = zero
                                .getJSONArray("address_components");
                        for (int i = 0; i < address_components.length(); i++) {

                            JSONObject zero2 = address_components.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            short_name = zero2.getString("short_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);

                            if (Type.equalsIgnoreCase("street_number")) {
                                Address1 = long_name + " ";
                            } else if (Type.equalsIgnoreCase("route")) {
                                Address1 = Address1 + long_name;
                            } else if (Type.equalsIgnoreCase("sublocality")) {
                                Address2 = long_name;
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                            } else if (Type
                                    .equalsIgnoreCase("administrative_area_level_2")) {
//                                County = long_name;
                            } else if (Type
                                    .equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = short_name;
                                Log.e("short_name_lat",short_name);
                                mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                                SharedPreferences.Editor ed = mpref.edit();
                                ed.putString("shortname_country", short_name);
                                ed.putString("current_location", City);
                                ed.commit();

                                if(str_user_id.equalsIgnoreCase("")){
                                    parameters_guest = new HashMap<>();
                                    parameters_guest.put("DeviceId", imei);
                                    Log.e("products_guest", parameters_guest.toString());
                                    HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, Constants.BASE_URL+"Guest", parameters_guest, 2, "guest");
                                    httpAsync1.execute();
                                }
                                else {
                                    parameters_location = new HashMap<>();
                                    parameters_location.put("UserId",mpref.getString("user_id",""));
                                    parameters_location.put("Longitude",mpref.getString("user_lang",""));
                                    parameters_location.put("Latitude",mpref.getString("user_lat",""));
                                    parameters_location.put("CountryCode",short_name);
                                    Log.e("parameters_location",parameters_location.toString());
//                                    progressDialog.setMessage("Loading...");
//                                    progressDialog.setCancelable(false);
//                                    progressDialog.show();
                                    new HttpAsync(getApplicationContext(), listener1,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();

                                }
                            }
                        }
                        currentLocation = Address1 + "," + Address2 + "," + City + ","
                                + State + "," + Country;
                        Log.e("locat", currentLocation);

//                        nextActivity();



//                        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
//                        SharedPreferences.Editor ed = mpref.edit();
//                        ed.putString("user_city", City);
//                        ed.putString("user_state", State);
//                        ed.putString("user_country", Country);
//                        ed.putString("user_lat", str_latitude);
//                        ed.putString("user_lang", str_longitude);
//                        ed.commit();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (tag.equalsIgnoreCase("city_latitude")) {
                try {
                    JSONObject job1 = new JSONObject(result);
                    if (job1 != null) {
                        JSONObject job2 = job1.getJSONObject("result");
                        JSONArray jsonArray = job2.getJSONArray("address_components");
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    JSONArray jsonArray1 = jsonObject.getJSONArray("types");
//                    if (jsonArray1.length() > 0) {
//                        for (int j = 0; j < jsonArray1.length(); j++) {
//                            String str = jsonArray1.getString(j);
//                            if (str.equalsIgnoreCase("country")) {
//                                curr = jsonObject.getString("short_name");
//                                Log.e("curr",curr);
////                                try {
////                                    ar_dp_text = new ArrayList<String>();
////                                    ar_dp_text.add(curr);
////                                    str_arr_dp_text=new String[ar_dp_text.size()];
////                                    str_arr_dp_text=ar_dp_text.toArray(str_arr_dp_text);
////                                }catch (Exception e){
////
////                                }
//                            }
//                        }
//                    }

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject zero2 = jsonArray.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            short_name = zero2.getString("short_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);

                            if (Type.equalsIgnoreCase("country")) {
                                Country = short_name;
                            Log.e("shortname",short_name);
                                mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                                SharedPreferences.Editor ed = mpref.edit();
                                ed.putString("shortname_country", short_name);
                                ed.commit();
                            }
                            else if (Type.equalsIgnoreCase("locality")) {
//                                Country = short_name;
                                Log.e("locality",zero2.getString("long_name"));
                                mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                                SharedPreferences.Editor ed = mpref.edit();
                                ed.putString("current_location", zero2.getString("long_name"));
                                ed.commit();
                            }

                        }


//                        para_curr = new HashMap<>();
//                        para_curr.put("cn_code",Country);
//                        Log.e("para_country",Country);
//                        HttpAsync httpAsync = new HttpAsync(getApplicationContext(),asyncTaskListener,curr_url,para_curr,1,"curr");
//                        httpAsync.execute();
//                    if(jsonArray.length()>0){
//                        JSONObject jsonObject = job2.getJSONObject(3);
//                        String add = jsonObject.getString("formatted_address");
//                        Log.e("add",add);
                        JSONObject jsonObject1 = job2.getJSONObject("geometry");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                        try {
                            city_latitude = Double.valueOf(jsonObject2.getString("lat"));
                            city_longitude = Double.valueOf(jsonObject2.getString("lng"));

                            layoutLocationSearch.setAnimation(slideDown);
                            layoutLocationSearch.startAnimation(slideDown);

                            mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor ed = mpref.edit();
                            ed.putString("user_lat", city_latitude.toString());
                            ed.putString("user_lang", city_longitude.toString());
                            ed.putString("imei", imei);
                            ed.commit();
//                            nextActivity();

                            if(str_user_id.equalsIgnoreCase("")){
                                parameters_guest = new HashMap<>();
                                parameters_guest.put("DeviceId", imei);
                                Log.e("products_guest", parameters_guest.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, Constants.BASE_URL+"Guest", parameters_guest, 2, "guest");
                                httpAsync1.execute();
                            }
                            else {
                                parameters_location = new HashMap<>();
                                parameters_location.put("UserId",mpref.getString("user_id",""));
                                parameters_location.put("Longitude",mpref.getString("user_lang",""));
                                parameters_location.put("Latitude",mpref.getString("user_lat",""));
                                parameters_location.put("CountryCode",short_name);
                                Log.e("parameters_location",parameters_location.toString());
//                                progressDialog.setMessage("Loading...");
//                                progressDialog.setCancelable(false);
//                                progressDialog.show();
                                new HttpAsync(getApplicationContext(), listener1,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();

                            }

                        }catch (Exception e){
                            city_latitude = 12.8989;
                            city_longitude = 77.7878;

                            layoutLocationSearch.setAnimation(slideDown);
                            layoutLocationSearch.startAnimation(slideDown);

                            mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor ed = mpref.edit();
                            ed.putString("user_lat", "12.8989");
                            ed.putString("user_lang", "77.7878");
                            ed.putString("imei", imei);
                            ed.commit();
//                            nextActivity();

                            if(str_user_id.equalsIgnoreCase("")){
                                parameters_guest = new HashMap<>();
                                parameters_guest.put("DeviceId", imei);
                                Log.e("products_guest", parameters_guest.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, Constants.BASE_URL+"Guest", parameters_guest, 2, "guest");
                                httpAsync1.execute();
                            }
                            else {
                                parameters_location = new HashMap<>();
                                parameters_location.put("UserId",mpref.getString("user_id",""));
                                parameters_location.put("Longitude",mpref.getString("user_lang",""));
                                parameters_location.put("Latitude",mpref.getString("user_lat",""));
                                parameters_location.put("CountryCode",short_name);
                                Log.e("parameters_location",parameters_location.toString());
//                                progressDialog.setMessage("Loading...");
//                                progressDialog.setCancelable(false);
//                                progressDialog.show();
                                new HttpAsync(getApplicationContext(), listener1,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();

                            }


                        }


//                        Log.e("latlng", String.valueOf(latitude)+","+String.valueOf(longitude));
//                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("location")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
//                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Log.e("loc_success",jsonObject.getString("errMsg"));
                            Log.e("currency_code",jsonObject.getString("Currency"));
                            Log.e("currency_symbol",jsonObject.getString("CurrencySymbole"));
                            Log.e("country_code",jsonObject.getString("CountryCode"));

                            mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor ed = mpref.edit();
                            ed.putString("currency_code", jsonObject.getString("Currency"));
                            ed.putString("currency_symbol", jsonObject.getString("CurrencySymbole"));
                            ed.putString("country_code", jsonObject.getString("CountryCode"));
                            ed.commit();

//                            nextActivity();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();


                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("guest")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
//                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
//                            Log.e("UserId_guest",jsonObject.getString("UserId"));

                            mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor ed = mpref.edit();
                            ed.putString("user_id", jsonObject.getString("UserId"));
                            ed.putString("guest_status", "0");
                            ed.commit();

                            parameters_location = new HashMap<>();
                            parameters_location.put("UserId",mpref.getString("user_id",""));
                            parameters_location.put("Longitude",mpref.getString("user_lang",""));
                            parameters_location.put("Latitude",mpref.getString("user_lat",""));
                            parameters_location.put("CountryCode",short_name);
                            Log.e("parameters_location",parameters_location.toString());
//                            progressDialog.setMessage("Loading...");
//                            progressDialog.setCancelable(false);
//                            progressDialog.show();
                            new HttpAsync(getApplicationContext(), listener1,Constants.BASE_URL+"UpdateLocation" , parameters_location, 2, "location").execute();


//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();

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

        startService(new Intent(this, GPS_Service.class));


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        try {

            str_latitude = String.valueOf(location.getLatitude());
            str_longitude = String.valueOf(location.getLongitude());
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, Splash.this);


            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }

//                tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                imei = tel.getDeviceId().toString();
//                                    Log.e("imei", imei);
                    Log.e("latlng_post1st", str_latitude + "," + str_longitude);

                    mpref = getSharedPreferences("user_details", MODE_PRIVATE);
                    SharedPreferences.Editor ed = mpref.edit();
                    ed.putString("user_lat", str_latitude);
                    ed.putString("user_lang", str_longitude);
                    ed.putString("imei", imei);
                    ed.commit();

                    para = new HashMap<>();
                    para.put("latlng", str_latitude + "," + str_longitude);
                    para.put("sensor", "true");
                    Log.e("para_loc_name", para.toString());
                    HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, "http://maps.googleapis.com/maps/api/geocode/json?", para, 1, "map");
                    httpAsync1.execute();


//                    nextActivity();

                }
            }, 1000);

        }catch (Exception e){

        }


    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class parser extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub


            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url.toString());
            if (json != null) {
                try {
                    JSONArray contacts = json.getJSONArray("predictions");
                    sugestion = new ArrayList<String>();
                    locationBean = new ArrayList<>();
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String description = c.getString("description");
                        String place_id=c.getString("place_id");
                        LocationBean bn = new LocationBean();
                        JSONArray ja = c.getJSONArray("terms");
                        if (ja.length() >= 3) {
                            bn.setDescription(description);
                            bn.setPlace_id(place_id);
                            bn.setPlace(ja.getJSONObject((0)).getString("value"));
                            bn.setCity(ja.getJSONObject((1)).optString("value"));
                            bn.setState(ja.getJSONObject((2)).optString("value"));
                            bn.setCountry(ja.getJSONObject((ja.length() - 1)).optString("value"));
                            locationBean.add(bn);
                        }
                        sugestion.add(description);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            //			pb.setVisibility(View.GONE);
            try {
                LocationAdapter adapter = new LocationAdapter(Splash.this, locationBean);
                listLocation.setAdapter(adapter);
            }catch (Exception e){

            }

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            pb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if(layoutLocationSearch.getVisibility() == View.VISIBLE){
            layoutLocationSearch.setAnimation(slideDown);
            layoutLocationSearch.startAnimation(slideDown);
//            Splashtask();
        }
        else {
            super.onBackPressed();
        }
    }

    public void nextActivity(){

//        if (str_user_id.equalsIgnoreCase("")) {
////            Log.e("entered", "entered");
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        } else {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        }

        if(str_user_id.equalsIgnoreCase("")){
            parameters_guest = new HashMap<>();
            parameters_guest.put("DeviceId", imei);
            Log.e("products_guest", parameters_guest.toString());
            HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener1, Constants.BASE_URL+"Guest", parameters_guest, 2, "guest");
            httpAsync1.execute();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }
}
