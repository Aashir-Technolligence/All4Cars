package com.example.all4cars;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DirectionOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String longitude , latitude;
    Marker marker;
    private LocationManager locationManager;
    double LocLongitude;
    double LocLatitude;
    MarkerOptions current , destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        latitude = intent.getStringExtra("Latitude");
        longitude = intent.getStringExtra("Longitude");
        destination = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude) , Double.parseDouble(longitude)));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocLatitude = Double.parseDouble(latitude);
        LocLongitude = Double.parseDouble(longitude);

        mMap.addMarker(new MarkerOptions().position(new LatLng(LocLatitude , LocLongitude))).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));


        LatLng loc = new LatLng(LocLatitude, LocLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        CameraPosition camPos = new CameraPosition.Builder()
                .target(loc)
                .zoom(10)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
