package com.seasonsread.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.seasonsread.app.data.db.FeedsDBHelper;
import com.seasonsread.app.data.db.FeedsDBTable;

/**
 * Created by ZhanTao on 1/13/15.
 */
public class FeedsContentProvider extends ContentProvider {

    // MIME type definitions
    public static final String FEEDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.seasonsread.feed";

    private static final String TAG = "FeedsContentProvider";
    private static final Object DBLock = new Object();
    private static final String AUTHORITY = "com.seasonsread.app.data.FeedsContentProvider";
    private static final UriMatcher URI_MATHCER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String PATH_FEEDS = "feeds";
    public static final Uri FEEDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_FEEDS);
    private static final int FEEDS = 0;

    static {
        URI_MATHCER.addURI(AUTHORITY, PATH_FEEDS, FEEDS);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public String getType(Uri uri) {
        int nFlag = URI_MATHCER.match(uri);
        switch (nFlag) {
            case FEEDS:
                return FEEDS_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (DBLock) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String table = matchTable(uri);
            queryBuilder.setTables(table);

            SQLiteDatabase db = FeedsDBHelper.getDBHelper().getReadableDatabase();
            Cursor cursor = queryBuilder.query(db, // The database to
                    // queryFromDB
                    projection, // The columns to return from the queryFromDB
                    selection, // The columns for the where clause
                    selectionArgs, // The values for the where clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    sortOrder // The sort order
            );

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = FeedsDBHelper.getDBHelper().getWritableDatabase();
            int count;
            String table = matchTable(uri);
            db.beginTransaction();
            try {
                count = db.update(table, values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (DBLock) {
            String table = matchTable(uri);
            SQLiteDatabase db = FeedsDBHelper.getDBHelper().getWritableDatabase();
            long rowId = 0;
            db.beginTransaction();
            try {
                rowId = db.insert(table, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                db.endTransaction();
            }
            if (rowId > 0) {
                Uri returnUri = ContentUris.withAppendedId(uri, rowId);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnUri;
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int insertNum = 0;

        synchronized (DBLock) {
            String table = matchTable(uri);
            SQLiteDatabase db = FeedsDBHelper.getDBHelper().getWritableDatabase();
            db.beginTransaction();
            try {
                for (ContentValues value : values) {
                    if (value.get(FeedsDBTable.ROW_ARTICLE_ID) != null) {
                        if (db.update(table, value, FeedsDBTable.ROW_ARTICLE_ID + "=?", new String[]{
                                String.valueOf(value.get(FeedsDBTable.ROW_ARTICLE_ID))
                        }) == 0) {
                            long rowId = db.insert(table, null, value);
                            if (rowId > 0) {
                            }
                        }
                    }
                    insertNum++;
                }
                db.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                db.endTransaction();
            }
            if (insertNum > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return insertNum/*super.bulkInsert(uri, values)*/;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = FeedsDBHelper.getDBHelper().getWritableDatabase();

            int count = 0;
            String table = matchTable(uri);
            db.beginTransaction();
            try {
                count = db.delete(table, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    private String matchTable(Uri uri) {
        String table = null;
        switch (URI_MATHCER.match(uri)) {
            case FEEDS:
                table = FeedsDBTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return table;
    }
}
