package com.iused.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import com.iused.R;
import com.iused.adapters.GalleryAdapter;
import com.iused.adapters.GridviewAdapter;
import com.iused.bean.CustomGallery;
import com.iused.utils.AsyncTaskListener;
import com.iused.utils.HotelierUploadGalleryImgs;
import com.iused.utils.UploadSellProductVideo;
import com.yovenny.videocompress.MediaController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by Lenovo on 23/10/2016.
 */

public class Sell_Products_Activity extends AppCompatActivity implements AsyncTaskListener {

    private ImageView img_add_image= null;
    private ImageView img_add_video= null;

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int ACTION_TAKE_VIDEO = 3;

    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";

    private static final String TAG = "TedPicker";
    public static ArrayList<String> image_uris = new ArrayList<>();
    private LinearLayout mSelectedImagesContainer=null;
    private HorizontalScrollView horizontalScrollView=null;
    private Uri mVideoUri;
    private TextView txt_video_success= null;
    private Button btn_next = null;
    private EditText edt_item_name= null;
    private EditText edt_used_for= null;
    private EditText edt_used_for_type= null;
    private EditText edt_condition= null;
    private EditText edt_select_category= null;
    private EditText edt_description= null;
    private String str_array_duration_type[] = new String[]{"In Days","In Weeks","In Months","In Years"};
    private String str_array_in_days[] = new String[]{"1 Day","2 Days","3 Days","4 Days","5 Days","6 Days","7 Days","8 Days","9 Days","10 Days","11 Days","12 Days","13 Days","14 Days","15 Days","16 Days","17 Days","18 Days",
            "19 Days","20 Days","21 Days","22 Days","23 Days","24 Days","25 Days","26 Days","27 Days","28 Days","29 Days","30 Days","31 Days"};
    private String str_array_in_weeks[] = new String[]{"1 Week","2 Weeks","3 Weeks","4 Weeks","5 Weeks","6 Weeks","7 Weeks","8 Weeks"};
    private String str_array_in_months[] = new String[]{"1 Month","2 Months","3 Months","4 Months","5 Months","6 Months","7 Months","8 Months","9 Months","10 Months","11 Months","12 Months"};
    private String str_array_in_years[] = new String[]{"1 Year","2 Years","3 Years","4 Years","5 Years","6 Years","7 Years","8 Years","9 Years","10 Years"};
    int pos_duration_type = 0;
    int pos_in_days = 0;
    int pos_in_weeks = 0;
    int pos_in_months = 0;
    int pos_in_years = 0;
    private String str_array_condition[] = new String[]{"Brand-new / Unused","Almost looking new","Well maintained","Refurbished","Few scratches","Little broken","Some repair work needed","Not working"};
    private String str_array_categories[]=null;
    private String str_array_categories_ids[]=null;
    int pos_condition = 0;
    int pos_category = 0;
    private String str_category_id= null;
    private RelativeLayout relative_used_time_type= null;
    private RelativeLayout relative_condition= null;
    private RelativeLayout relative_category= null;
    public SharedPreferences mpref = null;

    private static final int MY_PERMISSIONS_CAMERA = 104;
    private static final int MY_PERMISSIONS_STORAGE = 106;

    private static final int MY_PERMISSIONS_CAMERA_VIDEO = 108;
    private static final int MY_PERMISSIONS_STORAGE_VIDEO = 109;

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 0;
    private final int VIDEO_REQUEST_CODE=10;
    private static int RESULT_OK=-1;
    String url;
    String picturePath;
    public String str_file_logo = "";
    private ImageView image = null;
    File file_logo = null;

    private static String bucketName = "mymarketbucket";
    private Uri fileUri;

//    Handler handler = new Handler();

    String mCurrentPhotoPath;

    File photoFile = null;
    private ArrayList<String> ar_category_name = new ArrayList<String>();
    private ArrayList<String> ar_category_id = new ArrayList<String>();

    public static GridView gridGallery;
    Handler handler;
    public static GalleryAdapter adapter;
    public static ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
    String action;
//    ViewSwitcher viewSwitcher;
    public static CustomGallery item = null;
    private ProgressDialog progressDialog= null;

    private GridviewAdapter mAdapter;
    public static ArrayList<String> list_images;
    ImageView image_delete= null;
    LinearLayout linear_add_images= null;
    public View view=null;

