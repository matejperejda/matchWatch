package sk.upjs.ics.android.matchwatch.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;

public class PersonContentProvider extends ContentProvider {

    private DatabaseOpenHelper dbHelper;

    public PersonContentProvider() {
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DatabaseOpenHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Provider.Person.TABLE_NAME,
                Defaults.ALL_COLUMNS,
                Defaults.NO_SELECTION,
                Defaults.NO_SELECTION_ARGS,
                Defaults.NO_GROUP_BY,
                Defaults.NO_HAVING,
                Defaults.NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), Provider.Person.CONTENT_URI);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // osetrenie duplicit
        try {
            long id = db.insertOrThrow(Provider.Person.TABLE_NAME, null, values);
            getContext().getContentResolver().notifyChange(Provider.Person.CONTENT_URI, null);
            return ContentUris.withAppendedId(Provider.Person.CONTENT_URI, id);
        } catch (SQLiteConstraintException e) {
            Log.d("SQLiteException", "Caught the constraint.");
        }
        return ContentUris.withAppendedId(Provider.Person.CONTENT_URI, -1);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(Provider.Person.TABLE_NAME,
                Provider.Person._ID + "=" + id,
                null);

        getContext().getContentResolver().notifyChange(Provider.Person.CONTENT_URI, null);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.update(Provider.Person.TABLE_NAME, values, Provider.Person._ID + "=" + id, null);
        getContext().getContentResolver().notifyChange(Provider.Person.CONTENT_URI, null);

        return rows;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
