package sk.upjs.ics.android.matchwatch.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.matchwatch.Defaults;
import sk.upjs.ics.android.matchwatch.entity.Team;
import sk.upjs.ics.android.matchwatch.openhelper.DatabaseOpenHelper;

public class TeamsContentProvider extends ContentProvider {

    private DatabaseOpenHelper dbHelper;

    public TeamsContentProvider() {
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
        Cursor cursor = db.query(Provider.Team.TABLE_NAME,
                Defaults.ALL_COLUMNS,
                Defaults.NO_SELECTION,
                Defaults.NO_SELECTION_ARGS,
                Defaults.NO_GROUP_BY,
                Defaults.NO_HAVING,
                Defaults.NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), Provider.Team.CONTENT_URI);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // osetrenie duplicit
        try {
            long id = db.insertOrThrow(Provider.Team.TABLE_NAME, null, values);
            getContext().getContentResolver().notifyChange(Provider.Team.CONTENT_URI, null);
            return ContentUris.withAppendedId(Provider.Team.CONTENT_URI, id);
        } catch (SQLiteException e) {
            Log.d("SQLiteException", "Caught the constraint.");
        }

        long id = getIdByName(values.getAsString(Provider.Team.FULL_NAME));
        return ContentUris.withAppendedId(Provider.Team.CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(Provider.Team.TABLE_NAME, Provider.Team._ID + "=" + id, null);
        getContext().getContentResolver().notifyChange(Provider.Team.CONTENT_URI, null);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.update(Provider.Team.TABLE_NAME, values, Provider.Team._ID + "=" + id, null);
        getContext().getContentResolver().notifyChange(Provider.Team.CONTENT_URI, null);

        return rows;
    }

    public int getCountEqualTeams(String fullName, String shortName) {
        Cursor c = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String query = "SELECT COUNT(*) FROM " + Provider.Team.TABLE_NAME
                    + " WHERE " + Provider.Team.FULL_NAME + "=\"" + fullName
                    + "\" AND " + Provider.Team.SHORT_NAME + "=\"" + shortName + "\"";
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

    // nepouzivane
    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Provider.Team.TABLE_NAME;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //TODO: Upravit spravne metodu
        if (cursor.moveToFirst()) {
            do {
                String fullName = cursor.getString(cursor.getColumnIndex(Provider.Team.FULL_NAME));
                String shortName = cursor.getString(cursor.getColumnIndex(Provider.Team.SHORT_NAME));

                Team team = new Team(fullName, shortName, null);
                teams.add(team);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return teams;
    }

    public long getIdByName(String name) {
        String selectQuery = "SELECT * FROM " + Provider.Team.TABLE_NAME;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String fullName = cursor.getString(cursor.getColumnIndex(Provider.Team.FULL_NAME));
                if (fullName.equals(name)) {
                    return cursor.getLong(cursor.getColumnIndex(Provider.Team._ID));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return -1;
    }

    public Team getTeamById(long id) {
        String selectQuery = "SELECT * FROM " + Provider.Team.TABLE_NAME;

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                long idT = cursor.getLong(cursor.getColumnIndex(Provider.Team._ID));
                String fullName = cursor.getString(cursor.getColumnIndex(Provider.Team.FULL_NAME));
                String shortName = cursor.getString(cursor.getColumnIndex(Provider.Team.SHORT_NAME));

                if (id == idT) {
                    return new Team(fullName, shortName);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
