package com.seasonsread.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.seasonsread.app.SeasonsReadApp;

/**
 * Created by ZhanTao on 1/13/15.
 */
public class FeedsDBHelper extends /*OrmLiteSqliteOpenHelper*/SQLiteOpenHelper {

    private static final String DB_NAME = "seasonsread.db";

    private static final int VERSION = 1;

    private static FeedsDBHelper mFeedsDBHelper = null;

    private FeedsDBHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    public static FeedsDBHelper getDBHelper(){
        if(mFeedsDBHelper == null)
            mFeedsDBHelper = new FeedsDBHelper(SeasonsReadApp.getAppContext());
        return mFeedsDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                        FeedsDBTable.TABLE_NAME+ "("+
                        FeedsDBTable.ROW_ID + " integer primary key autoincrement," +
                        FeedsDBTable.ROW_TITLE + " varchar," +
                        FeedsDBTable.ROW_AUTHOR + " varchar," +
                        FeedsDBTable.ROW_ARTICLE_ID + " integer," +
                        FeedsDBTable.ROW_TYPE_ID + " integer," +
                        FeedsDBTable.ROW_HITNUM + " integer," +
                        FeedsDBTable.ROW_IS_RESERVED + " integer," +
                        FeedsDBTable.ROW_CREATE_TIME + " varchar" +
                        ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + FeedsDBTable.TABLE_NAME );
        onCreate(db);
    }


//    // the DAO object we use to access the Feed table
//    private Dao<Feed, Integer> feedDao = null;
//
//    public FeedsDBHelper(Context context) {
//        super(context, DB_NAME, null, VERSION);
//    }
//
//    /**
//     * This is called when the database is first created. Usually you should call createTable statements here to create
//     * the tables that will store your data.
//     */
//    @Override
//    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
//        try {
//            Log.i(FeedsDBHelper.class.getName(), "onCreate");
//            TableUtils.createTable(connectionSource, Feed.class);
//
//        } catch (SQLException e) {
//            Log.e(FeedsDBHelper.class.getName(), "Can't create database", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
//     * the various data to match the new version number.
//     */
//    @Override
//    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {
//            Log.i(FeedsDBHelper.class.getName(), "onUpgrade");
//            TableUtils.dropTable(connectionSource, Feed.class, true);
//            // after we drop the old databases, we create the new ones
//            onCreate(db, connectionSource);
//        } catch (SQLException e) {
//            Log.e(FeedsDBHelper.class.getName(), "Can't drop databases", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Returns the Database Access Object (DAO) for our Feed class. It will create it or just give the cached
//     * value.
//     */
//    public Dao<Feed, Integer> getFeedDao() throws SQLException {
//        if (feedDao == null) {
//            feedDao = getDao(Feed.class);
//        }
//        return feedDao;
//    }
//
//    /**
//     * Close the database connections and clear any cached DAOs.
//     */
//    @Override
//    public void close() {
//        super.close();
//        feedDao = null;
//    }
}
