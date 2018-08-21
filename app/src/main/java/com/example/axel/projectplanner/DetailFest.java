package com.example.axel.projectplanner;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;


public class DetailFest extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    private Uri mUri;

    private static final int DETAIL_LOADER = 5;

    private static final String[] DETAIL_FESTCOLUMNS = {
            ProjectContract.TABLE_FEST + "." + ProjectContract._ID,
            ProjectContract.COLUMN_DATE,
            ProjectContract.COLUMN_DESCRIPTION,
            ProjectContract.COLUMN_AUTHOR,
            ProjectContract.COLUMN_TITLE
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_PROJECT_ID = 0;
    public static final int COL_PROJECT_DATE = 1;
    public static final int COL_PROJECT_DESCRIPTION = 2;
    public static final int COL_PROJECT_AUTHOR = 3;
    public static final int COL_PROJECT_TITLE = 4;

    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mTitleView;
    private TextView mAuthorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fest);
        Intent i=getIntent();
        mUri=i.getData();
        getSupportLoaderManager().initLoader(DETAIL_LOADER, null,this);
        mDateView = (TextView)findViewById(R.id.festdetaildate);
        mTitleView = (TextView) findViewById(R.id.festdetailtitle);
        mDescriptionView = (TextView) findViewById(R.id.festdetaildescription);
        mAuthorView = (TextView)findViewById(R.id.festdetailauthor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    this,
                    mUri,
                    DETAIL_FESTCOLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            // Read date from cursor and update views for day of week and date
            String date = data.getString(COL_PROJECT_DATE);
            mDateView.setText(date);
            // Read description from cursor and update view
            String description = data.getString(COL_PROJECT_DESCRIPTION);
            mDescriptionView.setText(description);
            String title = data.getString(COL_PROJECT_TITLE);
            mTitleView.setText(title);
            String author = data.getString(COL_PROJECT_AUTHOR);
            mAuthorView.setText(author);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
