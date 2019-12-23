package com.example.all4cars;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_LOCATION = 0;
    LocationManager locationManager;
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    private String provider;
    DrawerLayout drawerLayout;
    TextView name,email;
    ImageView imageView;
    String lati, loni;
    Double latitude = 0.0, longitude = 0.0;
    FloatingActionButton floatingActionButton;
    ActionBarDrawerToggle drawerToggle;
    FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawarLayout);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatButton);
        //adding drawar button
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //navbar item click
        NavigationView navigationView=(NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //header click navbar
        View headerview = navigationView.getHeaderView(0);
        imageView=(ImageView) headerview.findViewById(R.id.profile_image);
        name=(TextView) headerview.findViewById(R.id.name);
        email=(TextView) headerview.findViewById(R.id.email);

        //opening map fragment
        SupportMapFragment supportMapFragment =(SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        supportMapFragment.getMapAsync(MainActivity.this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceId=getIntent().getStringExtra("ServiceId");
                if(serviceId==null)
                Snackbar.make(drawerLayout,"Select Service First",Snackbar.LENGTH_LONG).show();
                else{
                    Snackbar.make(drawerLayout,""+serviceId,Snackbar.LENGTH_LONG).show();
                }
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentUser=user.getUid();
        if(currentUser==null) {
            dref.child("Users").child(currentUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name.setText(dataSnapshot.child("Name").getValue().toString());
                    email.setText(dataSnapshot.child("Email").getValue().toString());
                    Picasso.get().load(dataSnapshot.child("pic").getValue().toString()).into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //imageView.setImageResource();

        //profile img click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String skip=getIntent().getStringExtra("ServiceId");
                if(skip.equals("Skip")){
                    Snackbar.make(drawerLayout, "You must have account", Snackbar.LENGTH_LONG).show();

                }
                else {
                    startActivity(new Intent(MainActivity.this, Profile.class));
                }
                }
        });



    }






    @Override
    public void onMapReady(final GoogleMap googleMap) {
        String serviceId=getIntent().getStringExtra("ServiceId");
        if(serviceId.equals("Empty")){

            imageView.setVisibility(View.GONE);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location1) {
                            // Got last known location. In some rare situations this can be null.
                            if (location1 != null) {
                                // Logic to handle location object

                                longitude = location1.getLongitude();
                                latitude = location1.getLatitude();
                                lati = (String.valueOf(latitude));
                                loni = (String.valueOf(longitude));

                                //showing on map
                                LatLng latLng1 = new LatLng(latitude, longitude);
                                googleMap.addMarker(new MarkerOptions().position(latLng1).title("Your location"));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 18), 4000, null);
                            } else {

                                Snackbar.make(drawerLayout, "Please allow location to this app", Snackbar.LENGTH_LONG).show();
                            }


                        }
                    });

        }
        else
        {
            dref.child("Services").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        AddServiceAttr addServiceAttr=ds.getValue(AddServiceAttr.class);
                        LatLng latLng=new LatLng(Double.valueOf(addServiceAttr.getLatitude()), Double.valueOf(addServiceAttr.getLongitude()));
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(addServiceAttr.getId()));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18), 4000, null);

                    }
               }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    //drawer open close click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.Benzinarie: {
                Intent intent=new Intent(this,MainActivity.class);
                intent.putExtra("ServiceId","Asigurare Auto");
                startActivity(intent);
                break;
            }
            case R.id.Decarbonizare: {
                Intent intent=new Intent(this,MainActivity.class);
                intent.putExtra("ServiceId","Chip-Tunning");
                startActivity(intent);
                break;
            }
            case R.id.Diagnoza: {
                Snackbar.make(drawerLayout, "Punctur", Snackbar.LENGTH_LONG).show();
                break;

            }
            case R.id.Inmatriculari: {
                Snackbar.make(drawerLayout, "Punctur", Snackbar.LENGTH_LONG).show();
                break;

            }
        }
        return false;
    }
}
