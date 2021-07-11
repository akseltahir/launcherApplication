package com.example.launcherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeScreenActivity extends AppCompatActivity {

    public static final String PACKAGE_NAME = "pckName";
    public static final String EXTRA_MESSAGE = "com.example.launcherapplication.MESSAGE";
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_view);

        //Some helper methods to get the application started
        getSupportActionBar().hide();   //hide action bar for fullscreen experience
        dateViewInstantiation();        //run calendar for homescreen
    }

    //Opens the drawer, currently an intents test
    public void openDrawerClick(View view) {
        //intent test, delete later
        Intent intent = new Intent(this, AppDrawer.class);
        startActivity(intent);
    }


    //opens the launcher options activity
    public void optionsButtonClick(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    /*
    Opens up the default phone dialing application
     */
    public void phoneAppButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        //intent.setData(Uri.parse("tel:"));
        startActivity(intent);
    }



    /*
    Simple method to fetch the calendar data for the homescreen visualisation
     */
    private void dateViewInstantiation() {
        TextView dateTimeDisplay = (TextView) findViewById(R.id.dateView);
        Calendar calendar;
        String date;

        calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, EEE");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);
    }


    public void button1Click(View view) {
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        String id = pref.getString(PACKAGE_NAME, "empty");

        Button btn = findViewById(R.id.button1);
        btn.setText(id);
    }
}