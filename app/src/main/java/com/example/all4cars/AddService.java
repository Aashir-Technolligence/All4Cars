package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddService extends AppCompatActivity {
    private TextView addressText;
    private EditText companyName , openTime , closeTime;
    private LocationManager locationManager;
    private String provider , latitude , longitude , service , addressString;
    private ImageView image;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private Button addService;
    ProgressBar progressBar;
    Spinner serviceSpinner;
    private StorageReference StorageRef;
    FirebaseDatabase database;
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        addressText = (TextView) findViewById(R.id.txtLocation);
        companyName = (EditText) findViewById(R.id.edtName);
        openTime = (EditText) findViewById(R.id.edtOpenTime);
        closeTime = (EditText) findViewById(R.id.edtCloseTime);
        progressBar = (ProgressBar) findViewById(R.id.addServiceProgress);
        progressBar.setVisibility(View.GONE);
        addService = (Button) findViewById(R.id.btnAddService);
        StorageRef = FirebaseStorage.getInstance().getReference();
        serviceSpinner = findViewById(R.id.spinnerService);
        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);


        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
            StringBuilder builder = new StringBuilder();
            try {
                List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
                int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i=0; i<maxLines; i++) {
                    String addressStr = address.get(0).getAddressLine(i);
                    builder.append(addressStr);
                    builder.append(" ");
                }
                if (address.size() > 0)
                {
                    System.out.println(address.get(0).getLocality());
                    System.out.println(address.get(0).getCountryName());
                    //Toast.makeText(getApplicationContext() , address.get(0).getAddressLine(0) , Toast.LENGTH_LONG).show();
                }
                String finalAddress = builder.toString(); //This is the complete address.

                latitude = (String.valueOf(lat));
                longitude = (String.valueOf(lng));
                Toast.makeText(getApplicationContext() , latitude + " " + longitude ,  Toast.LENGTH_LONG).show();
                addressText.setText(address.get(0).getAddressLine(0)); //This will display the final address.
                addressString = address.get(0).getAddressLine(0);
            } catch (IOException e) {
                // Handle IOException
            } catch (NullPointerException e) {
                // Handle NullPointerException
            }
        } else {
            addressText.setText("Please enable your location");
        }

        image = findViewById(R.id.serviceImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }

        });

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!companyName.getText().toString().isEmpty() && !openTime.getText().toString().isEmpty() && !closeTime.getText().toString().isEmpty() && !filePath.getPath().isEmpty() )
                {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext() , "Inserting Please wait" , Toast.LENGTH_LONG).show();
                    final String push = FirebaseDatabase.getInstance().getReference().child("Services").push().getKey();
                    StorageReference fileReference  = StorageRef.child("images/"+ push);
                    fileReference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                    if(filePath!=null) {
                                        AddServiceAttr addServiceAttr = new AddServiceAttr();
                                        addServiceAttr.setId(push);
                                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!urlTask.isSuccessful());
                                        Uri downloadUrl = urlTask.getResult();
                                        addServiceAttr.setImage_url(downloadUrl.toString());
                                        addServiceAttr.setCompanyName(companyName.getText().toString());
                                        addServiceAttr.setService(service);
                                        addServiceAttr.setCloseTime(closeTime.getText().toString());
                                        addServiceAttr.setOpenTime(openTime.getText().toString());
                                        addServiceAttr.setLocation(addressString);

                                        reference.child(push)
                                                .setValue(addServiceAttr);
                                        Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_LONG).show();

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter all Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getApplicationContext().getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
