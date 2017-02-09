package com.iused.introduction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.iused.R;
import com.iused.introduction.RegisterActivity;
import com.iused.main.MainActivity;
import com.iused.main.TermsandConditionsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Antto on 06-10-2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private static final int MY_PERMISSIONS_CALL = 104;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    String personName = null;
    String personPhotoUrl = null;
    String email = null;
    LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private TextView txt_terms_conditions;
    Intent intent_direct=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        initlization();
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.donate",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txt_terms_conditions= (TextView) findViewById(R.id.txt_terms_and_conditions);

        intent_direct=getIntent();
//        Log.e("intent_direct",intent_direct.getStringExtra("offer_negotiable"));
//        Log.e("intent_direct1","done");

        txt_terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsandConditionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        initButton();

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            // user has logged in
            LoginManager.getInstance().logOut();

        } else {
            // user has not logged in
//            loginButton.performClick();
        }

    }

    private void initlization() {
        FacebookSdk.sdkInitialize(LoginActivity.this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    private void initButton() {
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        // loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }


//    facebook
//    private FacebookCallback<LoginResult> callback= new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            AccessToken accessToken=loginResult.getAccessToken();
//            Profile profile=Profile.getCurrentProfile();
//
//            if(profile!=null)
//            {
////                tv1.setText(profile.getName());
////                fbpic.setImageURI(profile.getProfilePictureUri(30,30));
//
//                Toast.makeText(getApplicationContext(), profile.getName(), Toast.LENGTH_LONG).show();
//
//            }
////          Intent f=new Intent(getApplicationContext(),fbdetails.class);
////           startActivity(f);
//
//        }
//        @Override
//        public void onCancel() {
//
//        }
//
//        @Override
//        public void onError(FacebookException error) {
//
//        }    };

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
//            accessToken = loginResult.getAccessToken();
            //CAAHbcPcQKT4BAOsTN44cLnyh5BycncKBCcvGaCP06WpzM585h1S9d6ZA3AwHZBDqKNuQbNTdgW6o0WfBIr7lKrdavZBugztwX9KiQGkLnPZAC31wRRsjUSyLG2XIjb1JfkmCZCX2mHZCAKbycDO1SEGzo6ZCVHXqfHtujTPnSOOevc7g08OZAH6wvT8Y5JIetGjmvzQIoxfLZBXPzXypFgdXX
//            Log.e("auth_token", accessToken.getToken());
            Profile profile = Profile.getCurrentProfile();
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                String personName_fb = object.getString("name");
                                String email_fb = object.getString("email");
                                String fb_id=object.getString("id");
                                Log.e("email", personName_fb);
                                Log.e("name", email_fb);
                                String profilePicUrl_fb = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                Log.e("image",profilePicUrl_fb);
//                                String profilePicUrl_fb ="";

                                if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("negotiable")){
                                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                                    intent.putExtra("offer_negotiable","negotiable");
                                    intent.putExtra("name",personName_fb);
                                    intent.putExtra("email",email_fb);
                                    intent.putExtra("photo",profilePicUrl_fb);
                                    intent.putExtra("id",fb_id);
                                    intent.putExtra("Type","Facebook");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("sell_product")){
                                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                                    intent.putExtra("offer_negotiable","sell_product");
                                    intent.putExtra("name",personName_fb);
                                    intent.putExtra("email",email_fb);
                                    intent.putExtra("photo",profilePicUrl_fb);
                                    intent.putExtra("id",fb_id);
                                    intent.putExtra("Type","Facebook");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_product")){
                                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                                    intent.putExtra("offer_negotiable","donate_product");
                                    intent.putExtra("name",personName_fb);
                                    intent.putExtra("email",email_fb);
                                    intent.putExtra("photo",profilePicUrl_fb);
                                    intent.putExtra("id",fb_id);
                                    intent.putExtra("Type","Facebook");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("non-negotiable")){
                                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                                    intent.putExtra("offer_negotiable","non-negotiable");
                                    intent.putExtra("name",personName_fb);
                                    intent.putExtra("email",email_fb);
                                    intent.putExtra("photo",profilePicUrl_fb);
                                    intent.putExtra("id",fb_id);
                                    intent.putExtra("Type","Facebook");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("none")){
                                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                                    intent.putExtra("offer_negotiable","none");
                                    intent.putExtra("name",personName_fb);
                                    intent.putExtra("email",email_fb);
                                    intent.putExtra("photo",profilePicUrl_fb);
                                    intent.putExtra("id",fb_id);
                                    intent.putExtra("Type","Facebook");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_offer")){
                                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                                    intent.putExtra("offer_negotiable","donate_offer");
                                    intent.putExtra("name",personName_fb);
                                    intent.putExtra("email",email_fb);
                                    intent.putExtra("photo",profilePicUrl_fb);
                                    intent.putExtra("id",fb_id);
                                    intent.putExtra("Type","Facebook");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }


//                                getDetails();
//                                String birthday = object.getString("birthday");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // 01/31/1980 format
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();

//            personName = profile.getFirstName();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }

    };


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

//            Log.e(TAG, "display name: " + acct.getDisplayName());

            try {
                personName = acct.getDisplayName();
                personPhotoUrl = acct.getPhotoUrl().toString();
                email = acct.getEmail();
            }catch (Exception e){
                personName = acct.getDisplayName();
                personPhotoUrl = "photo";
                email = acct.getEmail();
            }


//            Log.e(TAG, "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl);

            txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);

            if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("negotiable")){
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.putExtra("name",personName);
                intent.putExtra("offer_negotiable","negotiable");
                intent.putExtra("email",email);
                intent.putExtra("photo",personPhotoUrl);
                intent.putExtra("id","");
                intent.putExtra("Type","Gmail");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("sell_product")){
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.putExtra("name",personName);
                intent.putExtra("offer_negotiable","sell_product");
                intent.putExtra("email",email);
                intent.putExtra("photo",personPhotoUrl);
                intent.putExtra("id","");
                intent.putExtra("Type","Gmail");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_product")){
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.putExtra("name",personName);
                intent.putExtra("offer_negotiable","donate_product");
                intent.putExtra("email",email);
                intent.putExtra("photo",personPhotoUrl);
                intent.putExtra("id","");
                intent.putExtra("Type","Gmail");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("non-negotiable")){
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.putExtra("name",personName);
                intent.putExtra("offer_negotiable","non-negotiable");
                intent.putExtra("email",email);
                intent.putExtra("photo",personPhotoUrl);
                intent.putExtra("id","");
                intent.putExtra("Type","Gmail");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("none")){
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.putExtra("name",personName);
                intent.putExtra("offer_negotiable","none");
                intent.putExtra("email",email);
                intent.putExtra("photo",personPhotoUrl);
                intent.putExtra("id","");
                intent.putExtra("Type","Gmail");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else if(intent_direct.getStringExtra("offer_negotiable").equalsIgnoreCase("donate_offer")){
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                intent.putExtra("name",personName);
                intent.putExtra("offer_negotiable","donate_offer");
                intent.putExtra("email",email);
                intent.putExtra("photo",personPhotoUrl);
                intent.putExtra("id","");
                intent.putExtra("Type","Gmail");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }


            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:

                if (Build.VERSION.SDK_INT >= 23) {
                    //Marshmallow
                    // demo();
                    marshmellowPermission();
                    //  mainTask();
                } else {
                    signIn();
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

                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }

    private void marshmellowPermission() {

        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_CALL);
        } else {
            signIn();
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

            case MY_PERMISSIONS_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signIn();
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
                    Toast.makeText(getApplicationContext(), "Please allow permission login", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if (requestCode == 200 && resultCode == RESULT_OK) {
            finish();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        signOut();
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.VISIBLE);
//            btnSignOut.setVisibility(View.VISIBLE);
//            btnRevokeAccess.setVisibility(View.VISIBLE);
//            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
//            btnSignOut.setVisibility(View.GONE);
//            btnRevokeAccess.setVisibility(View.GONE);
//            llProfileLayout.setVisibility(View.GONE);
        }
    }



}
