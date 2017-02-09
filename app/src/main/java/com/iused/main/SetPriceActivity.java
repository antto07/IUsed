package com.iused.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.fragments.SellFragment;
import com.iused.introduction.LoginActivity;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Antto on 04-10-2016.
 */
public class SetPriceActivity extends AppCompatActivity implements AsyncTaskListener{

    private CheckBox chk_negotiable= null;
    private CheckBox chk_non_negotiable= null;
    private EditText edt_price_negotiable= null;
    private EditText edt_price_non_negotiable= null;
    private EditText edt_percentage= null;
    private EditText edt_duration= null;
    private TextView txt_currency_code_nego=null;
    private TextView txt_currency_code_non_nego=null;
    private Button btn_sell=null;
    Intent intent=null;

    public static HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;
    private ProgressDialog progressDialog= null;

    String currentDateTimeString = "";
    String locale ="";
    String timezone="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_price);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set the Price");

        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (mpref != null)

        chk_negotiable= (CheckBox) findViewById(R.id.chk_negotiable);
        chk_non_negotiable= (CheckBox) findViewById(R.id.chk_non_negotiable);
        edt_price_negotiable= (EditText) findViewById(R.id.edt_price_negotiable);
        edt_price_non_negotiable= (EditText) findViewById(R.id.edt_price_non_negotiable);
        edt_percentage= (EditText) findViewById(R.id.edt_percentage);
        edt_duration= (EditText) findViewById(R.id.edt_duration);
        btn_sell= (Button) findViewById(R.id.btn_sell);
        txt_currency_code_nego= (TextView) findViewById(R.id.curr_code_neg);
        txt_currency_code_non_nego= (TextView) findViewById(R.id.curr_code_non_neg);
        progressDialog= new ProgressDialog(SetPriceActivity.this);
        listener = SetPriceActivity.this;
        intent=getIntent();

        txt_currency_code_nego.setText(mpref.getString("currency_symbol",""));
        txt_currency_code_non_nego.setText(mpref.getString("currency_symbol",""));

        edt_price_negotiable.setEnabled(true);
        edt_price_non_negotiable.setEnabled(false);

        chk_negotiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //Case 1
                    chk_non_negotiable.setChecked(false);
                    edt_price_non_negotiable.setText("");
                    edt_percentage.setText("");
                    edt_duration.setText("");
                    edt_price_negotiable.setEnabled(true);
                    edt_price_non_negotiable.setEnabled(false);
                }
                else{
                    chk_non_negotiable.setChecked(false);
                    edt_price_non_negotiable.setText("");
                    edt_percentage.setText("");
                    edt_duration.setText("");
                    edt_price_negotiable.setEnabled(true);
                    edt_price_non_negotiable.setEnabled(false);
                }

            }
        });

        chk_non_negotiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //Case 1
                    chk_negotiable.setChecked(false);
                    edt_price_negotiable.setText("");
                    edt_price_negotiable.setEnabled(false);
                    edt_price_non_negotiable.setEnabled(true);
                }
                else{

                    chk_negotiable.setChecked(false);
                    edt_price_negotiable.setText("");
                    edt_price_negotiable.setEnabled(false);
                    edt_price_non_negotiable.setEnabled(true);
                }
            }
        });

//        if(chk_negotiable.isChecked()==true){
//            edt_price_negotiable.setKeyListener((KeyListener) edt_price_negotiable.getTag());
//
//            chk_non_negotiable.setChecked(false);
//
//            edt_price_non_negotiable.setText("");
//            edt_price_non_negotiable.setTag(edt_price_non_negotiable.getKeyListener());
//            edt_price_non_negotiable.setKeyListener(null);
//            edt_percentage.setText("");
//            edt_percentage.setTag(edt_percentage.getKeyListener());
//            edt_percentage.setKeyListener(null);
//            edt_duration.setText("");
//            edt_duration.setTag(edt_duration.getKeyListener());
//            edt_duration.setKeyListener(null);
//        }
//        else if(chk_non_negotiable.isChecked()==true){
//
//            chk_negotiable.setChecked(false);
//
//            edt_price_negotiable.setText("");
//            edt_price_negotiable.setTag(edt_price_negotiable.getKeyListener());
//            edt_price_negotiable.setKeyListener(null);
//
//            edt_price_non_negotiable.setKeyListener((KeyListener) edt_price_non_negotiable.getTag());
//            edt_percentage.setKeyListener((KeyListener) edt_percentage.getTag());
//            edt_duration.setKeyListener((KeyListener) edt_duration.getTag());
//        }

        locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
