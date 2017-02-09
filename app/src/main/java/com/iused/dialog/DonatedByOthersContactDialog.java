package com.iused.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iused.R;
import com.iused.fragments.Dontated_By_Others_Fragment;

/**
 * Created by yash on 21-10-2016.
 */
public class DonatedByOthersContactDialog extends Dialog{

    String str_posted_by_name;
    String str_posted_by_phone;
    private Context context=null;
    private TextView txt_posted_by_name=null;
    private TextView txt_posted_by_contact=null;
    private Button btn_ok=null;
    private ImageView img_call=null;
    private LinearLayout linear_call=null;
    private static final int MY_PERMISSIONS_CALL = 104;

    public DonatedByOthersContactDialog(Context context1, String postedByUserName, String postedByPhone) {
        super(context1);
        this.str_posted_by_name=postedByUserName;
        this.str_posted_by_phone=postedByPhone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_donated_by_contact);

        txt_posted_by_name= (TextView) findViewById(R.id.txt_posted_by_name);
        txt_posted_by_contact= (TextView) findViewById(R.id.txt_posted_by_phone);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        img_call= (ImageView) findViewById(R.id.img_call);
        linear_call= (LinearLayout) findViewById(R.id.linear_call);

        txt_posted_by_name.setText(str_posted_by_name);
        txt_posted_by_contact.setText("+"+str_posted_by_phone);

        linear_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                callIntent.setData(Uri.parse("tel:" + txt_posted_by_contact.getText().toString()));
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
//                }
//                context.startActivity(callIntent);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
