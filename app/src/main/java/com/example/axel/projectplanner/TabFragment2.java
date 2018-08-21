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

public class TabFragment2 extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    private festAdapter mprojectAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int PROJECT_LOADER = 1;

    private static final String[] PROJECT_COLUMNS = {

            ProjectContract._ID,
            ProjectContract.COLUMN_DATE,
            ProjectContract.COLUMN_DESCRIPTION,
            ProjectContract.COLUMN_TITLE,
            ProjectContract.COLUMN_AUTHOR,
            ProjectContract.COLUMN_FEST_ID

    };
    static final int COL_PROJECT_ID = 0;
    static final int COL_PROJECT_DATE = 1;
    static final int COL_PROJECT_DESC = 2;
    static final int COL_PROJECT_TITLE = 3;
    static final int COL_PROJECT_AUTHOR = 4;
    static final int COL_FEST_ID=5;


    public interface Callback {
        void onItemSelected(Uri festIDUri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mprojectAdapter = new festAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_fests);
        mListView.setAdapter(mprojectAdapter);
        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(ProjectContract.buildFestWithID(cursor.getInt(COL_FEST_ID)
                    ));
                }
                mPosition = position;
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent compose = new Intent(getActivity(),ComposeFest.class);
                startActivity(compose);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PROJECT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    private void updateProjects() {
        ProjectSyncAdapter.syncImmediately(getActivity());
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

        String sortOrder = ProjectContract.COLUMN_FEST_ID + " DESC";
        Uri projectUri = ProjectContract.buildfestUri(1000);

        return new CursorLoader(getActivity(),
                projectUri,
                PROJECT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mprojectAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mprojectAdapter.swapCursor(null);
    }

}
