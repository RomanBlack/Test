package ru.romanblack.test.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.qbusict.cupboard.CupboardFactory;
import ru.romanblack.test.BuildConfig;
import ru.romanblack.test.R;
import ru.romanblack.test.data.entities.Note;
import ru.romanblack.test.data.provider.NotesProvider;
import ru.romanblack.test.ui.dialog.ApplyDialog;
import ru.romanblack.test.util.Consts;
import ru.romanblack.test.util.ObservableUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.subscriptions.CompositeSubscription;

public class AddNoteActivity extends BaseActivity implements ApplyDialog.DialogListener {

    private Note note;
    private String originalTitle;

    private EditText editText;
    private View doneButton;
    private View revertButton;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_note);

        initializeData();

        initializeUi();
    }

    @Override
    protected void onDestroy() {
        compositeSubscription.unsubscribe();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        String title = editText.getText() == null ? "" : editText.getText().toString();

        if (originalTitle.equals(title)) {
            super.onBackPressed();
            return;
        }

        ApplyDialog dialog = ApplyDialog.create();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    private void initializeData() {
        Intent intent = getIntent();

        note = (Note) intent.getSerializableExtra(Consts.EXTRA_NOTE);

        originalTitle = note != null && note.getTitle() != null ? note.getTitle() : "";
    }

    private void initializeUi() {
        editText = (EditText) findViewById(R.id.edit);
        if (note != null) {
            editText.setText(note.getTitle());
        }

        doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClicked();
            }
        });

        revertButton = findViewById(R.id.revert);
        revertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRevertClicked();
            }
        });
    }

    private void onDoneClicked() {
        if (note == null) {
            note = new Note();
        }

        String title = editText.getText() == null ? "" : editText.getText().toString();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, R.string.title_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        note.setTitle(title);

        Subscription subscription = Observable
                .defer(new Func0<Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call() {
                        CupboardFactory.cupboard()
                                .withContext(AddNoteActivity.this)
                                .put(NotesProvider.NOTES_CONTENT_URI, note);

                        return Observable.just(true);
                    }
                })
                .compose(ObservableUtils.applyIoSchedulers())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            Log.d(Consts.DEBUG_TAG, this.getClass().toString(), e);
                        }
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

        compositeSubscription.add(subscription);
    }

    private void onRevertClicked() {
        finish();
    }

    @Override
    public void ok() {
        onDoneClicked();
    }

    @Override
    public void cancel() {
        finish();
    }
}
