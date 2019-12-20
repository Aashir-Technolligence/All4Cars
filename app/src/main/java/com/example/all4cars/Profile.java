package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    ImageView imageView;
    TextView name,email;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView =(ImageView) findViewById(R.id.profileImage);
        name=(TextView) findViewById(R.id.name);
        email=(TextView) findViewById(R.id.email);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View view=getWindow().getDecorView().findViewById(android.R.id.content);
//                if(view.getParent()!=null)
//                    ((ViewGroup)view.getParent()).removeView(view); // <- fix
                ProfileDialog profileDialog =new ProfileDialog();
                profileDialog.show(getSupportFragmentManager(),"Example Dialog");
            }
        });
        //FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentUser="wAtuSN7fcaczKTKer402YWDCRmt2";//user.getUid();
        dref.child("Users").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                Picasso.get().load(dataSnapshot.child( "pic" ).getValue().toString()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
