package ru.romanblack.test.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardFactory;
import ru.romanblack.test.data.entities.Note;

public class SqliteHelper extends SQLiteOpenHelper {

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 1;

    static {
        Cupboard cupboard = CupboardFactory.cupboard();
        cupboard.register(Note.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CupboardFactory
                .cupboard()
                .withDatabase(db)
                .createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CupboardFactory
                .cupboard()
                .withDatabase(db)
                .upgradeTables();
    }
}
