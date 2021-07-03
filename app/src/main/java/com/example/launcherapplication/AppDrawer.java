package com.example.launcherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AppDrawer extends AppCompatActivity {

    private ArrayList<AppObject> appsList;
    private AppAdapter adapter;
    private final LauncherApps mLauncherApps = null;

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

    /**
     * Loads the app drawer asynchronously
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
                displayMessage("Added to Favourites");
                return true;

            case 2: // show information about app
                showApplicationInfo();
                return true;

            case 3: // uninstall application
                displayMessage("Uninstalled application");

                //this isn't working atm.
                //uninstallApp("name");
                return true;

            // delete this later, it's supposed to be an unreachable message
            default:
                displayMessage("You should not be seeing this message");
        }

        return super.onContextItemSelected(item);
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

    //not working currently. Need to figure out how to take packageName info from the selected recyclerArray item and pass it onto this method.
    //Also what is ClassName?
    private void uninstallApp(String packageName) {
        Intent detailsIntent = new Intent();
        detailsIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");

        //ApiLevel greater than or equal to 8
        detailsIntent.putExtra("pkg", "Appliction_Package_NAme");

        startActivity(detailsIntent);
    }

    // shows a little message for every context menu selection action.
    private void displayMessage(String message) {
        Snackbar.make(findViewById(R.id.app_drawer_item), message, Snackbar.LENGTH_SHORT).show();
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
