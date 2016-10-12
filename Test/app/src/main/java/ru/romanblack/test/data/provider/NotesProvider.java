package ru.romanblack.test.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import nl.qbusict.cupboard.CupboardFactory;
import ru.romanblack.test.App;
import ru.romanblack.test.BuildConfig;
import ru.romanblack.test.data.entities.Note;

public class NotesProvider extends ContentProvider {

    private static final int URI_NOTES = 1;
    private static final int URI_NOTES_ID = 2;

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".NotesProvider";
    private static final String NOTES_PATH = "notes";

    public static final Uri NOTES_CONTENT_URI = Uri.parse(
            "content://"
                    + AUTHORITY + "/"
                    + NOTES_PATH
    );

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTES_PATH, URI_NOTES);
        uriMatcher.addURI(AUTHORITY, NOTES_PATH + "/#", URI_NOTES_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                Cursor cursor = App.getInstance()
                        .getDatabaseManager()
                        .getNotesCursor(projection, selection, selectionArgs, sortOrder);

                cursor.setNotificationUri(getContext().getContentResolver(), NOTES_CONTENT_URI);

                return cursor;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
            case URI_NOTES_ID:
                long rowID = App.getInstance().getDatabaseManager().addNote(values);

                Uri resultUri = ContentUris.withAppendedId(NOTES_CONTENT_URI, rowID);

                getContext().getContentResolver().notifyChange(resultUri, null);

                return resultUri;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES_ID:
                long id = ContentUris.parseId(uri);

                App.getInstance().getDatabaseManager().deleteNote(id);

                getContext().getContentResolver().notifyChange(NOTES_CONTENT_URI, null);
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
