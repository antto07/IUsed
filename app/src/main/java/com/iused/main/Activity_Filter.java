package com.iused.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.iused.R;

public class Activity_Filter extends AppCompatActivity {
    private Context mContext;
    private LinearLayout mLinearLayout;
    private SeekBar mSeekBar;
    private TextView mTextView;
    Button reset,apply;
    CheckBox newest,close,low,high,last24,last7,last30;
    EditText to,from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter");
//buttons
        reset=(Button)findViewById(R.id.reset);
        apply=(Button)findViewById(R.id.apply);
        //checkbox
        newest=(CheckBox)findViewById(R.id.newFirst);
        close=(CheckBox)findViewById(R.id.closeFirst);
        low=(CheckBox)findViewById(R.id.lowtohigh);
        high=(CheckBox)findViewById(R.id.hightolow);
        last24=(CheckBox)findViewById(R.id.last24hours);
        last7=(CheckBox)findViewById(R.id.last7days);
        last30=(CheckBox)findViewById(R.id.last30days);
        from=(EditText)findViewById(R.id.from);
        to=(EditText)findViewById(R.id.to);



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newest.isChecked()) {
                    newest.setChecked(false);
                }
                if (close.isChecked()) {
                    close.setChecked(false);
                }
                if (low.isChecked()) {
                    low.setChecked(false);
                }
                if (high.isChecked()) {
                    high.setChecked(false);
                }
                if (last24.isChecked()) {
                    last24.setChecked(false);
                }
                if (last7.isChecked()) {
                    last7.setChecked(false);
                }
                if (last30.isChecked()) {
                    last30.setChecked(false);
                }
                if(!from.getText().equals(null))
                {
                    from.getText().clear();
                }

                if(!to.getText().equals(null))
                {
                    to.getText().clear();
                }
                mSeekBar.setProgress(500);
            }
        });


        mLinearLayout = (LinearLayout) findViewById(R.id.rl);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mTextView = (TextView) findViewById(R.id.TextSeekbarDistance);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Display the current progress of SeekBar
                mTextView.setText(""+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void neww(View view)
    {
        if (newest.isChecked())
            newest.setChecked(false);
            else
            newest.setChecked(true);
            close.setChecked(false);
            low.setChecked(false);
            high.setChecked(false);

    }
    public void close(View view)
    {
        if (close.isChecked())
            close.setChecked(false);
        else
            close.setChecked(true);
            newest.setChecked(false);
            low.setChecked(false);
            high.setChecked(false);

    }
    public void low(View view)
    {
        if (low.isChecked())
            low.setChecked(false);
        else
            low.setChecked(true);
            newest.setChecked(false);
            close.setChecked(false);
            high.setChecked(false);

    }
    public void high(View view)
    {
        if (high.isChecked())
            high.setChecked(false);
        else
            high.setChecked(true);
            newest.setChecked(false);
            low.setChecked(false);
            close.setChecked(false);

    }
    public void last24(View view)
    {
        if (last24.isChecked())
            last24.setChecked(false);
        else
            last24.setChecked(true);
            last7.setChecked(false);
            last30.setChecked(false);

    }
    public void last7(View view)
    {
        if (last7.isChecked())
            last7.setChecked(false);
        else
            last7.setChecked(true);
            last24.setChecked(false);
            last30.setChecked(false);

    }
    public void last30(View view)
    {
        if (last30.isChecked())
            last30.setChecked(false);
        else
            last30.setChecked(true);
            last7.setChecked(false);
            last24.setChecked(false);

    }

    public void apply(View view){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(to.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(from.getWindowToken(), 0);

        MainActivity.str_lower_distance="0";
        MainActivity.str_upper_distance=mTextView.getText().toString();

        if(newest.isChecked()){
            MainActivity.str_sort_by="4";
        }
        else if(close.isChecked()){
            MainActivity.str_sort_by="3";
        }
        else if(high.isChecked()){
           MainActivity.str_sort_by="2";
        }
        else if(low.isChecked()){
            MainActivity.str_sort_by="1";
        }
        else {
            MainActivity.str_sort_by="";
        }

        if(last7.isChecked()){
            MainActivity.str_posted_in="7";
        }
        else if(last24.isChecked()){
            MainActivity.str_posted_in="1";
        }
        else if(last30.isChecked()){
            MainActivity.str_posted_in="30";
        }
        else {
            MainActivity.str_posted_in="";
        }


        if(from.getText().toString().equalsIgnoreCase("")&&to.getText().toString().equalsIgnoreCase("")){

           MainActivity.str_lower_range_price="0";
            MainActivity.str_upper_range_price="1000000";
        }
        else if(from.getText().toString().equalsIgnoreCase("")&&!to.getText().toString().equalsIgnoreCase("")){

            MainActivity.str_lower_range_price="0";
            MainActivity.str_upper_range_price=to.getText().toString();
        }
        else if(!to.getText().toString().equalsIgnoreCase("")&&from.getText().toString().equalsIgnoreCase("")){

            MainActivity.str_lower_range_price=from.getText().toString();
            MainActivity.str_upper_range_price="1000000";
        }
        else if(!to.getText().toString().equalsIgnoreCase("")&&!from.getText().toString().equalsIgnoreCase("")){
            MainActivity.str_lower_range_price=from.getText().toString();
            MainActivity.str_upper_range_price=to.getText().toString();
        }

        try {
            if(Long.parseLong(from.getText().toString())>Long.parseLong(to.getText().toString())){
                Toast.makeText(getApplicationContext(),"Minimum price should not be greater than Maximum price",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }




    }
}
