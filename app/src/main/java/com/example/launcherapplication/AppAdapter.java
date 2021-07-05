package com.example.launcherapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    public List<AppObject> appsList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        public TextView appNameTV;
        public ImageView appIconIV;
        public TextView appCategoryTV;
        public LinearLayout appDrawerItemLL;

        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            appNameTV = (TextView) itemView.findViewById(R.id.applicationNameTextView);
            appIconIV = (ImageView) itemView.findViewById(R.id.applicationIconImageView);
            appCategoryTV = (TextView) itemView.findViewById(R.id.appCategoryTextView);
            appDrawerItemLL = (LinearLayout) itemView.findViewById(R.id.app_drawer_item);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick (View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();

            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).getPackageName());
            context.startActivity(launchIntent);
            //Toast.makeText(v.getContext(), appsList.get(pos).getName(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            View viewthing = v;
            menu.add(this.getAdapterPosition(), 1, 0, "Add to Favourites");
            menu.add(this.getAdapterPosition(), 2, 1, "App info");
            menu.add(this.getAdapterPosition(), 3, 2, "Uninstall app");
        }
    }


    @NonNull
    @Override
    public AppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.app_drawer_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(AppAdapter.ViewHolder viewHolder, int i) {

        //Here we use the information in the list we created to define the views

        String appLabel = appsList.get(i).getName();
        String appPackage = appsList.get(i).getPackageName();
        Drawable appIcon = appsList.get(i).getImage();
        String appCategory = appsList.get(i).getAppCategory();

        TextView textView = (TextView) viewHolder.appNameTV;
        textView.setText(appLabel);

        ImageView imageView = viewHolder.appIconIV;
        imageView.setImageDrawable(appIcon);

        TextView appcat = (TextView) viewHolder.appCategoryTV;
        appcat.setText(appCategory);
    }

    public void uninstallApp(int position) {
        appsList.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        //This method needs to be overridden so that Androids knows how many items
        //will be making it into the list
        return appsList.size();
    }

    public void addApp(AppObject app) {
        appsList.add(app);
    }

    public AppAdapter(Context c) {
        appsList = new ArrayList<>();

    }
}