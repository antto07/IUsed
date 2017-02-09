package com.iused.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iused.R;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;


/**
 * Created by Antto on 05-10-2016.
 */
public class WishlistFragment extends Fragment implements AsyncTaskListener{

    View view;
    private static final String TAG = "MainActivity";
    private EditText mTagsEditText;
    private Button btn_wishlist_submit=null;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;
    private ProgressDialog progressDialog= null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        mpref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (mpref != null)

        progressDialog= new ProgressDialog(getActivity());
        listener=WishlistFragment.this;

        mTagsEditText = (EditText) view.findViewById(R.id.tagsEditText);
        mTagsEditText.setHint("Enter the tags and press enter");
//        mTagsEditText.setTagsListener(new TagsEditText.TagsEditListener() {
//            @Override
//            public void onTagsChanged(Collection<String> tags) {
//
//            }
//
//            @Override
//            public void onEditingFinished() {
//
//            }
//        });
//        mTagsEditText.setTagsWithSpacesEnabled(true);
//        mTagsEditText.setTag(mTagsEditText.getText().toString());

        btn_wishlist_submit= (Button) view.findViewById(R.id.btn_wishlist_submit);
        btn_wishlist_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mTagsEditText.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Please add a tag",Toast.LENGTH_SHORT).show();
                }
                else {
                    para = new HashMap<>();
                    para.put("UserId", mpref.getString("user_id", ""));
                    para.put("WishList", mTagsEditText.getText().toString());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Log.e("para_wishlist", para.toString());
                    HttpAsync httpAsync1 = new HttpAsync(getActivity(), listener, Constants.BASE_URL+"UpdateWishList", para, 2, "wishlist");
                    httpAsync1.execute();
                }
            }
        });

        return view;
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        progressDialog.dismiss();

        if(result.equalsIgnoreCase("fail")){
            try {
                Toast.makeText(getActivity(),"Check internet connection",Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }
        else {
            if(tag.equalsIgnoreCase("wishlist")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            Toast.makeText(getActivity(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (JSONException e){

                }
            }
        }

    }
}
