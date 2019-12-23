package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Build;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginSignupActivity extends AppCompatActivity  {
    private static final int RC_SIGN_IN = 100;
    private Button btnLogin,btnLogin2,btnSignup;
    ImageView btnFacebook,btnGoole,btnTwitter;
    EditText emailValidate,password;
    TextView mRecoverPasswordtv;
    SignInButton mGoogleloginbtn;
    private String selection;
//  final   Context context;
    ProgressDialog pd;
    private FirebaseAuth mAuth;
    static final int GOOGLE_SIGN = 123;
GoogleSignInClient mGoogleSignInClient;

    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();




    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_signup );
        //getActionBar().hide();

        pd=new ProgressDialog(this);
        pd.setMessage("Logging In..... ");



        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin2=(Button)findViewById(R.id.button3);
        btnSignup=(Button)findViewById(R.id.btnSignup);
//        btnFacebook = (ImageView) findViewById(R.id.imageView);
        mGoogleloginbtn=findViewById(R.id.googleloginbtn);
      //  btnGoole = (ImageView) findViewById(R.id.imageView2);

        emailValidate = (EditText)findViewById(R.id.editText);

        password = (EditText) findViewById(R.id.editText2);
        final String arr[] = getResources().getStringArray(R.array.selection);

        mRecoverPasswordtv = (TextView) findViewById(R.id.recoverpasswordtv);
        //recover password TextviewCLICK
        mRecoverPasswordtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            recreate();
            return;
        }
//        mAuth = FirebaseAuth.getInstance(  );
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
//                .Builder()
//                .requestIdToken( getString( R.string.default_web_client_id ) )
//                .requestEmail()
//                .build();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        mGoogleloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder( LoginSignupActivity.this );
                dialog.setCancelable( false );
                dialog.setTitle( "SignUp as : " );
                dialog.setSingleChoiceItems( R.array.selection, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection = arr[which];
                        if (selection.equals( "Customer" )) {
                            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                            pd.setMessage("Logging In....");
                            pd.show();
                            startActivityForResult( signInIntent, RC_SIGN_IN );


                        }
                        if (selection.equals( "Service Provider" )) {

                            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                            pd.setMessage("Logging In....");
                            pd.show();
                            startActivityForResult( signInIntent, RC_SIGN_IN );

                        }

                    }
                } );
                dialog.show();


            }
        });





//        mGoogleSignInClient = GoogleSignIn.getClient( this,googleSignInOptions );
//        btnGoole.setOnClickListener( v -> SignInGoogle() );
//        if(mAuth.getCurrentUser() !=null){
//            FirebaseUser user = mAuth.getCurrentUser();
//            updateUI( user );
//            Intent intent = new Intent(this,MainActivity.class  );
//            startActivity( intent );
//        }

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
                            Intent intent = new Intent(LoginSignupActivity.this,SignupActivity.class);
                            intent.putExtra( "name", String.valueOf( "Customer" ) );
                            startActivity(intent);

                        }
                        if(selection.equals( "Service Provider" )){
                            Intent intent = new Intent(LoginSignupActivity.this,SignupActivity.class);
                            intent.putExtra( "name", String.valueOf( "ServiceProvider" ) );
                            startActivity(intent);

                        }
                    }
                } );


                final AlertDialog alert = dialog.create();
                alert.show();
//                alert.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, CUSTOM_COLOR));
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                String email=user.getEmail();
                                String uid=user.getUid();
                                HashMap<Object, String> hashMap=new HashMap<>();
                                hashMap.put("Email",email);
                                hashMap.put("Id",uid);
                                hashMap.put("Category",selection);
                                hashMap.put("Name","");

                                //firebase data instance
                                FirebaseDatabase database=FirebaseDatabase.getInstance();

                                //path to store user data named 'Users'
                                DatabaseReference reference=database.getReference("Users");

                                //PUT Data within hashmap in database
                                reference.child(uid).setValue(hashMap);
                            }

                            Intent intent =  new Intent(LoginSignupActivity.this,MainActivity.class);
                          pd.dismiss();
                            startActivity(intent);
                            finish();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginSignupActivity.this, "Login Failed...", Toast.LENGTH_SHORT).show();

                            //  updateUI(null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //GETAND SHOW ERROR
                Toast.makeText(LoginSignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
