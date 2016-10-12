package ru.romanblack.test.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import ru.romanblack.test.App;
import ru.romanblack.test.network.Service;

public class NetworkUtils {

    public static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Consts.URL_SERVICE)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Service buildService() {
        return buildRetrofit().create(Service.class);
    }

    public static boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
