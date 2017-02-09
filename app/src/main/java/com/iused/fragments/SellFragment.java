package com.iused.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.bumptech.glide.Glide;

import com.iused.R;
import com.iused.main.MainActivity;
import com.iused.main.SetPriceActivity;
import com.iused.utils.AsyncTaskListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Antto on 04-10-2016.
 */
public class SellFragment extends Fragment implements AsyncTaskListener{

    View view = null;
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
    private String str_array_duration_type[] = new String[]{"Days","Weeks","Months","Years"};
    int pos_duration_type = 0;
    private String str_array_condition[] = new String[]{"Good","Better","Best"};
    int pos_condition = 0;
    int pos_category = 0;
    private String str_category_id= null;
    private RelativeLayout relative_used_time_type= null;
    private RelativeLayout relative_condition= null;
    private RelativeLayout relative_category= null;

    private static final int MY_PERMISSIONS_CAMERA = 104;
    private static final int MY_PERMISSIONS_STORAGE = 106;

    private static final int MY_PERMISSIONS_CAMERA_VIDEO = 108;
    private static final int MY_PERMISSIONS_STORAGE_VIDEO = 109;

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 0;
    private static int RESULT_OK=-1;
    String url;
    String picturePath;
    public String str_file_logo = "";
    private ImageView image = null;

    private static String bucketName = "mymarketbucket";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            view = inflater.inflate(R.layout.fragment_sell, container, false);

