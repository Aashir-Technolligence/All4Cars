package com.example.all4cars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashScreenForCarServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen_for_car_services );
        getActionBar().hide();


        ImageView imageView = findViewById( R.id.imagelogo );
        Animation animation = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fade );
        imageView.startAnimation( animation );

        Thread timer = new Thread(  ) {
            @Override
            public void run() {
                try {
                    sleep( 5000 );

                    Intent intent = new Intent(getApplicationContext(), LoginSignupActivity.class );
                    startActivity( intent );
                    finish();
                    super.run();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        timer.start();
    }
}
