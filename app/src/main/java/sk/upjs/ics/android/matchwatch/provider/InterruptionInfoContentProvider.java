package sk.upjs.ics.android.matchwatch.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;

public class InterruptionInfoContentProvider extends ContentProvider {

    private DatabaseOpenHelper dbHelper;

    public InterruptionInfoContentProvider() {
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
        Cursor cursor = db.query(Provider.InterruptionInfo.TABLE_NAME,
                Defaults.ALL_COLUMNS,
                Defaults.NO_SELECTION,
                Defaults.NO_SELECTION_ARGS,
                Defaults.NO_GROUP_BY,
                Defaults.NO_HAVING,
                Defaults.NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), Provider.InterruptionInfo.CONTENT_URI);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = db.insert(Provider.InterruptionInfo.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(Provider.InterruptionInfo.CONTENT_URI, null);

        return ContentUris.withAppendedId(Provider.InterruptionInfo.CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(Provider.InterruptionInfo.TABLE_NAME,
                Provider.InterruptionInfo._ID + "=" + id,
                null);

        getContext().getContentResolver().notifyChange(Provider.InterruptionInfo.CONTENT_URI, null);

        return rows;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.update(Provider.InterruptionInfo.TABLE_NAME, values, Provider.InterruptionInfo._ID + "=" + id, null);
        getContext().getContentResolver().notifyChange(Provider.InterruptionInfo.CONTENT_URI, null);

        return rows;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
