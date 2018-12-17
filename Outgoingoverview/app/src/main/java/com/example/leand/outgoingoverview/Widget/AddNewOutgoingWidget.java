package com.example.leand.outgoingoverview.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.leand.outgoingoverview.Activitys.AddNewItemActivity;
import com.example.leand.outgoingoverview.Activitys.AddRepeatedItemsActivity;
import com.example.leand.outgoingoverview.Activitys.MainActivity;
import com.example.leand.outgoingoverview.R;

import java.util.Calendar;

import static com.example.leand.outgoingoverview.Activitys.MainActivity.EXTRA_LONG_DATE;

/**
 * Implementation of App Widget functionality.
 */
public class AddNewOutgoingWidget extends AppWidgetProvider {
Calendar calendar;
    public static final String EXTRA_INT_DIRECT_OPEN_ACTIVITY = "direct Open Activity";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            calendar=Calendar.getInstance();

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.add_new_outgoing_widget);

            //open AddNew
            Intent intentAddNew = new Intent(context, AddNewItemActivity.class);
            intentAddNew.putExtra(MainActivity.EXTRA_LONG_DATE, calendar.getTimeInMillis());
            intentAddNew.setAction("AddNew");
            PendingIntent pendingIntentAddNew = PendingIntent.getActivity(context, 0, intentAddNew, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.button_AddNewOutgoingWidget_AddNew, pendingIntentAddNew);

            //open AddNewRepeated
            Intent intentAddNewRepeated = new Intent(context, AddRepeatedItemsActivity.class);
            intentAddNewRepeated.putExtra(EXTRA_INT_DIRECT_OPEN_ACTIVITY, 2);
            intentAddNewRepeated.setAction("AddNewRepeated");
            PendingIntent pendingIntentAddNewRepeated = PendingIntent.getActivity(context, 1, intentAddNewRepeated, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.button_AddNewOutgoingWidget_AddNewRepeated, pendingIntentAddNewRepeated);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}

