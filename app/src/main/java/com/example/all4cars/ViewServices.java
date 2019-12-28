package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewServices extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    RecyclerView recyclerView;
    ArrayList<AddServiceAttr> addServiceAttrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_services );
        Intent intent = getIntent();
        final String check = intent.getStringExtra( "Id" );
        final String user = intent.getStringExtra( "user" );
        recyclerView = (RecyclerView) findViewById( R.id.recyclerview );
        addServiceAttrs = new ArrayList<AddServiceAttr>();
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        if (check.equals( "SP" )) {
            String user1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference.child( "Services" ).orderByChild( "userID" ).equalTo( user1 ).addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    addServiceAttrs.clear();
                    //profiledata.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        AddServiceAttr p = dataSnapshot1.getValue( AddServiceAttr.class );
                        addServiceAttrs.add( p );
                    }

                    recyclerView.setAdapter( new ViewServiceAdapter( addServiceAttrs, getApplicationContext() ) );


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

        } else {
            databaseReference.child( "Services" ).orderByChild( "service" ).equalTo( check ).addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    addServiceAttrs.clear();
                    //profiledata.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        AddServiceAttr p = dataSnapshot1.getValue( AddServiceAttr.class );
                        addServiceAttrs.add( p );
                    }
                    if (user != null) {
                        if (user.equals( "Skip" ))
                            recyclerView.setAdapter( new ViewServiceAdapter( addServiceAttrs, "Skip", getApplicationContext() ) );
                    }
                    else
                        recyclerView.setAdapter( new ViewServiceAdapter( addServiceAttrs, getApplicationContext() ) );

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
        }

    }
}
