package com.iused.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iused.R;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lenovo on 28/10/2016.
 */
public class ProfileActivity extends AppCompatActivity implements AsyncTaskListener{

    private CircleImageView imageView_profile_pic=null;
    private EditText edt_name= null;
    private EditText edt_email= null;
    private EditText edt_mobile_number= null;
    private EditText edt_name_editable=null;
    public SharedPreferences mpref = null;
    private Button btn_edit_profile=null;
    private Button btn_save_profile=null;
    private AsyncTaskListener listener = null;
    private HashMap<String, String> para = null;
    private ProgressDialog progressDialog= null;
    private LinearLayout linear_editable=null;
    private LinearLayout linear_name=null;
    private Typeface face,face1=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        progressDialog= new ProgressDialog(ProfileActivity.this);
        listener = ProfileActivity.this;

        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (mpref != null)

        edt_name= (EditText) findViewById(R.id.edt_name);
        edt_email= (EditText) findViewById(R.id.edt_email);
        edt_mobile_number= (EditText) findViewById(R.id.edt_mobile);
        edt_name_editable= (EditText) findViewById(R.id.edt_name_editable);
        imageView_profile_pic= (CircleImageView) findViewById(R.id.img_profile_pic);
        btn_edit_profile= (Button) findViewById(R.id.btn_edit_profile);
        btn_save_profile= (Button) findViewById(R.id.btn_save_profile);
        linear_editable= (LinearLayout) findViewById(R.id.linear_name_editable);
        linear_name= (LinearLayout) findViewById(R.id.linear_name);

        face= Typeface.createFromAsset(getAssets(), "fonts/bariolreg.otf");
        edt_name.setTypeface(face);
        edt_email.setTypeface(face);
        edt_mobile_number.setTypeface(face);
        edt_name_editable.setTypeface(face);

        try {
            edt_name.setText(mpref.getString("username",""));
            edt_email.setText(mpref.getString("email",""));
            edt_mobile_number.setText(mpref.getString("mobile",""));
            edt_name_editable.setText(mpref.getString("username",""));
        }catch (Exception e){
            edt_name.setText("");
            edt_email.setText("");
            edt_mobile_number.setText("");
            edt_name_editable.setText("");
        }

//        Log.e("photo",mpref.getString("photo","")+mpref.getString("username",""));

        try {
            Picasso.with(getApplicationContext())
                    .load(mpref.getString("photo",""))
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView_profile_pic,new PicassoCallback(mpref.getString("photo","")));
        }catch (Exception e){
            Picasso.with(getApplicationContext())
                    .load("http://52.41.70.254/pics/user.jpg")
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView_profile_pic,new PicassoCallback("http://52.41.70.254/pics/user.jpg"));
        }

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_edit_profile.setVisibility(View.GONE);
                btn_save_profile.setVisibility(View.VISIBLE);

                linear_name.setVisibility(View.GONE);
                linear_editable.setVisibility(View.VISIBLE);
                edt_name_editable.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_name_editable, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_edit_profile.setVisibility(View.VISIBLE);
                btn_save_profile.setVisibility(View.GONE);

                linear_name.setVisibility(View.VISIBLE);
                linear_editable.setVisibility(View.GONE);

                para = new HashMap<>();
                para.put("UserId",mpref.getString("user_id",""));
                para.put("Name",edt_name_editable.getText().toString());
                para.put("Photo",mpref.getString("photo",""));
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.e("products_details", para.toString());
                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"updatecustomerprofile", para, 2, "profile");
                httpAsync1.execute();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
            if(tag.equalsIgnoreCase("profile")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
//                    str_json_message= jsonObject.getString("errMsg");
                        if(jsonObject.getString("errMsg").equalsIgnoreCase("Success")){


                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor edit = mpref.edit();
                            edit.putString("username",edt_name_editable.getText().toString());
                            edit.commit();

                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
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

        }

    }
}
