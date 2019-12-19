package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private Button btnLogin,btnSignup;
    ImageView btnFacebook,btnGoole,btnTwitter;
    EditText txtEmail,txtPassword,txtReenterPassword;
    ProgressDialog progressDialog;
    TextView mHaveAccountTv;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.txtSignUp);
        txtEmail =(EditText)findViewById( R.id.editText ) ;
        txtPassword = (EditText)findViewById( R.id.editText2 ) ;
        txtReenterPassword = (EditText)findViewById( R.id.editText3 ) ;
        mHaveAccountTv=findViewById(R.id.have_accounttv);
        btnFacebook = (ImageView) findViewById(R.id.imageView);
        btnGoole = (ImageView) findViewById(R.id.imageView2);
        btnTwitter = (ImageView) findViewById(R.id.imageView3);
        progressDialog=new ProgressDialog(this);


        final String arr[] = getResources().getStringArray(R.array.selection);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txtEmail.getText().toString().trim();
                String password=txtPassword.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    txtEmail.setError("Invalid Email");
                    txtEmail.setFocusable(true);

                }else if (password.length()<6){
                    txtPassword.setError("Pasword Length Must Be grester than 6 characters");
                    txtPassword.setFocusable(true);

                }else {
                    progressDialog.setMessage("Registering ....");
                    registeruser(email,password);
                }

            }
        });

        btnFacebook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                startActivity(browserIntent);
            }
        } );

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this,LoginSignupActivity.class);
                startActivity(intent);
            }
        });


    }

    private void registeruser(final String email, String password) {
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
                            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase database=FirebaseDatabase.getInstance();

                            DatabaseReference reference=database.getReference("Users");

                            reference.child(uid).child( "Email" ).setValue(Email);
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
