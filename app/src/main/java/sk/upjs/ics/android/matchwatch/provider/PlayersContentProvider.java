package sk.upjs.ics.android.matchwatch.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;

public class PlayersContentProvider extends ContentProvider {

    private DatabaseOpenHelper dbHelper;

    public PlayersContentProvider() {
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
        Cursor cursor = db.query(Provider.Player.TABLE_NAME,
                Defaults.ALL_COLUMNS,
                Defaults.NO_SELECTION,
                Defaults.NO_SELECTION_ARGS,
                Defaults.NO_GROUP_BY,
                Defaults.NO_HAVING,
                Defaults.NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), Provider.Player.CONTENT_URI);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // osetrenie duplicit
        try {
            long id = db.insertOrThrow(Provider.Player.TABLE_NAME, null, values);
            getContext().getContentResolver().notifyChange(Provider.Player.CONTENT_URI, null);
            return ContentUris.withAppendedId(Provider.Player.CONTENT_URI, id);
        } catch (SQLiteException e) {
            Log.d("SQLiteException", "Caught the constraint.");
        }

        long id = getIdByName(values.getAsString(Provider.Player.FIRST_NAME), values.getAsString(Provider.Player.LAST_NAME));
        return ContentUris.withAppendedId(Provider.Player.CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(Provider.Player.TABLE_NAME,
                Provider.Player._ID + "=" + id,
                null);

        getContext().getContentResolver().notifyChange(Provider.Player.CONTENT_URI, null);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.update(Provider.Player.TABLE_NAME, values, Provider.Player._ID + "=" + id, null);
        getContext().getContentResolver().notifyChange(Provider.Player.CONTENT_URI, null);

        return rows;
    }

    public int getCountEqualPlayer(String firstName, String lastName, String birthDate, int number, String position, String shoots, double height, int weight, String club) {
        Cursor c = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String query = "SELECT COUNT(*) FROM " + Provider.Player.TABLE_NAME
                    + " WHERE " + Provider.Player.FIRST_NAME + "=" + firstName
                    + " AND " + Provider.Player.LAST_NAME + "=" + lastName
                    + " AND " + Provider.Player.BIRTH_DATE + "=" + birthDate
                    + " AND " + Provider.Player.NUMBER + "=" + number
                    + " AND " + Provider.Player.POSITION + "=" + position
                    + " AND " + Provider.Player.SHOOTS + "=" + shoots
                    + " AND " + Provider.Player.HEIGHT + "=" + height
                    + " AND " + Provider.Player.WEIGHT + "=" + weight
                    + " AND " + Provider.Player.CLUB + "=" + club;
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
            return 0;
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public long getIdByName(String firstName, String lastName) {
        String selectQuery = "SELECT * FROM " + Provider.Player.TABLE_NAME;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String fN = cursor.getString(cursor.getColumnIndex(Provider.Player.FIRST_NAME));
                String lN = cursor.getString(cursor.getColumnIndex(Provider.Player.LAST_NAME));

                if (firstName.equals(fN) && lastName.equals(lN)) {
                    return cursor.getLong(cursor.getColumnIndex(Provider.Player._ID));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return -1;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
