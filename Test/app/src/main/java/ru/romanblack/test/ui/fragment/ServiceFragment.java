package ru.romanblack.test.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ru.romanblack.test.App;
import ru.romanblack.test.BuildConfig;
import ru.romanblack.test.R;
import ru.romanblack.test.data.entities.Quote;
import ru.romanblack.test.network.response.QuotesResponse;
import ru.romanblack.test.ui.adapter.QuotesAdapter;
import ru.romanblack.test.util.Consts;
import ru.romanblack.test.util.NetworkUtils;
import ru.romanblack.test.util.ObservableUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ServiceFragment extends MainActivityFragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView listView;

    private boolean loading = false;
    private boolean loaded = false;

    private List<Quote> quotes;

    private QuotesAdapter adapter;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUi(view);

        startLoad();
    }

    private void initializeUi(View view) {
        listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (loaded) {
            listView.setAdapter(adapter);
        }

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setEnabled(false);
    }

    private void startLoad() {
        if (loading || loaded) {
            return;
        }

        if (!NetworkUtils.isOnline()) {
            Toast.makeText(getContext(), R.string.alert_no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        loading = false;

        showSpinner();

        Subscription subscription = App.getInstance()
                .getService()
                .getQuotes()
                .compose(ObservableUtils.applyIoSchedulers())
                .subscribe(new Subscriber<QuotesResponse>() {
                    @Override
                    public void onCompleted() {
                        if (BuildConfig.DEBUG) {
                            Log.d(Consts.DEBUG_TAG, "download quotes completed");
                        }

                        loaded = true;
                        loading = false;

                        hideSpinner();

                        setupAdapter();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            Log.d(Consts.DEBUG_TAG, "download quotes failed", e);
                        }

                        loading = false;

                        hideSpinner();
                    }

                    @Override
                    public void onNext(QuotesResponse quotesResponse) {
                        if (BuildConfig.DEBUG) {
                            Log.d(Consts.DEBUG_TAG, "quotes downloaded");
                        }

                        quotes = quotesResponse.getQuotesList().getQuotes();
                    }
                });

        compositeSubscription.add(subscription);
    }

    private void showSpinner() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    private void hideSpinner() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupAdapter() {
        adapter = new QuotesAdapter(quotes);
        listView.setAdapter(adapter);
    }
}