    private RelativeLayout relativelayout_video=null;
    private ImageView img_video_image=null;
    private ImageView img_delete_video=null;
    public static String str_video_url=null;
    private Button btn_next_gray=null;
    String path_video;
    String picture_path_trimmed_video;
    private Typeface face=null;

    String video_outPath;
    String video_outpath_trimmed_video;
    public static final String APP_DIR = "VideoCompressor";
    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";
    public static final String TEMP_DIR = "/Temp/";


    public static void try2CreateCompressDir() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + TEMP_DIR);
        f.mkdirs();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sell_product);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sell an Item");

        mpref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (mpref != null)

        mSelectedImagesContainer = (LinearLayout) findViewById(R.id.selected_photos_container_sell);
        horizontalScrollView= (HorizontalScrollView) findViewById(R.id.hori_scroll_view_sell);
        progressDialog= new ProgressDialog(Sell_Products_Activity.this);

        image=new ImageView(getApplicationContext());
        image_delete=new ImageView(getApplicationContext());
        view=new View(getApplicationContext());

        relativelayout_video= (RelativeLayout) findViewById(R.id.thumbview_video);
        img_video_image= (ImageView) findViewById(R.id.img_video);
        img_delete_video= (ImageView) findViewById(R.id.img_delete_video);
        btn_next_gray= (Button) findViewById(R.id.btn_next_gray);

        list_images= new ArrayList<String>();
        dataT.clear();
        image_uris.clear();

        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext());
        adapter.setMultiplePick(false);


//        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
//        viewSwitcher.setDisplayedChild(1);
        img_delete_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_video_url="";
                relativelayout_video.setVisibility(View.GONE);
            }
        });

        image_uris=new ArrayList<>();
        item = new CustomGallery();

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        int size = mpref.getInt("array_size", 0);
        str_array_categories = new String[size];
        for(int i=0; i<size; i++){
            mpref.getString("array_" + i, null);
//            Log.e("arr_categ",mpref.getString("array_" + i, null));
            ar_category_name.add(mpref.getString("array_" + i, null));
//            ar_category_name.remove("All");
//            TreeSet<String> unique = new TreeSet<String>(ar_category_name);
//
//            str_array_categories=new String[unique.size()];
//            str_array_categories = unique.toArray(new String[unique.size()]);
            ArrayList<String> unique = removeDuplicates(ar_category_name);
            str_array_categories=unique.toArray(str_array_categories);
        }

        int size1 = mpref.getInt("array_size_id", 0);
        str_array_categories_ids = new String[size1];
        for(int i=0; i<size1; i++){
            mpref.getString("array_id" + i, null);
//            Log.e("arr_id",mpref.getString("array_id" + i, null));
            ar_category_id.add(mpref.getString("array_id" + i, null));
//            ar_category_id.remove("");
//            TreeSet<String> unique = new TreeSet<String>(ar_category_id);
//
//            str_array_categories_ids=new String[unique.size()];
//            str_array_categories_ids = unique.toArray(new String[unique.size()]);
            ArrayList<String> unique1 = removeDuplicates(ar_category_id);
            str_array_categories_ids=unique1.toArray(str_array_categories_ids);
        }

        txt_video_success= (TextView) findViewById(R.id.txt_video_success_message);
        img_add_image= (ImageView) findViewById(R.id.img_add_galery);
        img_add_video= (ImageView) findViewById(R.id.img_add_video);
        edt_item_name= (EditText) findViewById(R.id.edt_item_name);
        edt_used_for= (EditText) findViewById(R.id.edt_used_time);
        edt_used_for_type= (EditText) findViewById(R.id.edt_used_time_type);
        edt_condition= (EditText) findViewById(R.id.edt_condition);
        edt_select_category= (EditText) findViewById(R.id.edt_category);
        edt_description= (EditText) findViewById(R.id.edt_description);
        btn_next = (Button) findViewById(R.id.btn_next);
        relative_used_time_type= (RelativeLayout) findViewById(R.id.relative_used_time_type);
        relative_condition= (RelativeLayout) findViewById(R.id.relative_condition);
        relative_category= (RelativeLayout) findViewById(R.id.relative_category);

        edt_used_for_type.setTag(edt_used_for_type.getKeyListener());
        edt_used_for_type.setKeyListener(null);
        edt_condition.setTag(edt_condition.getKeyListener());
        edt_condition.setKeyListener(null);
        edt_used_for.setTag(edt_used_for.getKeyListener());
        edt_used_for.setKeyListener(null);
        edt_select_category.setTag(edt_select_category.getKeyListener());
        edt_select_category.setKeyListener(null);

        face= Typeface.createFromAsset(getAssets(), "fonts/bariolreg.otf");
        edt_item_name.setTypeface(face);
        edt_used_for.setTypeface(face);
        edt_used_for_type.setTypeface(face);
        edt_condition.setTypeface(face);
        edt_select_category.setTypeface(face);


