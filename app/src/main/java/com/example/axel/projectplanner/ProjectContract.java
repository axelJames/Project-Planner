package com.example.axel.projectplanner;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class ProjectContract implements BaseColumns {


    public static final String CONTENT_AUTHORITY = "com.example.axel.projects.projectprovider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri FEST_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY +"/fest");
    public static final Uri REF_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY +"/ref");

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY ;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY ;

    public static final String TABLE_FEST = "fests";
    public static final String TABLE_REF = "reference";

    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_FEST_ID="fest_id";
    public static final String COLUMN_REF_ID="ref_id";
    public static final String COLUMN_URL="ref_url";

    public static Uri buildFestWithID(int ID) {
        return BASE_CONTENT_URI.buildUpon().appendPath("fest").appendPath("select").appendPath(Integer.toString(ID)).build();
    }

    public static Uri buildfestUri(int l) {
        return BASE_CONTENT_URI.buildUpon().appendPath("fest").appendPath(Integer.toString(l)).build();
    }

    public static Uri buildrefUri(int i) {
        return BASE_CONTENT_URI.buildUpon().appendPath("ref").appendPath(Integer.toString(i)).build();
    }

    public static Uri buildRefWithID(int anInt) {
        return BASE_CONTENT_URI.buildUpon().appendPath("ref").appendPath("select").appendPath(Integer.toString(anInt)).build();
    }

    public static Uri buildRefWithUrl(String s) {
        return BASE_CONTENT_URI.buildUpon().appendPath("ref").appendPath("select").appendPath(s).build();
    }
}
