package ru.romanblack.test.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import nl.qbusict.cupboard.CupboardFactory;
import ru.romanblack.test.BuildConfig;
import ru.romanblack.test.R;
import ru.romanblack.test.data.entities.Note;
import ru.romanblack.test.data.provider.NotesProvider;
import ru.romanblack.test.ui.activity.AddNoteActivity;
import ru.romanblack.test.ui.adapter.NotesAdapter;
import ru.romanblack.test.util.Consts;
import ru.romanblack.test.util.ObservableUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.subscriptions.CompositeSubscription;

public class ListFragment extends MainActivityFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, NotesAdapter.AdapterInterface{

    private Cursor notesCursor;

    private RecyclerView listView;

    private NotesAdapter adapter;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUi(view);

        startLoadNotes();
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();

        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startEdit(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUi(View view) {
        listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void startLoadNotes() {
        if (getMainActivity() != null) {
            getMainActivity().getSupportLoaderManager().initLoader(
                    Consts.LOADER_NOTES, null, this);
        }
    }

    private void showList() {
        if (adapter == null) {
            adapter = new NotesAdapter(notesCursor);
            adapter.setAdapterInterface(this);
            listView.setAdapter(adapter);
        } else {
            adapter.swapCursor(notesCursor);
        }
    }

    private PopupMenu createPopupMenu(View view, final Note note) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.popup_menu_list_fragment);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onPopupItemClicked(item, note);
            }
        });

        return popupMenu;
    }

    private boolean onPopupItemClicked(MenuItem item, Note note) {
        switch (item.getItemId()) {
            case R.id.edit:
                startEdit(note);
                return true;
            case R.id.delete:
                startDelete(note);
                return true;
        }

        return false;
    }

    private void startEdit(Note note) {
        Intent intent = new Intent(getContext(), AddNoteActivity.class);

        if (note != null) {
            intent.putExtra(Consts.EXTRA_NOTE, note);
        }

        startActivity(intent);
    }

    private void startDelete(final Note note) {
        Subscription subscription = Observable
                .defer(new Func0<Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call() {
                        int res = CupboardFactory.cupboard()
                                .withContext(getContext())
                                .delete(NotesProvider.NOTES_CONTENT_URI, note);

                        return Observable.just(res);
                    }
                })
                .compose(ObservableUtils.applyIoSchedulers())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        if (adapter != null) {
                            adapter.onRemoved(note);
                        }
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(), NotesProvider.NOTES_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        notesCursor = data;

        showList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(View view, int position, Note item) {
        startEdit(item);
    }

    @Override
    public void onItemLongClick(View view, int position, Note item) {
        PopupMenu popupMenu = createPopupMenu(view, item);
        popupMenu.show();
    }
}
