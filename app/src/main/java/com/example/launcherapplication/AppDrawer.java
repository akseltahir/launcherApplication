package com.example.launcherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AppDrawer extends AppCompatActivity {

    ArrayList<AppObject> appsList;
    AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        getSupportActionBar().hide();   //hide action bar for fullscreen experience

        RecyclerView recyclerView = findViewById(R.id.appsList);

        adapter = new AppAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //this is an async task that loads the contents of the appadater the moment you click the appdrawer button, instead of doing it inside of the appadapter itself.
        new AppAdapterThread().execute();

        // notify the adapter that the data has changed
        //recyclerView.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    public class AppAdapterThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... Params) {

            PackageManager pm = getPackageManager();
            appsList = new ArrayList<>();

            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
            for(ResolveInfo ri:allApps) {


                // Application categorisation method
                int categoryCode = -1;   //if exception occurs, the app will be uncategorised
                try {
                    //fetching the application category from the package manager
                    categoryCode = getPackageManager().getApplicationInfo(ri.activityInfo.packageName, 0).category;

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String categoryName;
                String[] applicationCategories = {"General","Games","Audio","Videos","Images","Social and Internet","News","Maps","Productivity","Accessibility"};
                categoryName = applicationCategories[categoryCode+1];


                AppObject app = new AppObject(ri.activityInfo.packageName, ri.loadLabel(pm).toString(), ri.activityInfo.loadIcon(pm), false, categoryName);
                adapter.addApp(app);
            }

            return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            updateStuff();
        }
    }

    public void updateStuff() {
        // Dynamically add app list to recycler view (reflects newly added/deleted apps immediately)
        adapter.notifyItemChanged(adapter.getItemCount()-1);
    }

    //overrides the backpress, aka the activity doesn't deload when it's not in view.
    // makes it so that we dont load a new recycler view every time we go to the app drawer
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


