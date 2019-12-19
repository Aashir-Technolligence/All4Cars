package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ServiceDetail extends AppCompatActivity {

    TextView service , company,location , contact , end , start;
    ImageView imgService;
    FirebaseDatabase database;
    DatabaseReference ref;
    String longitude , latitude , phone;
    ImageButton callBtn , directionBtn , reviewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        service = (TextView) findViewById(R.id.txtService);
        company = (TextView) findViewById(R.id.txtCompanyName);
        location = (TextView) findViewById(R.id.txtLocation);
        contact = (TextView) findViewById(R.id.txtCall);
        end = (TextView) findViewById(R.id.txtClose);
        start = (TextView) findViewById(R.id.txtOpen);
        imgService = (ImageView) findViewById(R.id.imgService);
        callBtn = (ImageButton) findViewById(R.id.btnCall);
        directionBtn = (ImageButton) findViewById(R.id.btnDirection);
        reviewBtn = (ImageButton) findViewById(R.id.btnReview);

        database= FirebaseDatabase.getInstance();
        ref = database.getReference("Services");
        Intent intent = getIntent();
        String check= intent.getStringExtra("Id");
        ref.child(check).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    AddServiceAttr addService = dataSnapshot.getValue(AddServiceAttr.class);
                    if(addService != null) {
                        service.setText(addService.getService());
                        company.setText(addService.getCompanyName());
                        location.setText(addService.getLocation());
                        contact.setText(addService.getPhone());
                        phone = addService.getPhone();
                        end.setText(addService.getCloseTime());
                        start.setText(addService.getOpenTime());
                        Picasso.get().load(addService.getImage_url()).into(imgService);
                        longitude = addService.getLongitude();
                        latitude = addService.getLatitude();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDetail.this , DirectionOnMap.class);
                intent.putExtra("Latitude" , latitude);
                intent.putExtra("Longitude" , longitude);
                startActivity(intent);
            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
