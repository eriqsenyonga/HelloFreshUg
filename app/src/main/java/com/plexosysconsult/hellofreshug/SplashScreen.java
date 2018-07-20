package com.plexosysconsult.hellofreshug;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {


    String jsonFileFruits = "fruits.json";
    String jsonFileVegetables = "vegetables.json";
    String jsonFileHerbs = "spices.json";
    String jsonFileSeaFood = "seafood.json";
    String jsonFileRedWine = "redwine.json";
    String jsonFileWhiteWine = "whitewine.json";

    UsefulFunctions usefulFunctions;

    Typeface beautifulTypeFace;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView) findViewById(R.id.tv_text);

        usefulFunctions = new UsefulFunctions(SplashScreen.this);

        usefulFunctions.deleteFile(jsonFileFruits);
        usefulFunctions.deleteFile(jsonFileVegetables);
        usefulFunctions.deleteFile(jsonFileHerbs);
        usefulFunctions.deleteFile(jsonFileSeaFood);
        usefulFunctions.deleteFile(jsonFileRedWine);
        usefulFunctions.deleteFile(jsonFileWhiteWine);


        beautifulTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Beautiful-People.ttf");
        textView.setTypeface(beautifulTypeFace);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                goToMainActivity();


            }
        }, 2000);


    }

    private void goToMainActivity() {

        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
        i.putExtra("beginning", 1);
        startActivity(i);
        finish();
    }

}
