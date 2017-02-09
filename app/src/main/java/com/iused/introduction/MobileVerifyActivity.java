package com.iused.introduction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.fragments.Donate_Product_Activity;
import com.iused.main.MainActivity;
import com.iused.main.ProductDetailsActivity_Donate;
import com.iused.main.ProductDetailsActivity_Negotiable;
import com.iused.main.ProductDetailsActivity_Non_Negotiable;
import com.iused.main.SetPriceActivity;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Antto on 06-10-2016.
 */
public class MobileVerifyActivity extends AppCompatActivity implements AsyncTaskListener{

    private EditText edt_verify_code=null;
    private Button btn_submit=null;
    private HashMap<String, String> para = null;
    private HashMap<String, String> para_negotiable = new HashMap<>();
    private HashMap<String, String> para_sell_product = new HashMap<>();
    private HashMap<String, String> para_donate_product = new HashMap<>();
    private HashMap<String, String> para_non_negotiable = new HashMap<>();
    private HashMap<String, String> para_donate_request= new HashMap<>();
    private HashMap<String, String> para_profile = null;
    private AsyncTaskListener listener = null;
    private SharedPreferences pref = null;
    private String str_json_message= null;
    Intent intent_data= null;
    private ProgressDialog progressDialog= null;
    private Button btn_resend_code=null;
    private Button btn_resend_code_gray=null;
    private static MobileVerifyActivity ins;
    private TextView txt_mobile_verify_text=null;
    private Intent intent_direct=null;
    private TextView txt_timer_mobile_verify=null;
    private static CountDownTimer countDownTimer;
    private Typeface face,face1=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mobile_verify);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mobile Verify");

        ins=this;

        face= Typeface.createFromAsset(getAssets(), "fonts/bariolreg.otf");

        progressDialog= new ProgressDialog(MobileVerifyActivity.this);
        edt_verify_code= (EditText) findViewById(R.id.edt_code);
        btn_submit= (Button) findViewById(R.id.btn_submit);
        btn_resend_code= (Button) findViewById(R.id.btn_resend_code);
        btn_resend_code_gray= (Button) findViewById(R.id.btn_resend_code_gray);
        txt_mobile_verify_text= (TextView) findViewById(R.id.txt_verify_message);
        txt_timer_mobile_verify= (TextView) findViewById(R.id.txt_timer_mobile_verify);

        edt_verify_code.setTypeface(face);

        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txt_timer_mobile_verify.setText(hms);//set text
                btn_resend_code_gray.setVisibility(View.VISIBLE);
                btn_resend_code.setVisibility(View.GONE);
            }

            public void onFinish() {

//                        Intent intent=new Intent(context, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(intent);

                txt_timer_mobile_verify.setText("00:00");
                btn_resend_code_gray.setVisibility(View.GONE);
                btn_resend_code.setVisibility(View.VISIBLE);//On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
//                startTimer.setText(getString(R.string.start_timer));//Change button text
            }
        }.start();

        intent_data=getIntent();
        pref = getSharedPreferences("user_details", MODE_PRIVATE);

        listener = MobileVerifyActivity.this;

        intent_direct=getIntent();

        txt_mobile_verify_text.setText("Enter verification code that sent to the mobile number ending "+intent_data.getStringExtra("mobile").substring(Math.max(intent_data.getStringExtra("mobile").length() - 4, 0)));
