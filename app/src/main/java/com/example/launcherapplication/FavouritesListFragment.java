package com.example.launcherapplication;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.launcherapplication.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FavouritesListFragment extends RecyclerView.Adapter<FavouritesListFragment.ViewHolder> {

    private final List<AppObject> faveAppsList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView faveAppNameTV;
        public ImageView faveAppIconIV;
        public TextView faveAppUsageTimeTV;
        public LinearLayout faveAppItemTV;

        public ViewHolder(View itemView) {
            super(itemView);

            faveAppNameTV = (TextView) itemView.findViewById(R.id.faveAppNameTV);
            faveAppIconIV = (ImageView) itemView.findViewById(R.id.faveAppIconIV);
            faveAppUsageTimeTV = (TextView) itemView.findViewById(R.id.faveAppUsageTimeTV);
            faveAppItemTV = (LinearLayout) itemView.findViewById(R.id.faveAppItemTV);
        }
    }


    //adds the code we've written to the target view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_app_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String appLabel = faveAppsList.get(i).getName();
        String appPackage = faveAppsList.get(i).getPackageName();
        Drawable appIcon = faveAppsList.get(i).getImage();
        String appCategory = faveAppsList.get(i).getAppCategory();

        TextView textView = (TextView) viewHolder.faveAppNameTV;
        textView.setText(appLabel);

        ImageView imageView = viewHolder.faveAppIconIV;
        imageView.setImageDrawable(appIcon);

        TextView appUsageTime = (TextView) viewHolder.faveAppUsageTimeTV;
        appUsageTime.setText(appCategory);
    }


    public FavouritesListFragment(List<AppObject> items) {
        faveAppsList = items;
    }

    @Override
    public int getItemCount() {
        return faveAppsList.size();
    }

    public void addApp(AppObject app) {
        faveAppsList.add(app);
    }
}