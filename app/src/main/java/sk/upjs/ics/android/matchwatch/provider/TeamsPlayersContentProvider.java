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

public class TeamsPlayersContentProvider extends ContentProvider {

    private DatabaseOpenHelper dbHelper;

    public TeamsPlayersContentProvider() {
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
        Cursor cursor = db.query(Provider.TeamPlayer.TABLE_NAME,
                Defaults.ALL_COLUMNS,
                Defaults.NO_SELECTION,
                Defaults.NO_SELECTION_ARGS,
                Defaults.NO_GROUP_BY,
                Defaults.NO_HAVING,
                Defaults.NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), Provider.TeamPlayer.CONTENT_URI);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            long id = db.insertOrThrow(Provider.TeamPlayer.TABLE_NAME, null, values);
            getContext().getContentResolver().notifyChange(Provider.TeamPlayer.CONTENT_URI, null);
            return ContentUris.withAppendedId(Provider.TeamPlayer.CONTENT_URI, id);
        } catch (SQLiteException e) {
            Log.d("SQLiteException", "Caught the constraint.");
        }

        long id = getIdByRows(values.getAsLong(Provider.TeamPlayer.TEAM_ID), values.getAsLong(Provider.TeamPlayer.PLAYER_ID));
        return ContentUris.withAppendedId(Provider.TeamPlayer.CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(Provider.TeamPlayer.TABLE_NAME,
                Provider.TeamPlayer._ID + "=" + id,
                null);

        getContext().getContentResolver().notifyChange(Provider.TeamPlayer.CONTENT_URI, null);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.update(Provider.TeamPlayer.TABLE_NAME, values, Provider.TeamPlayer._ID + "=" + id, null);
        getContext().getContentResolver().notifyChange(Provider.TeamPlayer.CONTENT_URI, null);

        return rows;
    }

    public long getIdByRows(long teamId, long playerId) {
        String selectQuery = "SELECT * FROM " + Provider.TeamPlayer.TABLE_NAME;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                long tId = cursor.getLong(cursor.getColumnIndex(Provider.TeamPlayer.TEAM_ID));
                long pId = cursor.getLong(cursor.getColumnIndex(Provider.TeamPlayer.PLAYER_ID));

                if ((tId == teamId) && (pId == playerId)) {
                    return cursor.getLong(cursor.getColumnIndex(Provider.TeamPlayer._ID));
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
