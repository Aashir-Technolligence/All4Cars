package com.example.all4cars;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class Profile extends AppCompatActivity {
    ImageView imageView;
    TextView name,email;
    String category;
    LinearLayout LayoutAddService,LayoutViewService;
    View view1,view2;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView =(ImageView) findViewById(R.id.profileImage);
        name=(TextView) findViewById(R.id.name);
        email=(TextView) findViewById(R.id.email);
        LayoutAddService=(LinearLayout) findViewById(R.id.LayoutAddService);
        LayoutViewService=(LinearLayout) findViewById(R.id.LayoutViewServices);
        view1=(View) findViewById(R.id.view1);
        view2=(View) findViewById(R.id.view2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View view=getWindow().getDecorView().findViewById(android.R.id.content);
//                if(view.getParent()!=null)
//                    ((ViewGroup)view.getParent()).removeView(view); // <- fix
                ProfileDialog profileDialog =new ProfileDialog();
                profileDialog.show(getSupportFragmentManager(),"Example Dialog" );
            }
        });
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentUser=user.getUid();
        dref.child("Users").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                Picasso.get().load(dataSnapshot.child( "pic" ).getValue().toString()).into(imageView);
                category=dataSnapshot.child("Category").getValue().toString();
                if(category.equals("Customer")){

                    LayoutAddService.setVisibility(View.GONE);
                    LayoutViewService.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        LayoutAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,AddService.class));

            }
        });
        LayoutViewService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this , ViewServices.class);
                i.putExtra("Id" , "SP");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (category.equals( "Customer" )) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( this );
            alertDialogBuilder.setMessage( "Are you sure want to logout?" ).setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity( new Intent( Profile.this, LoginSignupActivity.class ) );
                }
            } ).setNegativeButton( "No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            } ).show();
        } else {
            super.onBackPressed();
        }
    }
}
