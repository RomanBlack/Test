package ru.romanblack.test.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;

import ru.romanblack.test.BuildConfig;
import ru.romanblack.test.R;
import ru.romanblack.test.util.Consts;
import ru.romanblack.test.util.ObservableUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.subscriptions.CompositeSubscription;

public class ViewPhotoActivity extends BaseActivity {

    private ImageView imageView;

    private Bitmap bitmap;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_photo);

        initializeUi();

        startInitializeData();
    }

    @Override
    protected void onDestroy() {
        compositeSubscription.unsubscribe();

        super.onDestroy();
    }

    private void initializeUi() {
        imageView = (ImageView) findViewById(R.id.image);
    }

    private void startInitializeData() {
        Subscription subscription = Observable
                .defer(new Func0<Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call() {
                        String uriString = getIntent().getStringExtra(Consts.EXTRA_FILEPATH);
                        Uri filepath = Uri.parse(uriString);

                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(filepath)
                            );
                        } catch (FileNotFoundException e) {
                            if (BuildConfig.DEBUG) {
                                Log.d(Consts.DEBUG_TAG, this.getClass().toString(), e);
                            }
                        }

                        return Observable.just(bitmap);
                    }
                })
                .compose(ObservableUtils.applyIoSchedulers())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        setupBitmap();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            Log.d(Consts.DEBUG_TAG, this.getClass().toString(), e);
                        }
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        ViewPhotoActivity.this.bitmap = bitmap;
                    }
                });

        compositeSubscription.add(subscription);
    }

    private void setupBitmap() {
        imageView.setImageBitmap(bitmap);
    }
}
