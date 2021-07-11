package com.example.launcherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppDrawer extends AppCompatActivity {

    private ArrayList<AppObject> appsList;
    private AppAdapter adapter;
    private final LauncherApps mLauncherApps = null;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PACKAGE_NAME = "pckName";
    public List<String> favouriteApps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        getSupportActionBar().hide();   //hide action bar for fullscreen experience

        RecyclerView recyclerView = findViewById(R.id.appsList);

        adapter = new AppAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //RUN IF YOU'VE REINSTALLED THE APP AND THE SHAREDPREFS ARE GONE. SEE YOUR VOICE NOTE ON YOUR PHONE TO LEARN WHY.
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(PACKAGE_NAME, "empty");
//
// //        Type type = new TypeToken<ArrayList<String>>() {}.getType();
// //        favouriteApps = gson.fromJson(json,type); //puts the apps in sharedpref into the variable
//
//        favouriteApps.add("object 1"); //adds the selected package name into the variable, which now also houses the previous shared prefs
//        favouriteApps.add("object 2");
//        String json1 = gson.toJson(favouriteApps); //puts the new variable into shared prefs.
//
//        editor.putString(PACKAGE_NAME, json1);
//        editor.commit();



        //this is an async task that loads the contents of the appadater the moment you click the appdrawer button, instead of doing it inside of the appadapter itself.
        new AppAdapterThread().execute();
    }

    //delete later
    public void seeSharedPrefs(View view) {
        TextView txt = findViewById(R.id.sharedprefTextView);
        SharedPreferences pref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefeditor = pref.edit();
        prefeditor.putString(SHARED_PREFS, "empty");
        //pref.edit().remove(SHARED_PREFS);
        prefeditor.apply();

        String prefs = pref.getAll().toString();
        Log.d("sharedprefs", prefs);
        Log.d("faveapps", favouriteApps.toString());
    }


    /**
     * Loads the app drawer asynchronously. Currently it doesn't matter because the appdrawer view opens at the same time as the appadapter.
     * Will be useful if I put the entire appdrawer activity in a viewpage fragment that loads with the homescreen though.
     * Then the appadapter would load before the user even looks at it
     */
    @SuppressLint("StaticFieldLeak")
    public class AppAdapterThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... Params) {

            PackageManager pm = getPackageManager();
            appsList = new ArrayList<>();

            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
            for(ResolveInfo ri:allApps) {

                AppObject app = new AppObject(ri.activityInfo.packageName,
                        ri.loadLabel(pm).toString(),
                        ri.activityInfo.loadIcon(pm), false,
                        getAppCategory(ri));
                appsList.add(app);
                adapter.addApp(app);
            }

            return "Success";
        }

        private String getAppCategory(ResolveInfo ri) {
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
            return categoryName;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            updateStuff();
        }
    }




    /**
     * Context menu selection method.
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 1: // add to favourites in homescreen
                addAppToFavourites(item.getGroupId());
                return true;

            case 2: // show information about app
                showApplicationInfo();
                return true;

            case 3: // remove application from app drawer. Currently working only with one app instance.
                uninstallApp(item);
                return true;
        }
        return super.onContextItemSelected(item);
    }



    /**
     * This method adds the selected app to favourites if
     *      1. the app is installed
     *      2. the app is not already in favourites
     *      The ifs are obviously not implemented yet. I've got not clue how to do them
     * @param position
     */
    private void addAppToFavourites(int position) {

        if(checkIfFavouritesExist()) { //runs if there are apps in shared prefs
            if (checkIfAppNotInList(position)) { //runs if the app is not in the list already
                //add app package String to shared preferences,
                //then make favourites handler look for an addition to the shared prefs
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Gson gson = new Gson();
                String json = sharedPreferences.getString(PACKAGE_NAME, "empty");

                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                favouriteApps = gson.fromJson(json,type); //puts the apps in sharedpref into the variable

                String pck = adapter.getAppPackageName(position); //gets the package name selected
                favouriteApps.add(pck); //adds the selected package name into the variable, which now also houses the previous shared prefs

                String json1 = gson.toJson(favouriteApps); //puts the new variable into shared prefs.

                editor.putString(PACKAGE_NAME, json1);
                editor.apply();

                displayMessage("Added to Favourites");
            }
        } else displayMessage("App is already in favourites");

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

            }
        };
    }



    private boolean checkIfAppNotInList(int position) {
        return true;
    }

    //returns true if favourites exist
    private boolean checkIfFavouritesExist() {
        SharedPreferences pref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = pref.getString(PACKAGE_NAME, "empty");

        Log.d("list123123", id);
        if (id.isEmpty()) return false;
        else return true;
    }


    /**
     *      This method is supposed to open up the system settings to the page that shows information about the selected application.
     *      Currently not working because I don't know how to get it to open for the selected application.
     *      There must be a way to pass the packagename from the recyclerview to the context menu to this method.
     *      Find it and see if that's why it isn't working
     */
    private void showApplicationInfo() {

//        final Intent i = new Intent();
//        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.setData(Uri.parse("package:" + getApplication().getPackageName()));
//
//        getApplication().startActivity(i);

//      //alternative, doesn't work either.
//        ComponentName componentName = null;
//        if (info instanceof AppInfo) {
//            componentName = ((AppInfo) info).componentName;
//        } else if (info instanceof WorkspaceItemInfo) {
//            componentName = info.getTargetComponent();
//        } else if (info instanceof PendingAddItemInfo) {
//            componentName = ((PendingAddItemInfo) info).componentName;
//        } else if (info instanceof LauncherAppWidgetInfo) {
//            componentName = ((LauncherAppWidgetInfo) info).providerName;
//        }
//        if (componentName != null) {
//            try {
//                mLauncherApps.startAppDetailsActivity(componentName, info.user, sourceBounds, opts);
//            } catch (SecurityException | ActivityNotFoundException e) {
//                Toast.makeText(this, "activity not found", Toast.LENGTH_SHORT).show();
//                //Log.e(TAG, "Unable to launch settings", e);
//            }
//        }
    }




    private void uninstallApp(MenuItem item) {

        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:"+appsList.get(item.getGroupId()).getPackageName()));
        startActivity(intent);

        //the idea here is that i load a new list of apps, and if they're a different number, it'd run uninstallApp.
        //for some reason this works all of the time though. :/
        PackageManager pm = getPackageManager();
        appsList = new ArrayList<>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        if (allApps.size() != appsList.size()) {
            adapter.removeAppFromList(item.getGroupId());
        }

        //another attempt at making the dataset change, but it doesn't work either.
        adapter.notifyDataSetChanged();
    }



    // shows a little message for every context menu selection action.
    private void displayMessage(String message) {
        Snackbar.make(findViewById(R.id.app_drawer_item), message, Snackbar.LENGTH_SHORT).show();
    }

    // idk what it does, but
    //the app crashes if i get rid of it
    public void updateStuff() {
        adapter.notifyItemChanged(adapter.getItemCount()-1);
    }

    //overrides the backpress, aka the activity doesn't deload when it's not in view.
    // makes it so that we dont load a new recycler view every time we go to the app drawer
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
