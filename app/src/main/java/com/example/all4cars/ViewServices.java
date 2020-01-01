
package com.example.all4cars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);

        recyclerView=(RecyclerView) findViewById(R.id.recyclerview);
        addServiceAttrs = new ArrayList<AddServiceAttr>();
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference.child("Services").orderByChild("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addServiceAttrs.clear();
                //profiledata.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    AddServiceAttr p = dataSnapshot1.getValue(AddServiceAttr.class);
                    addServiceAttrs.add(p);
                }

                recyclerView.setAdapter(new ViewServiceAdapter(addServiceAttrs , getApplicationContext()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
