package ru.romanblack.test.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardFactory;
import ru.romanblack.test.data.entities.Note;

public class DatabaseManager {

    public DatabaseManager(Context context) {
        helper = new SqliteHelper(context);
    }

    private SqliteHelper helper;

    public Cursor getNotesCursor(
            String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cupboard cupboard = CupboardFactory.cupboard();

        return cupboard
                .withDatabase(db)
                .query(Note.class)
                .withProjection(projection)
                .withSelection(selection, selectionArgs)
                .orderBy(sortOrder)
                .getCursor();
    }

    public void addNote(Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Cupboard cupboard = CupboardFactory.cupboard();

        cupboard
                .withDatabase(db)
                .put(note);
    }

    public long addNote(ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Cupboard cupboard = CupboardFactory.cupboard();

        return cupboard
                .withDatabase(db)
                .put(Note.class, contentValues);
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Cupboard cupboard = CupboardFactory.cupboard();

        cupboard
                .withDatabase(db)
                .delete(note);
    }

    public void deleteNote(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Cupboard cupboard = CupboardFactory.cupboard();

        cupboard
                .withDatabase(db)
                .delete(Note.class, id);
    }

}
