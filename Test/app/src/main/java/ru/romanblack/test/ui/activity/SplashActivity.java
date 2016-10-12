package ru.romanblack.test.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ru.romanblack.test.BuildConfig;
import ru.romanblack.test.R;
import ru.romanblack.test.util.Consts;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SplashActivity extends BaseActivity {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initializeUi();

        startSplash();
    }

    @Override
    protected void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }

        super.onDestroy();
    }

    private void initializeUi() {

    }

    private void startSplash() {
        Observable splashObservable = Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });

        Observable delayObservable = Observable.just(1).delay(2, TimeUnit.SECONDS);

        Observable delayedSplash = Observable.merge(splashObservable, delayObservable);

        delayedSplash.subscribeOn(Schedulers.io());
        delayedSplash.observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = delayedSplash.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                startMain();
            }

            @Override
            public void onError(Throwable e) {
                if (BuildConfig.DEBUG) {
                    Log.e(Consts.DEBUG_TAG, this.getClass().toString(), e);
                }
            }

            @Override
            public void onNext(Object o) {

            }
        });

        compositeSubscription.add(subscription);
    }

    private void startMain() {
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);

        finish();
    }
}
