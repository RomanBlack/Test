package ru.romanblack.test.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ObservableUtils {

    private static Observable.Transformer<Object, Object> schedulersIoTransformer;

    static {
        schedulersIoTransformer = new Observable.Transformer<Object, Object>() {

            @Override
            public Observable<Object> call(Observable<Object> objectObservable) {
                return objectObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static Observable.Transformer applyIoSchedulers() {
        return schedulersIoTransformer;
    }
}
