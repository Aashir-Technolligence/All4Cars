package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ServiceDetail extends AppCompatActivity {

    TextView service , company,location , contact , end , start ,txtNum , txtRating;
    ImageView imgService;
    FirebaseDatabase database;
    DatabaseReference ref;
    String longitude , latitude , phone;
    ImageButton callBtn , directionBtn , reviewBtn;
    RatingBar ratingBar;
    RecyclerView recyclerView;
    ArrayList<Rating_Attr> rating_attrs;
    String serviceId;


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
        ratingBar =  (RatingBar) findViewById(R.id.ratingBar);
        txtRating = (TextView) findViewById(R.id.txtRating);
        txtNum = (TextView) findViewById(R.id.txtTotalRating);
        recyclerView = (RecyclerView) findViewById(R.id.reviewList);
        rating_attrs = new ArrayList<Rating_Attr>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database= FirebaseDatabase.getInstance();
        ref = database.getReference("Services");
        Intent intent = getIntent();


        final String check= intent.getStringExtra("Id");
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
                        serviceId = addService.getId();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        database.getReference("Rating").child(check).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                Double total = 0.0;
                for (DataSnapshot ratingsnapshot : dataSnapshot.getChildren()) {
                    Rating_Attr rating_attr = ratingsnapshot.getValue(Rating_Attr.class);
                    total += rating_attr.getTotal();
                }
                String star = String.valueOf(new DecimalFormat("#.#").format(total/count));
                String str = String.valueOf(total/count);
                ratingBar.setRating(Float.parseFloat(str));
                //ratingBar.setNumStars((int) (total/count));
                txtRating.setText(star);
                txtNum.setText(String.valueOf(count));
                if(total!=0.0){
                database.getReference("Services").child(check).child("rating").setValue(Float.valueOf(star));
                database.getReference("Services").child(check).child("total").setValue(Integer.valueOf((int) count));}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                ShowDialog();

            }
        });
        database.getReference("Rating").child(check).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rating_attrs.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Rating_Attr p = dataSnapshot1.getValue(Rating_Attr.class);
                    rating_attrs.add(p);
                }

                recyclerView.setAdapter(new ReviewListAdapter(rating_attrs , getApplicationContext()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ShowDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView =getLayoutInflater().inflate(R.layout.ratecomment , null);
        final RatingBar ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        final EditText comment = (EditText) mView.findViewById(R.id.edtComment);
        builder.setView(mView);
        builder.setPositiveButton("OK",
        new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String push = FirebaseDatabase.getInstance().getReference().child("Rating").push().getKey();
                Rating_Attr rating_attr = new Rating_Attr();
                rating_attr.setId(push);
                rating_attr.setServiceId(serviceId);
                rating_attr.setComment(comment.getText().toString());
                rating_attr.setTotal(Float.valueOf(ratingBar.getRating()));
                rating_attr.setUserId(currentUser);

                database.getReference().child("Rating").child(serviceId).child(currentUser)
                        .setValue(rating_attr);

                dialog.dismiss();
            }
        }).setNegativeButton("Cancel",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }

        });

        builder.create();
        builder.show();
    }
}
