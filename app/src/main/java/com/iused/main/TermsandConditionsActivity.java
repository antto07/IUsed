package com.iused.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.iused.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;
import com.iused.utils.MyTagHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vamsi on 17-10-2016.
 */
public class TermsandConditionsActivity extends AppCompatActivity implements AsyncTaskListener {

    private AsyncTaskListener listener = null;
    private HashMap<String, String> para = null;
    private ProgressDialog progressDialog= null;
    private TextView txt_terms=null;
    private WebView webview_terms=null;
    private String str_terms="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_terms_and_conditions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Terms & Conditions");

        progressDialog= new ProgressDialog(TermsandConditionsActivity.this);
        listener = TermsandConditionsActivity.this;

        txt_terms= (TextView) findViewById(R.id.txt_terms);
        webview_terms= (WebView) findViewById(R.id.webView_terms);



        para = new HashMap<>();
//        para.put("Datetime","");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("products_details", para.toString());
        HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"Terms", para, 2, "terms");
        httpAsync1.execute();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static CharSequence trimTrailingWhitespace(CharSequence source) {

        if(source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i+1);
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
            if(tag.equalsIgnoreCase("terms")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
//                        if(jsonObject.getString("errFlag").equalsIgnoreCase("Success")){
                            JSONObject object=jsonObject.getJSONObject("data");
                            if(object!=null){
                                txt_terms.setText(Html.fromHtml(object.getString("Terms")));
//                                Log.e("terms",object.getString("Terms"));

//                                CharSequence trimmed = trimTrailingWhitespace(Html.fromHtml(object.getString("Terms"), null, new MyTagHandler()));
//                                txt_terms.setText(trimmed);
                                webview_terms.loadData(object.getString("Terms"),"text/html", "UTF-8");
                            }

//                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errFlag"),Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
