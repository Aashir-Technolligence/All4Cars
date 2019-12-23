package com.example.all4cars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText( this, "Heelo"+currentuser, Toast.LENGTH_SHORT ).show();
    }
}
