package com.example.axel.projectplanner;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class TabFragment3 extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private refAdapter mrefAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int REF_LOADER = 2;

    private static final String[] PROJECT_COLUMNS = {

            ProjectContract._ID,
            ProjectContract.COLUMN_URL,
            ProjectContract.COLUMN_TITLE,
            ProjectContract.COLUMN_AUTHOR,
            ProjectContract.COLUMN_REF_ID

    };
    static final int COL_PROJECT_ID = 0;
    static final int COL_PROJECT_DESC = 1;
    static final int COL_PROJECT_TITLE = 2;
    static final int COL_PROJECT_AUTHOR = 3;
    static final int COL_REF_ID=4;


    public interface Callback {
        void onItemSelected(Uri refIDUri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mrefAdapter = new refAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_refs);
        mListView.setAdapter(mrefAdapter);
        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(ProjectContract.buildRefWithUrl(cursor.getString(COL_PROJECT_DESC)
                    ));
                }
                mPosition = position;
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent compose = new Intent(getActivity(),ComposeRef.class);
                startActivity(compose);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(REF_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String sortOrder = ProjectContract.COLUMN_REF_ID + " DESC";

        Uri projectUri = ProjectContract.buildrefUri(1000);
        return new CursorLoader(getActivity(),
                projectUri,
                PROJECT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mrefAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mrefAdapter.swapCursor(null);
    }

}