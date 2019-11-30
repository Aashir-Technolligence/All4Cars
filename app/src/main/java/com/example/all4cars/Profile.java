package com.example.all4cars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Profile extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView =(ImageView) findViewById(R.id.profileImage);
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
    }
}