            mSelectedImagesContainer = (LinearLayout) view.findViewById(R.id.selected_photos_container_sell);
            horizontalScrollView= (HorizontalScrollView) view.findViewById(R.id.hori_scroll_view_sell);
            image_uris=new ArrayList<>();

            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }


            txt_video_success= (TextView) view.findViewById(R.id.txt_video_success_message);
            img_add_image= (ImageView) view.findViewById(R.id.img_add_galery);
            img_add_video= (ImageView) view.findViewById(R.id.img_add_video);
            edt_item_name= (EditText) view.findViewById(R.id.edt_item_name);
            edt_used_for= (EditText) view.findViewById(R.id.edt_used_time);
            edt_used_for_type= (EditText) view.findViewById(R.id.edt_used_time_type);
            edt_condition= (EditText) view.findViewById(R.id.edt_condition);
            edt_select_category= (EditText) view.findViewById(R.id.edt_category);
            edt_description= (EditText) view.findViewById(R.id.edt_description);
            btn_next = (Button) view.findViewById(R.id.btn_next);
            relative_used_time_type= (RelativeLayout) view.findViewById(R.id.relative_used_time_type);
            relative_condition= (RelativeLayout) view.findViewById(R.id.relative_condition);
            relative_category= (RelativeLayout) view.findViewById(R.id.relative_category);

            edt_used_for_type.setTag(edt_used_for_type.getKeyListener());
            edt_used_for_type.setKeyListener(null);
            edt_condition.setTag(edt_condition.getKeyListener());
            edt_condition.setKeyListener(null);
            edt_select_category.setTag(edt_select_category.getKeyListener());
            edt_select_category.setKeyListener(null);

            img_add_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        //Marshmallow
                        // demo();
                        marshmellowPermission_video();
                        //  mainTask();
                    } else {

                        marshmellowPermissionStorage_video();

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


                }
            });

            img_add_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(image_uris.size()>=5){
                        Toast.makeText(getActivity(),"Maximum of 5 images can be uploaded",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= 23) {
                            //Marshmallow
                            // demo();
                            marshmellowPermission();
                            //  mainTask();
                        } else {

                            marshmellowPermissionStorage();

//                        Config config = new Config();
//                        config.setCameraHeight(R.dimen.app_camera_height);
//                        config.setToolbarTitleRes(R.string.custom_title);
//                        config.setSelectionMin(1);
//                        config.setSelectionLimit(10);
//                        config.setSelectedBottomHeight(R.dimen.bottom_height);
//                        config.setFlashOn(true);
//                        getImages(config);
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
                        }
                    }

                }
            });

            edt_used_for_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Select a Type");
                    builder.setSingleChoiceItems(str_array_duration_type, pos_duration_type, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edt_used_for_type.setText(str_array_duration_type[which]);
                            dialog.dismiss();
                            pos_duration_type = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        }
                    });

                    builder.show();
                }
            });

            edt_condition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Select a Category");
                    builder.setSingleChoiceItems(MainActivity.str_arr_category_name, pos_category, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edt_select_category.setText(MainActivity.str_arr_category_name[which]);
                            str_category_id= MainActivity.str_arr_category_id[which];
                            dialog.dismiss();
                            pos_category = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        }
                    });

                    builder.show();
                }
            });

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("image_uris",image_uris.toString());

                    if(image_uris.size()==0){
                        Toast.makeText(getActivity(),"Select atleast one image of product",Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_used_for.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getActivity(),"Enter how old the item is",Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_used_for_type.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getActivity(),"Select Duration Type",Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_select_category.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getActivity(),"Select a Category",Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_description.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getActivity(),"Enter Item Description",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent=new Intent(getActivity(),SetPriceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        try {
                            intent.putExtra("image_urls",image_uris.toString());
                            intent.putExtra("video_url",mVideoUri.toString());
                        }catch (Exception e){
                            intent.putExtra("image_urls","");
                            intent.putExtra("video_url","");
                        }

                        intent.putExtra("item_name",edt_item_name.getText().toString());
                        intent.putExtra("used_for",edt_used_for.getText().toString());
                        intent.putExtra("used_for_type",edt_used_for_type.getText().toString());
                        intent.putExtra("category_id",str_category_id);
                        intent.putExtra("condition",edt_condition.getText().toString());
                        intent.putExtra("decription",edt_description.getText().toString());
                        startActivity(intent);
                    }

                }
            });


        }catch (Exception e){

        }

        return view;
    }

    private void marshmellowPermissionStorage_video() {


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
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
            dispatchTakeVideoIntent();
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
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
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

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
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


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
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
                    Toast.makeText(getActivity(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case MY_PERMISSIONS_STORAGE_VIDEO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchTakeVideoIntent();
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
                    Toast.makeText(getActivity(), "Please allow permission to use this feature", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

//    private void getImages(Config config) {
//
//
//        ImagePickerActivity.setConfig(config);
//
//        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
//
//        if (image_uris != null) {
//            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
//        }
//
//
//        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
//
//    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {

            if (resultCode == Activity.RESULT_OK){
//                image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//
//                Log.e("image_urls",image_uris.toString());
//                if (image_uris != null) {
//                    showMedia();
//                }

//                if (requestCode == CAMERA_REQUEST) {

//                try {
//                    createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, getActivity());
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
//                image_company_logo.setImageBitmap(bm);
                image = new ImageView(getActivity());
                image.setImageBitmap(bm);
                image.setLayoutParams(new ViewGroup.LayoutParams(150,150));
                image.setMaxHeight(150);
                image.setMaxWidth(150);

                // Adds the view to the layout
                mSelectedImagesContainer.addView(image);

                Uri u = data.getData();
                Log.e("uiri",u.toString());

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(u,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath_camera = cursor.getString(columnIndex);
                Log.e("picturePath", picturePath_camera);
                Log.e("trimmed",picturePath_camera.substring(picturePath_camera.lastIndexOf("/") + 1));
                String picture_path_trimmed_camera=picturePath_camera.substring(picturePath_camera.lastIndexOf("/") + 1);

                AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( "AKIAJ3YGIVONBRYID3VA", "XDaZKNMTGO+Ap4ICF877oP1MhEQQYV8I/aySbV50" ) );
//				s3Client.createBucket( "mymarketbucket" );
                File filePath = new File(picturePath_camera);
                PutObjectRequest por = new PutObjectRequest( "mymarketbucket",picture_path_trimmed_camera, filePath);
                s3Client.putObject( por );

                ResponseHeaderOverrides override = new ResponseHeaderOverrides();
                override.setContentType( "image/jpeg" );

                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( "mymarketbucket", picture_path_trimmed_camera);
//                urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600000 ) );  // Added an hour's worth of milliseconds to the current time.
                urlRequest.setResponseHeaders( override );

                URL url = s3Client.generatePresignedUrl( urlRequest );
                Log.e("url_final",url.toString());
                image_uris.add(url.toString());

//                File f = new File(Environment.getExternalStorageDirectory()
//                            .toString());
//                    for (File temp : f.listFiles()) {
//                        if (temp.getName().equals("temp.jpg")) {
//                            f = temp;
//                            break;
//                        }
//                    }
//                    try {
//                        Bitmap bm;
//                        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//
//                        bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                                btmapOptions);
//
//                        // bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
////                        image_company_logo.setImageBitmap(bm);
//
//                        image = new ImageView(getActivity());
//                        image.setImageBitmap(bm);
//                        image.setLayoutParams(new android.view.ViewGroup.LayoutParams(150,150));
//                        image.setMaxHeight(150);
//                        image.setMaxWidth(150);
//
//                        // Adds the view to the layout
//                        mSelectedImagesContainer.addView(image);
//
//
//                        String path = android.os.Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "Phoenix" + File.separator + "default";
//                        Log.e("file_path",path);
//                        f.delete();
//                        OutputStream fOut = null;
//                        File file_logo = new File(path, String.valueOf(System
//                                .currentTimeMillis()) + ".jpg");
//                        Log.e("file_camera",file_logo.toString());
//                        str_file_logo=file_logo.toString();
//                        Log.e("trimmed",file_logo.toString().substring(file_logo.toString().lastIndexOf("/") + 1));
//                        String picture_path_trimmed=file_logo.toString().substring(file_logo.toString().lastIndexOf("/") + 1);
//                        image_uris.add("https://s3-us-west-2.amazonaws.com/mymarketbucket/"+picture_path_trimmed);

//                        AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( "AKIAJ3YGIVONBRYID3VA", "XDaZKNMTGO+Ap4ICF877oP1MhEQQYV8I/aySbV50" ) );
////				s3Client.createBucket( "mymarketbucket" );
//                        File filePath = new File(str_file_logo);
//                        PutObjectRequest por = new PutObjectRequest( "mymarketbucket",picture_path_trimmed, filePath);
//                        s3Client.putObject( por );
//
//                        ResponseHeaderOverrides override = new ResponseHeaderOverrides();
//                        override.setContentType( "image/jpeg" );
//
//                        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( "mymarketbucket", picture_path_trimmed);
////                urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600000 ) );  // Added an hour's worth of milliseconds to the current time.
//                        urlRequest.setResponseHeaders( override );
//
//                        URL url = s3Client.generatePresignedUrl( urlRequest );
//                        Log.e("url_final",url.toString());
//                        image_uris.add(url.toString());
////                        image_uris.add(str_file_logo);
//                        // PASS "str_file_logo" AS YOUR IMAGE PARAMETER IN JSON
//                        try {
//                            fOut = new FileOutputStream(file_logo);
//                            bm.compress(Bitmap.CompressFormat.JPEG, 150, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

//            }
            else if (resultCode == Activity.RESULT_CANCELED){

            }

        }
        else if(requestCode==SELECT_FILE){
            if (resultCode == Activity.RESULT_OK){

                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, getActivity());
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
//                image_company_logo.setImageBitmap(bm);
                image = new ImageView(getActivity());
                image.setImageBitmap(bm);
                image.setLayoutParams(new ViewGroup.LayoutParams(150,150));
                image.setMaxHeight(150);
                image.setMaxWidth(150);

                // Adds the view to the layout
                mSelectedImagesContainer.addView(image);

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                Log.e("picturePath", picturePath);
                Log.e("trimmed",picturePath.substring(picturePath.lastIndexOf("/") + 1));
                String picture_path_trimmed=picturePath.substring(picturePath.lastIndexOf("/") + 1);
//                image_uris.add("https://s3-us-west-2.amazonaws.com/mymarketbucket/"+picture_path_trimmed);

                AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( "AKIAJ3YGIVONBRYID3VA", "XDaZKNMTGO+Ap4ICF877oP1MhEQQYV8I/aySbV50" ) );
//				s3Client.createBucket( "mymarketbucket" );
                File filePath = new File(picturePath);
                PutObjectRequest por = new PutObjectRequest( "mymarketbucket",picture_path_trimmed, filePath);
                s3Client.putObject( por );

                ResponseHeaderOverrides override = new ResponseHeaderOverrides();
                override.setContentType( "image/jpeg" );

                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( "mymarketbucket", picture_path_trimmed);
//                urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600000 ) );  // Added an hour's worth of milliseconds to the current time.
                urlRequest.setResponseHeaders( override );

                URL url = s3Client.generatePresignedUrl( urlRequest );
                Log.e("url_final",url.toString());
                image_uris.add(url.toString());


//                Toast.makeText(getActivity(), "picturePath :" + picturePath, Toast.LENGTH_LONG).show();
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                if (bitmap != null) {
                    // encodeTobase64(bitmap);
                    picturePath = getRealPathFromURI(getImageUri(getActivity(), bitmap));
                    Log.e("real_pic_path", picturePath);
                    File file_logo = new File(picturePath);
                    Log.e("file_picture",file_logo.toString());

                    str_file_logo=file_logo.toString();
                    // PASS "file" AS YOUR IMAGE PARAMETER IN JSON
//                new HotelierUploadGalleryImgs(HotelierUploadGallery.this, file, picturePath, auth_token).execute();
//                new HttpAsync(getApplicationContext(), listener, URL, parameters, 1, null).execute();
                }

            }
            else if (resultCode == Activity.RESULT_CANCELED) {

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
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
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
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    RESULT_OK=0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

//    private void showMedia() {
//        // Remove all views before
//        // adding the new ones.
//        mSelectedImagesContainer.removeAllViews();
//        if (image_uris.size() >= 1) {
//            mSelectedImagesContainer.setVisibility(View.VISIBLE);
//        }
//
//        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
//        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
//
//
//        for (Uri uri : image_uris) {
//
//            View imageHolder = LayoutInflater.from(getActivity()).inflate(R.layout.image_item, null);
//            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
//
//            Glide.with(this)
//                    .load(uri.toString())
//                    .fitCenter()
//                    .into(thumbnail);
//
//            mSelectedImagesContainer.addView(imageHolder);
//
//            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
//
//
//        }
//
//    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

    }
}
