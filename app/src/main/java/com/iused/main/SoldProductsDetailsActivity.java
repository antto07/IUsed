package com.iused.main;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iused.R;
import com.iused.adapters.SoldProductsDetailsAdapter;
import com.iused.bean.SoldProductsBean;
import com.iused.dialog.DonatedByOthersContactDialog;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.Constants;
import com.iused.utils.HttpAsync;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ${ADMIN} on 10/11/2016.
 */
public class SoldProductsDetailsActivity extends AppCompatActivity implements AsyncTaskListener{

    Intent intent = null;

    private AsyncTaskListener listener = null;
    public SharedPreferences mpref = null;
    private ProgressDialog progressDialog= null;
    private HashMap<String, String> para = null;
    private RecyclerView recyclerView_sold_items=null;
    private ArrayList<String> arr_request_list = null;
    private ArrayList<String> arr_user_name = null;
    private ArrayList<String> arr_amount = null;
    private ArrayList<String> arr_photo = null;
    private ArrayList<String> arr_product_name = null;
    private ArrayList<String> arr_offer_till = null;
    private ArrayList<String> arr_expires_on = null;
    private ArrayList<String> arr_phone = null;
    private ArrayList<String> arr_currency = null;
    private ArrayList<SoldProductsBean> orderedProductsBean= null;
    SoldProductsDetailsAdapter adapter;
    private String str_username="";
    private String str_phone="";

    private TextView txt_count_donate_request=null;
    private ImageView img_product_donate=null;
    private Button btn_republish_product=null;
    private static final int MY_PERMISSIONS_CALL = 104;
    private TextView txt_posted_by_contact=null;
    int recycle_count=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sold_item_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderedProductsBean= new ArrayList<SoldProductsBean>();

        intent=getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("product_name_intent"));
        listener=SoldProductsDetailsActivity.this;
        progressDialog=new ProgressDialog(SoldProductsDetailsActivity.this);
        mpref = getApplicationContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (mpref != null)

        recyclerView_sold_items= (RecyclerView) findViewById(R.id.sold_items_list);
        recyclerView_sold_items.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_sold_items.setLayoutManager(layoutManager);
        recyclerView_sold_items.setNestedScrollingEnabled(false);
