package com.example.axel.projectplanner;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class ProjectProvider extends ContentProvider {

    private ProjectDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ProjectContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "/fest", 220);
        matcher.addURI(authority, "/fest/#", 210);
        matcher.addURI(authority,"/fest/select/#",200);
        matcher.addURI(authority, "/ref/#", 310);
        matcher.addURI(authority, "/ref", 320);
        matcher.addURI(authority,"/ref/select/*",300);
        return matcher;
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    @Override
    public boolean onCreate() {
        mOpenHelper = new ProjectDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Log.d("errrr", "query: "+uri.toString());
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case 200:
                String Identity2=ProjectContract.COLUMN_FEST_ID+" =? ";
                retCursor=mOpenHelper.getReadableDatabase().query(ProjectContract.TABLE_FEST,projection,Identity2,new String[]{getId(uri,2)},null,null,sortOrder);
                break;
            case 210:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProjectContract.TABLE_FEST,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case 220:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProjectContract.TABLE_FEST,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case 300:
                String Identity3=ProjectContract.COLUMN_REF_ID+" =? ";
                retCursor=mOpenHelper.getReadableDatabase().query(ProjectContract.TABLE_REF,projection,Identity3,new String[]{getId(uri,2)},null,null,sortOrder);
                break;
            case 310:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProjectContract.TABLE_REF,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case 320:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ProjectContract.TABLE_REF,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:retCursor = mOpenHelper.getReadableDatabase().query(
                    ProjectContract.TABLE_FEST,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

    return retCursor;
}

    private String getId(Uri uri,int i) {
        return uri.getPathSegments().get(i);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case 200:
                return ProjectContract.CONTENT_ITEM_TYPE;
            case 210:
                return ProjectContract.CONTENT_TYPE;
            case 300:
                return ProjectContract.CONTENT_ITEM_TYPE;
            case 310:
                return ProjectContract.CONTENT_TYPE;
        }
            return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long _id=-1;
        switch (match) {
            case 210:
                    _id = db.insert(ProjectContract.TABLE_FEST, null, values);
                break;
            case 310:
                    _id = db.insert(ProjectContract.TABLE_REF, null, values);
                break;
            default:
                break;
        }
        if ( _id < 0 )
            throw new android.database.SQLException("Failed to insert row into " );
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
            switch (match) {
                case 220:

                        rowsDeleted = db.delete(
                                ProjectContract.TABLE_FEST, selection, selectionArgs);
                    break;
                default:
                    rowsDeleted = db.delete(
                            ProjectContract.TABLE_REF, selection, selectionArgs);break;
            }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;
        final int match = sUriMatcher.match(uri);
        rowsUpdated = db.update(ProjectContract.TABLE_FEST, values, selection,
                selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        db.beginTransaction();
        int returnCount = 0;
        long _id=-1;
        try {
            switch (match) {
                case 220:
                    for (ContentValues value : values) {
                        _id = db.insert(ProjectContract.TABLE_FEST, null, value);
                    }break;
                case 320:
                    for (ContentValues value : values) {
                        _id = db.insert(ProjectContract.TABLE_REF, null, value);
                    }break;
                default:
                    break;
            }
            if (_id != -1) {
                returnCount++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}
