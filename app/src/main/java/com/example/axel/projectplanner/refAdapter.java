package com.example.axel.projectplanner;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class refAdapter extends CursorAdapter {
    public static class ViewHolder {
        public final TextView ftitleView;
        public final TextView fauthorView;

        public ViewHolder(View view) {
            ftitleView = (TextView) view.findViewById(R.id.ref_title);
            fauthorView = (TextView) view.findViewById(R.id.ref_author);
        }
    }

    public refAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = -1;
        layoutId = R.layout.list_item_ref;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read weather forecast from cursor
        String title = cursor.getString(TabFragment3.COL_PROJECT_TITLE);
        // Find TextView and set weather forecast on it
        viewHolder.ftitleView.setText(title);
        // Read weather forecast from cursor
        String author = cursor.getString(TabFragment3.COL_PROJECT_AUTHOR);
        // Find TextView and set weather forecast on it
        viewHolder.fauthorView.setText(author);

    }
}
