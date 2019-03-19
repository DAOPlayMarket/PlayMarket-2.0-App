package com.blockchain.store.playmarket.utilities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;

import com.blockchain.store.playmarket.Application;

import java.util.ArrayList;
import java.util.Locale;

public class LocaleUtils {

    private static Locale sLocale;
    private static ArrayList<AppCompatActivity> activities = new ArrayList<>();
    private static ArrayList<AppCompatActivity> onfrontActivities = new ArrayList<>();


    public static Locale getsLocale() {
        return sLocale;
    }

    public static void addActivity(AppCompatActivity activity) {
        activities.add(activity);
    }

    public static void removeActivity(AppCompatActivity activity) {
        activities.remove(activity);
    }

    public static void addOnfrontActivity(AppCompatActivity activity) {
        onfrontActivities.add(activity);
    }

    public static void removeOnfrontActivity(AppCompatActivity activity) {
        onfrontActivities.remove(activity);
    }

    public static ArrayList<AppCompatActivity> getActivities() {
        return activities;
    }

    public static ArrayList<AppCompatActivity> getOnfrontActivities() {
        return onfrontActivities;
    }

    public static void refreshActivies() {
        for (AppCompatActivity activity : activities) {
            activity.recreate();
        }
    }

    public static void setLocale(Locale locale) {
        sLocale = locale;
        if (sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        Configuration configuration = new Configuration();
        configuration.setLocale(sLocale);
        wrapper.applyOverrideConfiguration(configuration);
    }

    public static void updateConfig(Application app, Configuration configuration) {
        //Wrapping the configuration to avoid Activity endless loop
        Configuration config = new Configuration(configuration);
        // We must use the now-deprecated config.locale and res.updateConfiguration here,
        // because the replacements aren't available till API level 24 and 17 respectively.
        config.locale = sLocale;
        Resources res = app.getBaseContext().getResources();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}