//        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
//        if (mpref != null)

        arr_request_list = (ArrayList<String>) intent.getSerializableExtra("request_id");
        arr_user_name = (ArrayList<String>) intent.getSerializableExtra("name");
        arr_amount = (ArrayList<String>) intent.getSerializableExtra("price");
        arr_photo = (ArrayList<String>) intent.getSerializableExtra("image");
        arr_product_name = (ArrayList<String>) intent.getSerializableExtra("product_name");
        arr_offer_till = (ArrayList<String>) intent.getSerializableExtra("offer_till");
        arr_expires_on = (ArrayList<String>) intent.getSerializableExtra("expires_on");
        arr_phone= (ArrayList<String>) intent.getSerializableExtra("phone");
        arr_currency=(ArrayList<String>) intent.getSerializableExtra("currency");

        txt_count_donate_request= (TextView) findViewById(R.id.txt_count_donate);
        img_product_donate= (ImageView) findViewById(R.id.img_product_image_donate);
        btn_republish_product= (Button) findViewById(R.id.btn_republish_product);

        txt_count_donate_request.setText(intent.getStringExtra("count")+" buyer(s) Responded");

        try {
            Picasso.with(getApplicationContext())
                    .load(intent.getStringExtra("product_image"))
                    .placeholder(R.drawable.no_image)
                    .fit().centerInside()
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(img_product_donate,new PicassoCallback(intent.getStringExtra("product_image")));
        }catch (Exception e){
            Picasso.with(getApplicationContext())
                    .load("http://52.41.70.254/pics/user.jpg")
                    .placeholder(R.drawable.no_image)
                    .fit().centerInside()
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .into(img_product_donate,new PicassoCallback("http://52.41.70.254/pics/user.jpg"));
        }


        Log.e("req_ids",arr_request_list.toString());

        if(arr_request_list.size()>0){
            for (int i = 0; i < arr_request_list.size(); i++){
                SoldProductsBean ordered_Products_Bean=new SoldProductsBean();
                ordered_Products_Bean.setRequestId(arr_request_list.get(i));
                ordered_Products_Bean.setUsername(arr_user_name.get(i));
                ordered_Products_Bean.setAmount(arr_amount.get(i));
                ordered_Products_Bean.setPhoto(arr_photo.get(i));
                ordered_Products_Bean.setProductImage(arr_product_name.get(i));
                ordered_Products_Bean.setOfferTill(arr_offer_till.get(i));
                ordered_Products_Bean.setExpireson(arr_expires_on.get(i));
                ordered_Products_Bean.setUser_phone(arr_phone.get(i));
                ordered_Products_Bean.setCurrency(arr_currency.get(i));

                orderedProductsBean.add(ordered_Products_Bean);
            }

        }

        if(orderedProductsBean.size()>0){
            try {
                adapter = new SoldProductsDetailsAdapter(getApplicationContext(), orderedProductsBean);
                recyclerView_sold_items.setAdapter(adapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        btn_republish_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SoldProductsDetailsActivity.this);
//                alertDialogBuilder.setMessage("Want to edit the product details before you Republish")
//                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                Intent intent = new Intent(getApplicationContext(), UpadteSellProductActivity.class);
//                                intent.putExtra("used_for",intent.getStringExtra("used_for"));
//                                intent.putExtra("condition",intent.getStringExtra("condition"));
//                                intent.putExtra("product_id",intent.getStringExtra("product_id"));
//                                intent.putExtra("product_name",intent.getStringExtra("product_name_intent"));
//                                intent.putExtra("product_image",intent.getStringExtra("product_image"));
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Re-Publish", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
                                para = new HashMap<>();
                                para.put("RequestId", arr_request_list.get(0));
                                para.put("OrderStatus", "2");
                                para.put("Code", "");
                                progressDialog.setMessage("Processing...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"RespondToRequest", para, 2, "republish");
                                httpAsync1.execute();
//                            }
//                        });
//                alertDialogBuilder.show();

            }
        });

        if(intent.getStringExtra("type").equalsIgnoreCase("2")){
            if(intent.getStringExtra("status").equalsIgnoreCase("0")){
                if(Integer.parseInt(intent.getStringExtra("count"))<5){
                    btn_republish_product.setVisibility(View.GONE);
                }
                else {
                    btn_republish_product.setVisibility(View.VISIBLE);
                }
            }
            else {
                btn_republish_product.setVisibility(View.GONE);
            }
        }
        else {
            if(Integer.parseInt(intent.getStringExtra("count"))<5){
                btn_republish_product.setVisibility(View.GONE);
            }
            else {
                btn_republish_product.setVisibility(View.VISIBLE);
            }
        }


        adapter.SetOnItemClickListener(new SoldProductsDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                if(orderedProductsBean.get(position).getExpireson().startsWith("-")){

                }
                else {

                    if(intent.getStringExtra("type").equalsIgnoreCase("2")){
                        if(intent.getStringExtra("status").equalsIgnoreCase("0")){
                            if(Integer.parseInt(intent.getStringExtra("count"))>5){
                                str_username=orderedProductsBean.get(position).getUsername();
                                str_phone=orderedProductsBean.get(position).getUser_phone();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SoldProductsDetailsActivity.this);
                                alertDialogBuilder.setMessage("Please Unpublish the product to view the contact")
                                        .setPositiveButton("Unpublish", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                para = new HashMap<>();
                                                para.put("RequestId", orderedProductsBean.get(position).getRequestId());
                                                para.put("OrderStatus", "1");
                                                para.put("Code", "");
                                                progressDialog.setMessage("Processing...");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();
                                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"RespondToRequest", para, 2, "respond");
                                                httpAsync1.execute();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
//                                para = new HashMap<>();
//                                para.put("RequestId", orderedProductsBean.get(position).getRequestId());
//                                para.put("Status", "2");
//
//                                progressDialog.setMessage("Processing...");
//                                progressDialog.setCancelable(false);
//                                progressDialog.show();
//                                HttpAsync httpAsync1 = new HttpAsync(getApplicationContext(), listener, "http://54.191.146.243:8088/RespondToRequest", para, 2, "respond");
//                                httpAsync1.execute();
//                                dialog.dismiss();
                                            }
                                        });
                                alertDialogBuilder.show();
                            }
                            else {
                                buildDialog(R.style.DialogAnimation_2, orderedProductsBean.get(position).getUsername(),orderedProductsBean.get(position).getUser_phone());
                            }

                        }else {

                            buildDialog(R.style.DialogAnimation_2, orderedProductsBean.get(position).getUsername(),orderedProductsBean.get(position).getUser_phone());
//                            DonatedByOthersContactDialog donatedByOthersContactDialog=new DonatedByOthersContactDialog(SoldProductsDetailsActivity.this,orderedProductsBean.get(position).getUsername(),orderedProductsBean.get(position).getUser_phone());
//                            donatedByOthersContactDialog.show();
//                            btn_republish_product.setVisibility(View.VISIBLE);
                        }

                    }
                    else {
                        buildDialog(R.style.DialogAnimation_2, orderedProductsBean.get(position).getUsername(),orderedProductsBean.get(position).getUser_phone());
//                        DonatedByOthersContactDialog donatedByOthersContactDialog=new DonatedByOthersContactDialog(SoldProductsDetailsActivity.this,orderedProductsBean.get(position).getUsername(),orderedProductsBean.get(position).getUser_phone());
//                        donatedByOthersContactDialog.show();
//                        btn_republish_product.setVisibility(View.VISIBLE);
                    }

                }


            }
        });


    }

    private void buildDialog(int animationSource, String username,String userphone) {
        final Dialog dialog = new Dialog(SoldProductsDetailsActivity.this);
        dialog.setContentView(R.layout.dialog_donated_by_contact);
//        dialog.setTitle("This is my custom dialog box");
        dialog.setCancelable(true);

        dialog.getWindow().getAttributes().windowAnimations = animationSource;
        dialog.show();

        TextView txt_posted_by_name= (TextView) dialog.findViewById(R.id.txt_posted_by_name);
        txt_posted_by_contact= (TextView) dialog.findViewById(R.id.txt_posted_by_phone);
        LinearLayout linear_call= (LinearLayout) dialog.findViewById(R.id.linear_call);
        ImageView img_call_phone= (ImageView) dialog.findViewById(R.id.img_call_phone);
        ImageView img_whatsapp_call= (ImageView) dialog.findViewById(R.id.img_whatsapp_call);

        txt_posted_by_name.setText(username);
        txt_posted_by_contact.setText(userphone);

        Button button_ok= (Button) dialog.findViewById(R.id.btn_ok);
        img_call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    //Marshmallow
                    // demo();
                    marshmellowPermission();
                    //  mainTask();
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + txt_posted_by_contact.getText().toString()));
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
                    startActivity(callIntent);
                }
            }
        });

        img_whatsapp_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNumberExist=isContactExist(txt_posted_by_contact.getText().toString());

                if (isNumberExist) {
                    Uri uri = Uri.parse("smsto:" + txt_posted_by_contact.getText());
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(i, ""));
                }
                else {
                    final CharSequence[] items = { "Send Message", "Save Contact",
                            "Cancel" };

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SoldProductsDetailsActivity.this);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            dialog.dismiss();
                            if (items[item].equals("Send Message")) {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + txt_posted_by_contact.getText().toString()));
                                intent.putExtra("sms_body", "");
                                startActivity(intent);

                            } else if (items[item].equals("Save Contact")) {
                                dialog.dismiss();
                                Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel:" + txt_posted_by_contact.getText())); //currentNum is my TextView, you can replace it with the number directly such as Uri.parse("tel:1293827")
                                intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true); //skips the dialog box that asks the user to confirm creation of contacts
                                startActivity(intent);

                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }

            }
        });

        Button button = (Button) dialog.findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();



            }
        });


    }

    private boolean isContactExist(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumer = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = this.getContentResolver().query(uri, mPhoneNumer, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return true;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    private void marshmellowPermission() {

        if (ContextCompat.checkSelfPermission(SoldProductsDetailsActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SoldProductsDetailsActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + txt_posted_by_contact.getText().toString()));
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
            startActivity(callIntent);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + txt_posted_by_contact.getText().toString()));
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
                    startActivity(callIntent);

                } else {
//                    finish();
                }
                return;
            }
        }
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
            if(tag.equalsIgnoreCase("respond")){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject != null){
                        if(jsonObject.getString("errFlag").equalsIgnoreCase("0")){
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
                            btn_republish_product.setVisibility(View.VISIBLE);
//                            DonatedByOthersContactDialog donatedByOthersContactDialog=new DonatedByOthersContactDialog(SoldProductsDetailsActivity.this,str_username,str_phone);
//                            donatedByOthersContactDialog.show();
                            buildDialog(R.style.DialogAnimation_2, str_username,str_phone);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("errMsg"),Toast.LENGTH_LONG).show();
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(tag.equalsIgnoreCase("republish")){
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
//                            DonatedByOthersContactDialog donatedByOthersContactDialog=new DonatedByOthersContactDialog(SoldProductsDetailsActivity.this,str_username,str_phone);
//                            donatedByOthersContactDialog.show();
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
            Picasso.with(getApplicationContext()).load("http://52.41.70.254/pics/user.jpg").into(img_product_donate);
        }
    }

}
