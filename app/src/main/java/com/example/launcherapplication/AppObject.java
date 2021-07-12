package com.example.launcherapplication;

import android.graphics.drawable.Drawable;

public class AppObject {
    private String  name,
                    packageName,
                    appInfo; //in the app drawer, this is the app category. in the faves list, this is the app usage time.
    private Drawable image;
    private Boolean isAppInDrawer;

    public AppObject(String packageName, String name, Drawable image, Boolean isAppInDrawer, String appInfo) {
        this.name = name;
        this.image = image;
        this.packageName = packageName;
        this.isAppInDrawer = isAppInDrawer;
        this.appInfo = appInfo;
    }

    public String getPackageName() {
        return packageName;
    }
    public String getName() {
        return name;
    }
    public Drawable getImage() {
        return image;
    }
    public Boolean getIsAppInDrawer() {return isAppInDrawer;}
    public String getAppInfo() {
        return appInfo;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setImage(Drawable image) {
        this.image = image;
    }
    public void setIsAppInDrawer(Boolean appInDrawer) {
        this.isAppInDrawer = appInDrawer;
    }
    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }
}