//        InputFilter filter = new InputFilter() {
//            public CharSequence filter(CharSequence source, int start, int end,
//                                       Spanned dest, int dstart, int dend) {
//                for (int i = start; i < end; i++) {
//                    if (!Character.isLetterOrDigit(source.charAt(i))) {
//                        return "";
//                    }
//                }
//                return null;
//            }
//        };
//        edt_item_name.setFilters(new InputFilter[] { filter });
//        edt_description.setFilters(new InputFilter[] { filter });

//        edt_description.setTypeface(face);

//        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dataT.remove(position);
////					data.clear();
//                image_uris.remove(position);
//
////                Toast.makeText(getApplicationContext(),"Removed",Toast.LENGTH_SHORT).show();
//            }
//        });

        img_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    //Marshmallow
                    // demo();
                    marshmellowPermission_video();
                    //  mainTask();
                } else if (Build.VERSION.SDK_INT >= 23){

                    marshmellowPermissionStorage_video();

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
                else {
                    addvideo();
                }


            }
        });

        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(image_uris.size()>=3){
                    Toast.makeText(getApplicationContext(),"Maximum of 3 images can be uploaded",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //Marshmallow
                        // demo();
                        marshmellowPermission();
                        //  mainTask();
                    } else if(Build.VERSION.SDK_INT >= 23){

                        marshmellowPermissionStorage();

//                        Config config = new Config();
//                        config.setCameraHeight(R.dimen.app_camera_height);
//                        config.setToolbarTitleRes(R.string.custom_title);
//                        config.setSelectionMin(1);
//                        config.setSelectionLimit(10);
//                        config.setSelectedBottomHeight(R.dimen.bottom_height);
//                        config.setFlashOn(true);
//                        getImages(config);

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
                    else {
                        selectImage();
                    }
                }

            }
        });

