package com.example.all4cars;

import android.Manifest;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
    FloatingActionButton floatingActionButton;
    ActionBarDrawerToggle drawerToggle;
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
                Snackbar.make(drawerLayout,"Floating button click",Snackbar.LENGTH_LONG).show();
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentUser=user.getUid();
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
        //imageView.setImageResource();

        //profile img click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Profile.class));
            }
        });



    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        //GetLocation();
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location!=null){
            double lat= location.getLatitude();
            double lon=location.getLongitude();

            //showing on map
            LatLng latLng = new LatLng(lat, lon);
            Toast.makeText(this, " "+lat+" "+lon, Toast.LENGTH_LONG).show();
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18),4000,null);
        }
        else{

            Snackbar.make(drawerLayout, "Please allow location to this app", Snackbar.LENGTH_LONG).show();
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

    private double GetLocation(){
        //Location locationNetwork=locationManager.getLastKnownLocation(NETW)
        return 2.2;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.Benzinarie: {
                Snackbar.make(drawerLayout, "Car Wash", Snackbar.LENGTH_LONG).show();
                break;
            }
            case R.id.Decarbonizare: {
                Snackbar.make(drawerLayout, "Tyre Shop", Snackbar.LENGTH_LONG).show();
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
