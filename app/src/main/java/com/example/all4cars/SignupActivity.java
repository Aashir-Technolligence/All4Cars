package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private Button btnLogin,btnSignup;
    ImageView btnFacebook,btnGoole,btnTwitter;
    EditText txtName,txtEmail,txtPassword,txtReenterPassword;
    ProgressDialog progressDialog;
    String category;
    private String selection;

    SignInButton mGoogleloginbtn;
    TextView mHaveAccountTv;
    ProgressDialog pd;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );
        getActionBar().hide();

        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.txtSignUp);
        txtEmail =(EditText)findViewById( R.id.editTextEmail ) ;
        txtPassword = (EditText)findViewById( R.id.editTextPassword ) ;
        txtName= (EditText)findViewById( R.id.editTextName) ;
        mGoogleloginbtn=findViewById(R.id.googleloginbtn);
        final String arr[] = getResources().getStringArray(R.array.selection);



        txtReenterPassword = (EditText)findViewById( R.id.editRePassword ) ;
        mHaveAccountTv=findViewById(R.id.have_accounttv);
        progressDialog=new ProgressDialog(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Logging In..... ");



        Intent in = getIntent();
        category = in.getStringExtra( "name" );


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        mGoogleloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder( SignupActivity.this );
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


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txtEmail.getText().toString().trim();
                String password=txtPassword.getText().toString().trim();
                String name = txtName.getText().toString();

                if (name==null || name.equals( "" ))
                {
                   txtName.setError( "Please Fill Name" );
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    txtEmail.setError("Invalid Email");
                    txtEmail.setFocusable(true);

                }else if (password.length()<6){
                    txtPassword.setError("Pasword Length Must Be grester than 6 characters");
                    txtPassword.setFocusable(true);

                }else {
                    progressDialog.setMessage("Registering ....");
                    registeruser(email,password,name);
                }

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this,LoginSignupActivity.class);
                startActivity(intent);
            }
        });


    }

    private void registeruser(final String email, String password, final String name) {
        progressDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //progressDialog.dismiss();
                            //FirebaseUser user = mAuth.getCurrentUser();

                            String Email=email;
                            String Name = name;
                            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase database=FirebaseDatabase.getInstance();

                            DatabaseReference reference=database.getReference("Users");

                            reference.child(uid).child( "Name" ).setValue(Name);
                            reference.child(uid).child( "Email" ).setValue(Email);
                            reference.child(uid).child( "Category" ).setValue(category);
                            reference.child(uid).child( "Id" ).setValue(uid);


                            Toast.makeText(SignupActivity.this, "Registered....\n"+Email,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoginSignupActivity.class));
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
