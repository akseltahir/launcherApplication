package com.example.launcherapplication;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of Items.
 */
public class FavouriteAppFragment extends Fragment {

    private ArrayList<AppObject> appsList;
    private FavouriteAppAdapter faveAdapter;
    private final LauncherApps mLauncherApps = null;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PACKAGE_NAME = "pckName";
    public List<String> favouriteApps = new ArrayList<String>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavouriteAppFragment() {
        //super(R.layout.fragment_item_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        //Set the adapter
        RecyclerView recyclerView = (RecyclerView) view;
        faveAdapter = new FavouriteAppAdapter(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(faveAdapter);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("sharedprefs", sharedPreferences.getAll().toString());
        if (sharedPreferences.getString(SHARED_PREFS,null) != null) {
            loadItemsIntoRecycler();
        }
        return view;
    }


    //load the recyclerview again every time the fragment is resumed.
    // This is to show the new favourite additions from the app drawer without reloading the app entirely.
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("sharedprefs", sharedPreferences.getAll().toString());
        if (sharedPreferences.getString(SHARED_PREFS,null) != null) {
            loadItemsIntoRecycler();
        }
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        Log.d("sharedprefs", sharedPreferences.getAll().toString());
//        if (sharedPreferences.getString(SHARED_PREFS,null) != null) {
//            loadItemsIntoRecycler();
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        Log.d("sharedprefs", sharedPreferences.getAll().toString());
//        if (sharedPreferences.getString(SHARED_PREFS,null) != null) {
//            loadItemsIntoRecycler();
//        }
//    }

    private void loadItemsIntoRecycler() {
        //get packagemanager stuff
        PackageManager pm = getActivity().getPackageManager();
        appsList = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<String> sharedPrefPackages = returnAllItemsInSharedPrefs(); //fetches package name list in shared preferences
        List<ResolveInfo> allInstalledPackages = pm.queryIntentActivities(i, 0); //fetches all packages on device

        faveAdapter.clear();
        //Log.d("list123123", allInstalledPackages.toString());
        for(ResolveInfo ri:allInstalledPackages) {
            //TextView txtvw = getActivity().findViewById(R.id.faveAppsText);
            //Log.d("list123123", ri.resolvePackageName);

            //if the shared preferences list contains the current app, add it to the view.
            if (sharedPrefPackages.contains(ri.activityInfo.packageName)) {
                AppObject app = new AppObject(ri.activityInfo.packageName,
                        ri.loadLabel(pm).toString(),
                        ri.activityInfo.loadIcon(pm), false,
                        getAppUsageTime(ri));
                appsList.add(app);
                faveAdapter.addApp(app);
            }
        }
    }

    //returns the application usage time for the day
    private String getAppUsageTime(ResolveInfo ri) {

        long endTime = System.currentTimeMillis();
        long beginTime = endTime-1000;
        String usageTime="";
        long usageTimeL = 0;


        UsageStatsManager usm = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        //if i understand right, this holds a list of usage stats for all installed packages.
        List<UsageStats> usageStats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,beginTime,endTime);

        //goes through list of usage stats, comparing the package names to that of the parameter variable.
        //when it finds the usage stat of the parameter variable's packageName,
        //adds the value of total usageTime in foreground to the usageTime variable
        for (UsageStats usageStat:usageStats) {
            Log.d("usageTime0", usageStat.getPackageName()+" "+String.valueOf(usageStat.getTotalTimeInForeground()));
            Log.d("riPackageName", ri.activityInfo.packageName);
            if (usageStat.getPackageName().equals(ri.activityInfo.packageName)) {
                usageTimeL = usageStat.getTotalTimeInForeground();
                Log.d("usageTime2", String.valueOf(usageStat.getTotalTimeInForeground()));
            }
        }

        long minutes = (usageTimeL / 1000) / 60;
        long seconds = (usageTimeL / 1000) % 60;
        usageTime = minutes + "m" + seconds + "s";
        //int i = getActivity().getPackageManager().getAppS
        return usageTime;
    }

    private List<String> returnAllItemsInSharedPrefs() {
        List<String> packageNames = new ArrayList<String>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PACKAGE_NAME,"empty");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        packageNames = gson.fromJson(json,type);

        return packageNames;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 1: // add to favourites in homescreen
                removeAppFromFavourites(item);
                return true;

            case 2: // show information about app
                showApplicationInfo();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showApplicationInfo() {
    }

    private void removeAppFromFavourites(MenuItem item) {

        String packageName = appsList.get(item.getGroupId()).getPackageName();

        //remove app package String from shared preferences,
        //then make favourites handler look for an addition to the shared prefs
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(PACKAGE_NAME, "empty");

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        favouriteApps = gson.fromJson(json,type); //puts the apps in sharedpref into the variable
        Log.d("faveApps1", favouriteApps.toString());

        String pck = packageName; //gets the package name selected
        favouriteApps.remove(pck); //adds the selected package name into the variable, which now also houses the previous shared prefs
        Log.d("faveApps2", favouriteApps.toString());
        String json1 = gson.toJson(favouriteApps); //puts the new variable into shared prefs.
        Log.d("faveAppsJSON1", json1.toString());
        editor.putString(PACKAGE_NAME, json1);
        editor.commit();

        faveAdapter.removeAppFromList(item.getGroupId());
        faveAdapter.notifyDataSetChanged();
    }


//    @SuppressLint("StaticFieldLeak")
//    public class FaveAppAdapterThread extends AsyncTask<Void, Void, String> {
//
//        @Override
//        protected String doInBackground(Void... Params) {
//
//            PackageManager pm = this.getActivity().getPackageManager();
//            appsList = new ArrayList<>();
//
//            Intent i = new Intent(Intent.ACTION_MAIN, null);
//            i.addCategory(Intent.CATEGORY_LAUNCHER);
//
//            @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
//            for(ResolveInfo ri:allApps) {
//
//                AppObject app = new AppObject(ri.activityInfo.packageName,
//                        ri.loadLabel(pm).toString(),
//                        ri.activityInfo.loadIcon(pm), false,
//                        getAppCategory(ri));
//                adapter.addApp(app);
//            }
//
//            return "Success";
//        }
//
//        private String getAppCategory(ResolveInfo ri) {
//            // Application categorisation method
//            int categoryCode = -1;   //if exception occurs, the app will be uncategorised
//            try {
//                //fetching the application category from the package manager
//                categoryCode = getPackageManager().getApplicationInfo(ri.activityInfo.packageName, 0).category;
//
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            String categoryName;
//            String[] applicationCategories = {"General","Games","Audio","Videos","Images","Social and Internet","News","Maps","Productivity","Accessibility"};
//            categoryName = applicationCategories[categoryCode+1];
//            return categoryName;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            updateStuff();
//        }
//    }
}


    //    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    // TODO: Customize parameters
//    private int mColumnCount = 1;
//


//
//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static FavouriteAppFragment newInstance(int columnCount) {
//        FavouriteAppFragment fragment = new FavouriteAppFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//    }
//
