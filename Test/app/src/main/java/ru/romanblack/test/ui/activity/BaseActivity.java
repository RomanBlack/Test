package ru.romanblack.test.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private final static String PREF_LOCALE = "locale";

    private Locale currentLocale;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        String locale = getLocale();

        setLocale(locale);
    }

    protected void setLocale(String lang) {
        currentLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = currentLocale;
        res.updateConfiguration(conf, dm);

        saveLocale(lang);
    }

    private String getLocale() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString(PREF_LOCALE, getResources().getConfiguration().locale.toString());
    }

    private void saveLocale(String locale) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        sp.edit()
                .putString(PREF_LOCALE, locale)
                .commit();
    }
}