//        mSelectedImagesContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                int position  = image_uris.size();
//                Toast.makeText(getApplicationContext(), "position ", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        edt_used_for_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_item_name.getWindowToken(), 0);

                final AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
                builder.setTitle("Select a Type");
                builder.setSingleChoiceItems(str_array_duration_type, pos_duration_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt_used_for_type.setText(str_array_duration_type[which]);
                        edt_used_for.setText("");
                        dialog.dismiss();
                        pos_duration_type = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    }
                });

                builder.show();
            }
        });


        edt_used_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_item_name.getWindowToken(), 0);

                if(edt_used_for_type.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Select a duration type",Toast.LENGTH_SHORT).show();
                }
                else if(edt_used_for_type.getText().toString().equalsIgnoreCase("In Days")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
                    builder.setTitle("How Old the item");
                    builder.setSingleChoiceItems(str_array_in_days, pos_in_days, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edt_used_for.setText(str_array_in_days[which]);
                            dialog.dismiss();
                            pos_in_days = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        }
                    });

                    builder.show();
                }
                else if(edt_used_for_type.getText().toString().equalsIgnoreCase("In Weeks")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
                    builder.setTitle("How old the item");
                    builder.setSingleChoiceItems(str_array_in_weeks, pos_in_weeks, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edt_used_for.setText(str_array_in_weeks[which]);
                            dialog.dismiss();
                            pos_in_weeks = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        }
                    });

                    builder.show();
                }
                else if(edt_used_for_type.getText().toString().equalsIgnoreCase("In Months")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
                    builder.setTitle("How old the item");
                    builder.setSingleChoiceItems(str_array_in_months, pos_in_months, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edt_used_for.setText(str_array_in_months[which]);
                            dialog.dismiss();
                            pos_in_months = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        }
                    });

                    builder.show();
                }
                else if(edt_used_for_type.getText().toString().equalsIgnoreCase("In Years")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
                    builder.setTitle("How old the item");
                    builder.setSingleChoiceItems(str_array_in_years, pos_in_years, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edt_used_for.setText(str_array_in_years[which]);
                            dialog.dismiss();
                            pos_in_years = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        }
                    });

                    builder.show();
                }

            }
        });

        edt_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_item_name.getWindowToken(), 0);

                final AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
                builder.setTitle("Select Condition");
                builder.setSingleChoiceItems(str_array_condition, pos_condition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt_condition.setText(str_array_condition[which]);
                        dialog.dismiss();
                        pos_condition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    }
                });

                builder.show();
            }
        });

        edt_select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_item_name.getWindowToken(), 0);

                final AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
                builder.setTitle("Select a Category");
                builder.setSingleChoiceItems(str_array_categories, pos_category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt_select_category.setText(str_array_categories[which]);
                        str_category_id= str_array_categories_ids[which];
                        dialog.dismiss();
                        pos_category = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    }
                });

                builder.show();
            }
        });


        edt_item_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_used_for_type.getText().toString().equalsIgnoreCase("")||edt_used_for.getText().toString().equalsIgnoreCase("")||edt_condition.getText().toString().equalsIgnoreCase("")||edt_select_category.getText().toString().equalsIgnoreCase("")||edt_description.getText().toString().equalsIgnoreCase("")){
                    btn_next.setVisibility(View.GONE);
                    btn_next_gray.setVisibility(View.VISIBLE);
                }
                else {
                    btn_next.setVisibility(View.VISIBLE);
                    btn_next_gray.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_item_name.getText().toString().equalsIgnoreCase("")){
                    btn_next.setVisibility(View.GONE);
                    btn_next_gray.setVisibility(View.VISIBLE);
                }
                else {
                    btn_next.setVisibility(View.VISIBLE);
                    btn_next_gray.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_item_name.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edt_used_for.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edt_used_for_type.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edt_select_category.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edt_description.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edt_condition.getWindowToken(), 0);

                Log.e("image_uris",image_uris.toString());
                Log.e("image_",dataT.toString());

                if(image_uris.size()==0){
                    Toast.makeText(getApplicationContext(),"Please add at least one image",Toast.LENGTH_SHORT).show();
                }
                else if(edt_item_name.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter item name",Toast.LENGTH_SHORT).show();
                }
                else if (!edt_item_name.getText().toString().matches("[a-zA-Z0-9.? ]*")) {
                    Toast aviso = Toast
                            .makeText(
                                    getApplicationContext(),
                                    "Please Remove Special Character from Item Name",
                                    Toast.LENGTH_LONG);
                    aviso.show();

                }
                else if(edt_used_for.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter the age of item",Toast.LENGTH_SHORT).show();
                }
                else if(edt_used_for_type.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Select Duration Type",Toast.LENGTH_SHORT).show();
                }
                else if(edt_condition.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter current condition of item",Toast.LENGTH_SHORT).show();
                }
                else if(edt_select_category.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Choose the relevant category",Toast.LENGTH_SHORT).show();
                }
                else if(edt_description.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please provide the item description",Toast.LENGTH_SHORT).show();
                }
                else if(edt_description.getText().toString().length()<100){
                    Toast.makeText(getApplicationContext(),"Description too short",Toast.LENGTH_SHORT).show();
                }
                else if (!edt_description.getText().toString().matches("[a-zA-Z0-9.? ]*")) {
                    Toast aviso = Toast
                            .makeText(
                                    getApplicationContext(),
                                    "Please Remove Special Character from Description",
                                    Toast.LENGTH_LONG);
                    aviso.show();

                }
                else {
                    Intent intent=new Intent(getApplicationContext(),SetPriceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {
                        intent.putExtra("image_urls",image_uris.toString());
                        intent.putExtra("video_url",str_video_url.toString());
                    }catch (Exception e){
                        intent.putExtra("image_urls","");
                        intent.putExtra("video_url","");
                    }

                    intent.putExtra("item_name",edt_item_name.getText().toString().trim());
                    intent.putExtra("used_for",edt_used_for.getText().toString());
                    intent.putExtra("used_for_type",edt_used_for_type.getText().toString());
                    intent.putExtra("category_id",str_category_id);
                    intent.putExtra("condition",edt_condition.getText().toString());
                    intent.putExtra("decription",edt_description.getText().toString().trim());
                    startActivity(intent);
                }

            }
        });

    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    private void marshmellowPermissionStorage_video() {


        if (ContextCompat.checkSelfPermission(Sell_Products_Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Sell_Products_Activity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_STORAGE_VIDEO);
        } else {
//            Config config = new Config();
//            config.setCameraHeight(R.dimen.app_camera_height);
//            config.setToolbarTitleRes(R.string.custom_title);
//            config.setSelectionMin(1);
//            config.setSelectionLimit(10);
//            config.setSelectedBottomHeight(R.dimen.bottom_height);
//            config.setFlashOn(true);
//            getImages(config);
//            dispatchTakeVideoIntent();
            addvideo();
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

    private void marshmellowPermission_video() {
        if (ContextCompat.checkSelfPermission(Sell_Products_Activity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Sell_Products_Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_CAMERA_VIDEO);
        } else {
            marshmellowPermissionStorage_video();
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


    private void marshmellowPermission() {

        if (ContextCompat.checkSelfPermission(Sell_Products_Activity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Sell_Products_Activity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_CAMERA);
        } else {
            marshmellowPermissionStorage();
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

    private void marshmellowPermissionStorage() {


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Sell_Products_Activity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_STORAGE);
        } else {
//            Config config = new Config();
//            config.setCameraHeight(R.dimen.app_camera_height);
//            config.setToolbarTitleRes(R.string.custom_title);
//            config.setSelectionMin(1);
//            config.setSelectionLimit(10);
//            config.setSelectedBottomHeight(R.dimen.bottom_height);
//            config.setFlashOn(true);
//            getImages(config);
            selectImage();
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

            case MY_PERMISSIONS_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    marshmellowPermissionStorage();
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
                    Toast.makeText(getApplicationContext(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case MY_PERMISSIONS_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    Config config = new Config();
//                    config.setCameraHeight(R.dimen.app_camera_height);
//                    config.setToolbarTitleRes(R.string.custom_title);
//                    config.setSelectionMin(1);
//                    config.setSelectionLimit(10);
//                    config.setSelectedBottomHeight(R.dimen.bottom_height);
//                    config.setFlashOn(true);
//                    getImages(config);
                    selectImage();
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
                    Toast.makeText(getApplicationContext(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case MY_PERMISSIONS_CAMERA_VIDEO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    marshmellowPermissionStorage_video();
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
                    Toast.makeText(getApplicationContext(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case MY_PERMISSIONS_STORAGE_VIDEO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    addvideo();
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
                    Toast.makeText(getApplicationContext(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }

    private void handleCameraVideo(Intent intent) {
        mVideoUri = intent.getData();
//        mVideoView.setVideoURI(mVideoUri);
        if(mVideoUri.toString().equalsIgnoreCase("")){
            Log.e("video_url",mVideoUri.toString());
            txt_video_success.setVisibility(View.GONE);
        }
        else {
            txt_video_success.setVisibility(View.VISIBLE);
        }

//        mImageBitmap = null;
//        mVideoView.setVisibility(View.VISIBLE);
//        mImageView.setVisibility(View.INVISIBLE);
    }

    private void selectImage() {
//        buildDialog();

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Products_Activity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (items[item].equals("Take Photo")) {

                    dialog.dismiss();
//                    if (Build.VERSION.SDK_INT >= 23) {
//                        //Marshmallow
//                        // demo();
//                        marshmellowPermissionCamera();
//                        //  mainTask();
//                    } else {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File f = new File(android.os.Environment
//                                .getExternalStorageDirectory(), "temp.jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                        startActivityForResult(intent, CAMERA_REQUEST);
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);

//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//                    // start the image capture Intent
//                    startActivityForResult(intent, CAMERA_REQUEST);

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
                    }
//                    }

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
//                        }
                    }

                } else if (items[item].equals("Choose from Library")) {
                    dialog.dismiss();
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    startActivityForResult(
                            intent,
                            SELECT_FILE);

//                    Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
//                    startActivityForResult(i, SELECT_FILE);

//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent,
//                            "Select file to upload "), SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    RESULT_OK=0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void addvideo(){
        Intent calVideo = new Intent();
        calVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
        calVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        calVideo.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(calVideo, VIDEO_REQUEST_CODE);
    }

//    private void buildDialog() {
//
//        final Dialog dialog = new Dialog(Sell_Products_Activity.this);
//        dialog.setContentView(R.layout.dialog_take_select_photo);
////        dialog.setTitle("This is my custom dialog box");
//        dialog.setCancelable(true);
//
//
//        TextView txt_take_photo= (TextView) dialog.findViewById(R.id.txt_take_photo);
//        TextView txt_add_photo= (TextView) dialog.findViewById(R.id.txt_add_photo);
//        TextView txt_cancel= (TextView) dialog.findViewById(R.id.txt_cancel);
//
//        txt_take_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
////                    if (Build.VERSION.SDK_INT >= 23) {
////                        //Marshmallow
////                        // demo();
////                        marshmellowPermissionCamera();
////                        //  mainTask();
////                    } else {
////                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                        File f = new File(android.os.Environment
////                                .getExternalStorageDirectory(), "temp.jpg");
////                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
////                        startActivityForResult(intent, CAMERA_REQUEST);
////                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//////                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
////                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
//
////                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////
////                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
////
////                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
////
////                    // start the image capture Intent
////                    startActivityForResult(intent, CAMERA_REQUEST);
//
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                // Ensure that there's a camera activity to handle the intent
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    // Create the File where the photo should go
//                    File photoFile = null;
//                    try {
//                        photoFile = createImageFile();
//                    } catch (IOException ex) {
//                        // Error occurred while creating the File
//
//                    }
//                    // Continue only if the File was successfully created
//                    if (photoFile != null) {
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(photoFile));
//                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
//                    }
//                }
////                    }
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
////                            // TODO: Consider calling
////                            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
////                            // here to request the missing permissions, and then overriding
////                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                            //                                          int[] grantResults)
////                            // to handle the case where the user grants the permission. See the documentation
////                            // for Activity#requestPermissions for more details.
////                            return;
////                        }
////                        }
//                }
//            }
//        });
//
//        txt_add_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Intent intent = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//
//                startActivityForResult(
//                        Intent.createChooser(intent, "Select File"),
//                        SELECT_FILE);
//            }
//        });
//
//        txt_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RESULT_OK=0;
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//
//    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e(TAG, "photo path = " + mCurrentPhotoPath);

//        if(!mCurrentPhotoPath.isEmpty()){
//            setPic();
//        }

        return image;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + "Android File Upload" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void setPic() {
        // Get the dimensions of the View
        Log.e("done","entered");
//        image = new ImageView(getApplicationContext());
        int targetW = image.getWidth();
        int targetH = image.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/200, photoH/200);
//        int scaleFactor = Math.min(800, 800);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
//        Log.e("rotated",bitmap.toString());

        if (scaled != null) {
            // encodeTobase64(bitmap);
            mCurrentPhotoPath = getRealPathFromURI(getImageUri(getApplicationContext(), scaled));
            Log.e("real_pic_path", mCurrentPhotoPath);
            file_logo = new File(mCurrentPhotoPath);
            Log.e("file_picture",file_logo.toString());

            str_file_logo=file_logo.toString();

        }

        Matrix mtx = new Matrix();
//        mtx.postRotate(90);
        // Rotating Bitmap
        Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
        Log.e("rotated",rotatedBMP.toString());
//        Bitmap converetdImage = getResizedBitmap(rotatedBMP, 512);

        if (rotatedBMP != bitmap)
            bitmap.recycle();


            Log.e("trimmed_new",mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/") + 1));
            String picture_path_trimmed_camera=mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/") + 1);



        new HotelierUploadGalleryImgs(Sell_Products_Activity.this, picture_path_trimmed_camera,mCurrentPhotoPath).execute();


        item=new CustomGallery();
        item.sdcardPath = rotatedBMP;

        dataT.add(item);
        adapter.addAll(dataT);
        gridGallery.setAdapter(adapter);

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {

            if (resultCode == Activity.RESULT_OK){

//                try {

                try {
                    setPic();
                }catch (Exception e){

                }

            }

//            }
            else if (resultCode == Activity.RESULT_CANCELED){

            }

        }
        else if(requestCode==SELECT_FILE){
            if (resultCode == Activity.RESULT_OK){

                try {
                    String tempPath =null;
                    Uri selectedImageUri = data.getData();
                    Log.e("temp_path_init",selectedImageUri.toString());
//                    Log.e("temp_trimmed",selectedImageUri.toString().substring(selectedImageUri.toString().lastIndexOf(":///")+4));

                    if(selectedImageUri.toString().startsWith("file:///")){
                        tempPath = selectedImageUri.toString().substring(selectedImageUri.toString().lastIndexOf(":///")+4);
                        Log.e("temp_path_gal",tempPath);
                    }
                    else {
                        tempPath = getPath(selectedImageUri, Sell_Products_Activity.this);
                        Log.e("temp_path_pho",tempPath);
                    }

//                Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);

                    Bitmap bitmapImage = BitmapFactory.decodeFile(tempPath, btmapOptions);
                    int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);


                    item = new CustomGallery();
                    item.sdcardPath = scaled;
                    dataT.add(item);
                    adapter.addAll(dataT);
                    gridGallery.setAdapter(adapter);

                    if (scaled != null) {
                        // encodeTobase64(bitmap);
                        tempPath = getRealPathFromURI(getImageUri(getApplicationContext(), scaled));
                        Log.e("real_pic_path", tempPath);
                        file_logo = new File(tempPath);
                        Log.e("file_picture",file_logo.toString());

                        str_file_logo=file_logo.toString();
                        // PASS "file" AS YOUR IMAGE PARAMETER IN JSON

                    }


                    String picture_path_trimmed=tempPath.substring(tempPath.lastIndexOf("/") + 1);
                    Log.e("trimmed",picture_path_trimmed);


                    new HotelierUploadGalleryImgs(Sell_Products_Activity.this, picture_path_trimmed,tempPath).execute();


                }catch (Exception e){

                }

            }
            else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        else if (requestCode == VIDEO_REQUEST_CODE ) {
            if (resultCode == Activity.RESULT_OK){

//                String tempPath =null;
                Uri video_uri = data.getData();
                Log.e("video_uri",video_uri.toString());

                path_video = getRealPathFromURI(video_uri);
                Log.e("video_path",path_video);
//                Toast.makeText(getApplicationContext(),path, Toast.LENGTH_LONG).show();

                picture_path_trimmed_video=path_video.substring(path_video.lastIndexOf("/") + 1);
                Log.e("trimmed",picture_path_trimmed_video);

                try2CreateCompressDir();
                video_outPath=Environment.getExternalStorageDirectory()
                        + File.separator
                        + APP_DIR
                        + COMPRESSED_VIDEOS_DIR
                        +"VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
                Log.e("new_path",video_outPath);
                video_outpath_trimmed_video=video_outPath.substring(video_outPath.lastIndexOf("/") + 1);
                Log.e("trimmed",video_outpath_trimmed_video);

                new VideoCompressor().execute(path_video,video_outPath);

//                new UploadSellProductVideo(Sell_Products_Activity.this, picture_path_trimmed_video,path_video).execute();


                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path_video,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                img_video_image.setBackgroundDrawable(bitmapDrawable);
                relativelayout_video.setVisibility(View.VISIBLE);
            }
            else if(resultCode == Activity.RESULT_CANCELED){

            }

        }
        else if(requestCode == ACTION_TAKE_VIDEO){
            if (resultCode == Activity.RESULT_OK){
                handleCameraVideo(data);
            }
            else if(resultCode == Activity.RESULT_CANCELED){

            }
        }

    }

    class VideoCompressor extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG,"Start video compression");
            progressDialog.setMessage("Compressing Video...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return MediaController.getInstance().convertVideo(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
//            progressBar.setVisibility(View.GONE);
            if(compressed){
                Log.e(TAG,"Compression successfully!");
                new UploadSellProductVideo(Sell_Products_Activity.this, video_outpath_trimmed_video,video_outPath).execute();
                progressDialog.dismiss();
            }
        }
    }

    public String getPathNew(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static void UploadObject(URL url) throws IOException
    {
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
                connection.getOutputStream());
        out.write("This text uploaded as object.");
        out.close();
        int responseCode = connection.getResponseCode();
        System.out.println("Service returned response code " + responseCode);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @SuppressWarnings("deprecation")
    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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

    }
}
