package ru.romanblack.test;

import android.app.Application;

import ru.romanblack.test.data.database.DatabaseManager;
import ru.romanblack.test.network.Service;
import ru.romanblack.test.util.NetworkUtils;

public class App extends Application {

    private static App instance;

    private Service service;

    private DatabaseManager databaseManager;

    @Override
    public void onCreate() {
        super.onCreate();

        service = NetworkUtils.buildService();

        databaseManager = new DatabaseManager(this);

        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public Service getService() {
        return service;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