//        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        timezone= TimeZone.getDefault().getDisplayName();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentDateTimeString = formatter.format(today);

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_price_negotiable.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edt_price_non_negotiable.getWindowToken(), 0);

                if(chk_negotiable.isChecked()==true){
                    if(edt_price_negotiable.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Please enter price",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        para = new HashMap<>();
                        para.put("UserId", mpref.getString("user_id",""));
                        para.put("Name", intent.getStringExtra("item_name"));
                        para.put("Description",intent.getStringExtra("decription"));
                        para.put("Price",edt_price_negotiable.getText().toString());
                        para.put("Qty","1");
                        para.put("UsedFor",intent.getStringExtra("used_for"));
                        para.put("OfferPer","");
                        para.put("OfferMins","");
                        para.put("VideoLinks",intent.getStringExtra("video_url"));
                        para.put("ExchangeOffer","");
                        para.put("Created_dt",currentDateTimeString);
                        para.put("Type","2");
                        para.put("Condition",intent.getStringExtra("condition"));
                        para.put("ImageLinks",Sell_Products_Activity.image_uris.toString().substring(1, Sell_Products_Activity.image_uris.toString().length()-1));
                        para.put("CategoryId",intent.getStringExtra("category_id"));
                        para.put("Currency",mpref.getString("currency_symbol",""));

                        if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("offer_negotiable","sell_product");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else {

                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            Log.e("sell_product", para.toString());
                            HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"sellProduct", para, 2, "sell_product");
                            httpAsync1.execute();
                        }

                    }
                }
                else if(chk_non_negotiable.isChecked()==true){
                    if(edt_price_non_negotiable.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Please enter price",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(mpref.getString("user_id","").equalsIgnoreCase("")){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else {
                            para = new HashMap<>();
                            para.put("UserId", mpref.getString("user_id",""));
                            para.put("Name", intent.getStringExtra("item_name"));
                            para.put("Description",intent.getStringExtra("decription"));
                            para.put("Price",edt_price_non_negotiable.getText().toString());
                            para.put("Qty","1");
                            para.put("UsedFor",intent.getStringExtra("used_for"));
                            para.put("OfferPer","");
                            para.put("OfferMins","");
                            para.put("VideoLinks",intent.getStringExtra("video_url"));
                            para.put("ExchangeOffer",edt_percentage.getText().toString());
                            para.put("Created_dt",currentDateTimeString);
                            para.put("Type","1");
                            para.put("Condition",intent.getStringExtra("condition"));
                            try {
                                para.put("ImageLinks",Sell_Products_Activity.image_uris.toString().substring(1, Sell_Products_Activity.image_uris.toString().length()-1));
                            }catch (Exception e){
                                para.put("ImageLinks",Sell_Products_Activity.image_uris.toString());
                            }
                            para.put("CategoryId",intent.getStringExtra("category_id"));
                            para.put("Currency",mpref.getString("currency_symbol",""));

                            if(mpref.getString("guest_status","").equalsIgnoreCase("0")){
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.putExtra("offer_negotiable","sell_product");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else {
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                Log.e("sell_product", para.toString());
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"sellProduct", para, 2, "sell_product");
                                httpAsync1.execute();
                            }

                        }

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please select a type",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        progressDialog.dismiss();
        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }
        else {
            if(tag.equalsIgnoreCase("sell_product")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){

                            AlertDialog alertDialog=new AlertDialog.Builder(SetPriceActivity.this).setMessage(jsonObject.getString("errMsg"))
                                    .setCancelable(false)
                                    .setTitle("Success")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();

//                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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

    }
}