//        edt_verify_code.setText("jhghghhkjjk");
//        Log.e("bhjgh",intent_data.getStringExtra("photo"));

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_verify_code.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter verification code that sent to the mobile number ending "+intent_data.getStringExtra("mobile").substring(Math.max(intent_data.getStringExtra("mobile").length() - 4, 0)),Toast.LENGTH_SHORT).show();
                }
                else {
                    para = new HashMap<>();
                    para.put("code", edt_verify_code.getText().toString());
                    para.put("phone", intent_data.getStringExtra("mobile_code")+intent_data.getStringExtra("mobile"));
                    para.put("UserId",intent_data.getStringExtra("user_id"));
                    Log.e("para_verify", para.toString());
                    progressDialog.setMessage("Verifying...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"verificationcode", para, 2, "verify");
                    httpAsync1.execute();
                }
            }
        });

        btn_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                para = new HashMap<>();
                para.put("Email",intent_data.getStringExtra("email"));
                para.put("Phone", intent_data.getStringExtra("mobile_code")+intent_data.getStringExtra("mobile"));
                para.put("Name",intent_data.getStringExtra("user_name"));
                para.put("Longitude",pref.getString("user_lang",""));
                para.put("Latitude",pref.getString("user_lat",""));
                para.put("DeviceType","2");
                para.put("DeviceId",pref.getString("imei",""));
                para.put("DeviceToken",intent_data.getStringExtra("regid"));
                para.put("Photo",intent_data.getStringExtra("photo"));
                para.put("Timezon",intent_data.getStringExtra("timezone"));
                para.put("Created_dt",intent_data.getStringExtra("created_at"));
                para.put("FbId",intent_data.getStringExtra("fbid"));
                para.put("SignupBy",intent_data.getStringExtra("signup"));
                progressDialog.setMessage("Re-sending Code...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.e("para_sign_up_resend", para.toString());
                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"customerSignup", para, 2, "sign_up");
                httpAsync1.execute();
            }
        });

        edt_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_verify_code.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter verification code that sent to the mobile number ending "+intent_data.getStringExtra("mobile").substring(Math.max(intent_data.getStringExtra("mobile").length() - 4, 0)),Toast.LENGTH_SHORT).show();
                }
                else {
                    if(edt_verify_code.getText().toString().length()==5){
                        para = new HashMap<>();
                        para.put("code", edt_verify_code.getText().toString());
                        para.put("phone", intent_data.getStringExtra("mobile_code")+intent_data.getStringExtra("mobile"));
                        para.put("UserId",intent_data.getStringExtra("user_id"));
                        Log.e("para_verify", para.toString());
                        progressDialog.setMessage("Verifying...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"verificationcode", para, 2, "verify");
                        httpAsync1.execute();
                    }
                    else {

                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
//        countDownTimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public static MobileVerifyActivity  getInstace(){
        return ins;
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        progressDialog.dismiss();

        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }
        else{
            if(tag.equalsIgnoreCase("verify")){

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        str_json_message= jsonObject.getString("errMsg");
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
//                            countDownTimer.cancel();
                            para_profile = new HashMap<>();
                            para_profile.put("UserId",intent_data.getStringExtra("user_id"));
                            para_profile.put("Name",intent_data.getStringExtra("user_name"));
                            para_profile.put("Photo",intent_data.getStringExtra("photo"));
                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            Log.e("para_profile_update", para_profile.toString());
                            HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"updatecustomerprofile", para_profile, 2, "profile");
                            httpAsync1.execute();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else  if(tag.equalsIgnoreCase("profile")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
//                    str_json_message= jsonObject.getString("errMsg");
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){

                            pref= getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("user_id",intent_data.getStringExtra("user_id"));
                            edit.putString("mobile",intent_data.getStringExtra("mobile"));
                            edit.putString("email",intent_data.getStringExtra("email"));
                            edit.putString("photo",intent_data.getStringExtra("photo"));
                            edit.putString("username",intent_data.getStringExtra("user_name"));
                            edit.putString("guest_status", "1");
                            edit.commit();

                            Log.e("user_id_verify",intent_data.getStringExtra("user_id"));

                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();

                            if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("negotiable")){

                                para_negotiable= ProductDetailsActivity_Negotiable.para_buy_request;
                                ProductDetailsActivity_Negotiable.para_buy_request.put("UserId",intent_data.getStringExtra("user_id"));
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
//                                Log.e("products_details", para_negotiable.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"PurchaseRequests", para_negotiable, 2, "buy_request");
                                httpAsync1.execute();

                            }
                            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("sell_product")){

                                para_sell_product= SetPriceActivity.para;
                                SetPriceActivity.para.put("UserId",intent_data.getStringExtra("user_id"));
                                progressDialog.setMessage("Posting...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
//                                Log.e("sell_product", para.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"sellProduct", para_sell_product, 2, "sell_product");
                                httpAsync1.execute();
                            }
                            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_product")){

                                para_donate_product= Donate_Product_Activity.para;
                                Donate_Product_Activity.para.put("UserId",intent_data.getStringExtra("user_id"));
                                progressDialog.setMessage("Posting...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
//                                Log.e("sell_product", para.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"sellProduct", para_donate_product, 2, "donate_product");
                                httpAsync1.execute();
                            }
                            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("non-negotiable")){

                                para_non_negotiable= ProductDetailsActivity_Non_Negotiable.para_buy_request;
                                ProductDetailsActivity_Non_Negotiable.para_buy_request.put("UserId",intent_data.getStringExtra("user_id"));
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
//                                Log.e("products_details", para_buy_request.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"PurchaseRequests", para_non_negotiable, 2, "buy_request_non");
                                httpAsync1.execute();
                            }
                            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("none")){
                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_offer")){
                                para_donate_request= ProductDetailsActivity_Donate.para_buy_request;
                                ProductDetailsActivity_Donate.para_buy_request.put("UserId",intent_data.getStringExtra("user_id"));
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
//                                Log.e("products_details", para_buy_request.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"PurchaseRequests", para_donate_request, 2, "buy_request_donate");
                                httpAsync1.execute();
                            }

//                            Log.e("phhh",intent_data.getStringExtra("photo"));



                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("buy_request_non")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("buy_request_donate")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("buy_request")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();

                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("donate_product")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("sell_product")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){

                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("sign_up")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){

                        countDownTimer = new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                long millis = millisUntilFinished;
                                //Convert milliseconds into hour,minute and seconds
                                String hms = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                                txt_timer_mobile_verify.setText(hms);//set text
                                btn_resend_code_gray.setVisibility(View.VISIBLE);
                                btn_resend_code.setVisibility(View.GONE);
                            }

                            public void onFinish() {

//                        Intent intent=new Intent(context, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(intent);

                                txt_timer_mobile_verify.setText("00:00");
                                btn_resend_code_gray.setVisibility(View.GONE);
                                btn_resend_code.setVisibility(View.VISIBLE);//On finish change timer text
                                countDownTimer = null;//set CountDownTimer to null
//                startTimer.setText(getString(R.string.start_timer));//Change button text
                            }
                        }.start();

                        str_json_message= jsonObject.getString("errFlag");
                        if(str_json_message.equalsIgnoreCase("0")){

//                            SharedPreferences.Editor edit = pref.edit();
//                            edit.putString("user_id",jsonObject.getString("UserId"));
//                            edit.commit();

                            Toast.makeText(getApplicationContext(),"Enter verification code that sent to the mobile number ending "+intent_data.getStringExtra("mobile").substring(Math.max(intent_data.getStringExtra("mobile").length() - 4, 0)),Toast.LENGTH_SHORT).show();


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

//    public void validateOTP(Context context1, String message) throws Exception {
//
//        Log.e("msg",message);
//        try {
//            String str_otp = message.trim().substring(0, 5);
//            Log.e("msg_sub",str_otp);
//            edt_verify_code.setText(str_otp);
//            Toast.makeText(getApplicationContext(),str_otp,Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"code",Toast.LENGTH_SHORT).show();
//
//        }catch (Exception e){
//
//        }
//
//    }

    public void validateOTP(Context context1,final String t) {
        MobileVerifyActivity.this.runOnUiThread(new Runnable() {
            public void run() {
//                Log.e("msg",t);
        try {
            String str_otp = t.trim().substring(0, 5);
//            Log.e("msg_sub",str_otp);
            edt_verify_code.setText(str_otp);
//            Toast.makeText(getApplicationContext(),str_otp,Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"code",Toast.LENGTH_SHORT).show();

        }catch (Exception e){

        }
            }
        });
    }

}
