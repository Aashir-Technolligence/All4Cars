package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONObject;

import java.util.Arrays;


public class LoginSignupActivity extends AppCompatActivity {
    private Button btnLogin,btnLogin2,btnSignup;
    ImageView btnFacebook,btnGoole,btnTwitter;
    EditText emailValidate,password;
    TextView mRecoverPasswordtv;
    private String selection;
//  final   Context context;
    ProgressDialog pd;
    private FirebaseAuth mAuth;
    String fbId = "",fbemail="";
    //Facebook Declaration
    CallbackManager callbackManager;
    LoginManager loginManager;
    ImageView faceBookBtn;


    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_signup );
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        pd=new ProgressDialog(this);
        pd.setMessage("Logging In..... ");

        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin2=(Button)findViewById(R.id.button3);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        btnFacebook = (ImageView) findViewById(R.id.imageView);
        btnGoole = (ImageView) findViewById(R.id.imageView2);
        btnTwitter = (ImageView) findViewById(R.id.imageView3);

        emailValidate = (EditText)findViewById(R.id.editText);

        password = (EditText) findViewById(R.id.editText2);

        mRecoverPasswordtv = (TextView) findViewById(R.id.recoverpasswordtv);
        //recover password TextviewCLICK
        mRecoverPasswordtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL = emailValidate.getText().toString().trim();
                String PASSWORD = password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()){
                    emailValidate.setError("Invalid email");
                    emailValidate.setFocusable(true);
                }else {
                    //startActivity(new Intent(studentlogin_validation.this,students.class));
                    loginuser(EMAIL,PASSWORD);

                }
            }
        });

        final String arr[] = getResources().getStringArray(R.array.selection);

        btnFacebook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                startActivity(browserIntent);
            }
        } );



        faceBookBtn = (ImageView) findViewById(R.id.imageView);
        faceBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceBookLogin();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder( LoginSignupActivity.this );
                dialog.setCancelable( false );
                dialog.setTitle( "SignUp as : " );
                dialog.setSingleChoiceItems( R.array.selection, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection = arr[which];
                        if(selection.equals( "Customer" ))
                        {
                            Toast.makeText( LoginSignupActivity.this, "Customer Selected", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
                dialog.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(LoginSignupActivity.this,SignupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } );

//                dialog.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent i = new Intent( LoginSignupActivity.this, SignupActivity.class );
//                        startActivity( i );
//                    }
//                } );

                final AlertDialog alert = dialog.create();
                alert.show();
//                alert.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, CUSTOM_COLOR));
            }
        });

    }

    private void faceBookLogin() {
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
                if (AccessToken.getCurrentAccessToken() != null) {

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {

                                    if (object != null) {
                                        try {
                                            AppEventsLogger logger = AppEventsLogger.newLogger(LoginSignupActivity.this);
                                            logger.logEvent("Facebook login suceess");

                                            handleFacebookAccessToken(loginResult.getAccessToken());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender, birthday, about");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                //Log.d(TAG, "facebook:onCancel");
            }


            @Override
            public void onError(FacebookException error) {
              //  Log.d(TAG, "facebook:onError", error);
            }
        });
        loginManager.logInWithReadPermissions(LoginSignupActivity.this, Arrays.asList("email", "public_profile"));

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);
        mAuth = FirebaseAuth.getInstance();

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(LoginSignupActivity.this , ViewServices.class));
                            Toast.makeText(getApplicationContext(),"email", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(getApplicationContext(),"email" + user.getEmail() , Toast.LENGTH_LONG).show();

                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"signInWithCredential:failure " +task.getException() , Toast.LENGTH_LONG).show();
                        }


                    }
                });
    }

    private void loginuser(String EMAIL, String PASSWORD) {
        pd.setMessage("Logging In....");
        pd.show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginSignupActivity.this,MainActivity.class));
                            finish();

                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginSignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginSignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailET=new EditText(this);
        emailET.setHint("Email");
        emailET.setInputType( InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailET.setMinEms(16);



        linearLayout.addView(emailET);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //button recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailET.getText().toString().trim();
                beginRecovery(email);
            }
        });
//button cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {

        pd.setMessage("Sending Email....");
        pd.show();
        mAuth = FirebaseAuth.getInstance(  );
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginSignupActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginSignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginSignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}