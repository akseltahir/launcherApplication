package com.example.launcherapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class FavouriteAppAdapter extends RecyclerView.Adapter<FavouriteAppAdapter.ViewHolder> {

    private final List<AppObject> faveAppsList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
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

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();

            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(faveAppsList.get(pos).getPackageName());
            context.startActivity(launchIntent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 1, 0, "Remove from Favourites");
            menu.add(this.getAdapterPosition(), 2, 1, "App info");
        }
    }


    //adds the code we've written to the target view
    @NonNull
    @Override
    public FavouriteAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_app_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String appLabel = faveAppsList.get(i).getName();
        String appPackage = faveAppsList.get(i).getPackageName();
        Drawable appIcon = faveAppsList.get(i).getImage();
        String appCategory = faveAppsList.get(i).getAppInfo();

        TextView textView = (TextView) viewHolder.faveAppNameTV;
        textView.setText(appLabel);

        ImageView imageView = viewHolder.faveAppIconIV;
        imageView.setImageDrawable(appIcon);

        TextView appUsageTime = (TextView) viewHolder.faveAppUsageTimeTV;
        appUsageTime.setText(appCategory);
    }



    /*
    Various helper functions
     */
    //get the item count for the recyclerview
    @Override
    public int getItemCount() {
        return faveAppsList.size();
    }

    //remove single app from the recyclerview
    public void removeAppFromList(int position) {
        faveAppsList.remove(position);
        notifyDataSetChanged();
    }

    //return packagename of the selected app
    public String getAppPackageName(int position) {
        return faveAppsList.get(position).getPackageName();
    }

    //add into recyclerview
    public void addApp(AppObject app) {
        faveAppsList.add(app);
    }

    //simple constructor
    public FavouriteAppAdapter(Context c) {
        faveAppsList = new ArrayList<>();
    }

    //delete all items from the recyclerview
    public void clear() {
        int size = faveAppsList.size();
        faveAppsList.clear();
        notifyItemRangeRemoved(0, size);
    }
}