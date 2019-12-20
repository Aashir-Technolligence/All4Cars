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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginSignupActivity extends AppCompatActivity {
    private Button btnLogin,btnLogin2,btnSignup;
    ImageView btnFacebook,btnGoole,btnTwitter;
    EditText emailValidate,password;
    TextView mRecoverPasswordtv;
    private String selection;
//  final   Context context;
    ProgressDialog pd;
    private FirebaseAuth mAuth;

    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_signup );

        pd=new ProgressDialog(this);
        pd.setMessage("Logging In..... ");

        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin2=(Button)findViewById(R.id.button3);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        btnFacebook = (ImageView) findViewById(R.id.imageView);
        btnGoole = (ImageView) findViewById(R.id.imageView2);

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
