package com.example.axel.projectplanner;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjectDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "project.db";

    public ProjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FEST_TABLE = "CREATE TABLE " + ProjectContract.TABLE_FEST + " (" +
                ProjectContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProjectContract.COLUMN_DATE + " TEXT NOT NULL, " +
                ProjectContract.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ProjectContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                ProjectContract.COLUMN_AUTHOR + " TEXT NOT NULL, "+
                ProjectContract.COLUMN_FEST_ID + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_FEST_TABLE);

        final String SQL_CREATE_REF_TABLE = "CREATE TABLE " + ProjectContract.TABLE_REF + " (" +
                ProjectContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProjectContract.COLUMN_URL + " TEXT NOT NULL, " +
                ProjectContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                ProjectContract.COLUMN_AUTHOR + " TEXT NOT NULL, "+
                ProjectContract.COLUMN_REF_ID + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_REF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProjectContract.TABLE_FEST );
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProjectContract.TABLE_REF );
        onCreate(sqLiteDatabase);
    }
}

