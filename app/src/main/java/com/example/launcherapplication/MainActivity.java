package com.example.launcherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.launcherapplication.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Some helper methods to get the application started
        getSupportActionBar().hide();   //hide action bar for fullscreen experience
        dateViewInstantiation();        //run calendar for homescreen
    }

    //Opens the drawer, currently an intents test
    public void openDrawerClick(View view) {
        //intent test, delete later
        Intent intent = new Intent(this, AppDrawer.class);
        //fetches text from clocktext
        TextView clocktext = (TextView) findViewById(R.id.textClock);
        String message = clocktext.getText().toString();
        //puts text into intent
        intent.putExtra(EXTRA_MESSAGE, message);

        //starts activity
        startActivity(intent);
    }


    //opens the launcher options activity
    public void optionsButtonClick(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        //fetches text from clocktext
        TextView clocktext = (TextView) findViewById(R.id.textClock);
        String message = clocktext.getText().toString();
        //puts text into intent
        intent.putExtra(EXTRA_MESSAGE, message);

        //starts activity
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


}