package com.example.launcherapplication.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.launcherapplication.R;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class YearProgressionWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.year_progression_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            updateWidgetInstance(context,appWidgetManager,"",appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    public static double calculatePercentage(int obtained, double total) {
        return obtained * 100 / total;
    }

    // action = the String that you get from your intent in the onRecive() method
    // id = the id of the appWidget instance that you want to update
    // you can get the id in the onReceive() method like this:
    // int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    private static void updateWidgetInstance(Context context, AppWidgetManager manager, String action, int id) {
        RemoteViews remoteViews = updateCurrentWidget(context, id);
        remoteViews = setIntents(remoteViews, context, id);
        manager.updateAppWidget(id, remoteViews);
    }

    private static RemoteViews updateCurrentWidget(Context context, int appWidgetId) {
        RemoteViews remoteViews;

        Date date;
        LocalDate localDate;

        date = new Date();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int yearProgress = localDate.getDayOfYear();
        int monthProgress = localDate.getDayOfMonth();
        int weekProgress = localDate.getDayOfWeek().getValue();

        Log.d("progressbars1", yearProgress + " " + monthProgress+" " + weekProgress);

        double yearPercent = calculatePercentage(yearProgress,365);
        double monthPercent = calculatePercentage(monthProgress,localDate.getMonth().maxLength());
        double weekPercent = calculatePercentage(weekProgress,7);

        Log.d("progressbars2", yearPercent + " " + monthPercent+" " + weekPercent);

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.year_progression_widget);

        remoteViews.setProgressBar(R.id.year_Progress_Bar,100, ((int) yearPercent),false);
        remoteViews.setProgressBar(R.id.month_Progress_Bar,100, ((int) monthPercent),false);
        remoteViews.setProgressBar(R.id.week_Progress_Bar,100, ((int) weekPercent),false);

        Log.d("progressbars3", ((int) yearPercent) + " " + ((int) monthPercent)+" " + ((int)weekPercent));

        return remoteViews;
    }

    private static RemoteViews setIntents(RemoteViews rm, Context context, int appWidgetId) {

        Intent click = new Intent(context, YearProgressionWidget.class);
        click.setAction("[name of the action]");
        click.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, appWidgetId, click, PendingIntent.FLAG_UPDATE_CURRENT);
        rm.setOnClickPendingIntent(R.id.button1, pendingIntent);
        return rm;
    }


}