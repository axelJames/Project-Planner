package com.example.axel.projectplanner;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class ProjectSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = ProjectSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180 * 4;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public ProjectSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String projectJsonStr = null;

        //fest sync
        String festId=Integer.toString(prefs.getInt("newFest",0));
        try {

            final String FEST_BASE_URL =
                    "https://adjoining-shelf-7514.nanoscaleapi.io/fest?";
            Uri builtUri = Uri.parse(FEST_BASE_URL).buildUpon().appendQueryParameter("fest_id",festId ).build();

            URL url = new URL(builtUri.toString());
            Log.d("hj",builtUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            projectJsonStr = buffer.toString();
            Log.d("Herefest",projectJsonStr);
            if(projectJsonStr=="null")
            {
                return;
            }
            getFestDataFromJson(projectJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        //ref sync
        String refId=Integer.toString(prefs.getInt("newRef",0));
        try {

            final String REF_BASE_URL =
                    "https://adjoining-shelf-7514.nanoscaleapi.io/ref?";
            Uri builtUri = Uri.parse(REF_BASE_URL).buildUpon().appendQueryParameter("ref_id",refId ).build();

            URL url = new URL(builtUri.toString());
            Log.d("hj",builtUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            projectJsonStr = buffer.toString();
            getRefDataFromJson(projectJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {

            final String DELETE_URL =
                    "https://adjoining-shelf-7514.nanoscaleapi.io/del";

            URL url = new URL(DELETE_URL);
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            projectJsonStr = buffer.toString();
            getDelDataFromJson(projectJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }
    private void getDelDataFromJson(String projectJsonStr)throws JSONException {
        final String DELPRO = "delpro";
        final String DELFEST = "delfest";
        final String DELREF = "delref";

        try {
            JSONArray projectArray = new JSONArray(projectJsonStr);
            JSONObject deleteList = projectArray.getJSONObject(0);
            JSONArray delfestArray = deleteList.getJSONArray(DELFEST);
            for (int i = 0; i < delfestArray.length(); i++)
                getContext().getContentResolver().delete(ProjectContract.FEST_CONTENT_URI,
                        ProjectContract.COLUMN_FEST_ID + " == ?",
                        new String[]{Integer.toString(delfestArray.getInt(i))});

            JSONArray delrefArray = deleteList.getJSONArray(DELREF);
            for (int i = 0; i < delrefArray.length(); i++)
                getContext().getContentResolver().delete(ProjectContract.REF_CONTENT_URI,
                        ProjectContract.COLUMN_REF_ID + " == ?",
                        new String[]{Integer.toString(delrefArray.getInt(i))});

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
    private void getFestDataFromJson(String projectJsonStr)throws JSONException {
        final String TITLE = "title";
        final String DESCRIPTION = "description";
        final String AUTHOR = "author";
        final String DATE = "date";
        final String FEST = "fest_id";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int festnew=prefs.getInt("newFest",0);

        try {
            JSONArray projectArray =new JSONArray(projectJsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(projectArray.length());

            for (int i = 0; i < projectArray.length(); i++) {
                String date;
                String title;
                String author;
                String description;
                int fest_id;
                JSONObject myProject = projectArray.getJSONObject(i);

                description = myProject.getString(DESCRIPTION);
                date = myProject.getString(DATE);
                author = myProject.getString(AUTHOR);
                title = myProject.getString(TITLE);
                fest_id = myProject.getInt(FEST);

                ContentValues projectValues = new ContentValues();

                projectValues.put(ProjectContract.COLUMN_TITLE, title);
                projectValues.put(ProjectContract.COLUMN_DATE, date);
                projectValues.put(ProjectContract.COLUMN_AUTHOR, author);
                projectValues.put(ProjectContract.COLUMN_DESCRIPTION, description);
                projectValues.put(ProjectContract.COLUMN_FEST_ID, fest_id);

                cVVector.add(projectValues);
                if(festnew<fest_id)
                    festnew=fest_id;

            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("newFest",festnew);
            editor.commit();

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(ProjectContract.FEST_CONTENT_URI, cvArray);
            }
            //Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getRefDataFromJson(String projectJsonStr)throws JSONException {
        final String TITLE = "title";
        final String refURL = "url";
        final String AUTHOR = "author";
        final String REF = "ref_id";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int refnew=prefs.getInt("newRef",0);
        try {
            JSONArray projectArray =new JSONArray(projectJsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(projectArray.length());

            for (int i = 0; i < projectArray.length(); i++) {
                String title;
                String author;
                String description;
                int ref_id;
                JSONObject myProject = projectArray.getJSONObject(i);

                description = myProject.getString(refURL);
                author = myProject.getString(AUTHOR);
                title = myProject.getString(TITLE);
                ref_id = myProject.getInt(REF);

                ContentValues projectValues = new ContentValues();

                projectValues.put(ProjectContract.COLUMN_TITLE, title);
                projectValues.put(ProjectContract.COLUMN_AUTHOR, author);
                projectValues.put(ProjectContract.COLUMN_URL, description);
                projectValues.put(ProjectContract.COLUMN_REF_ID, ref_id);

                cVVector.add(projectValues);
                if(refnew<ref_id)
                    refnew=ref_id;

            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("newRef",refnew);
            editor.commit();

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(ProjectContract.REF_CONTENT_URI, cvArray);
            }
            //Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {

            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }


    public static Account getSyncAccount(Context context) {

        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if ( null == accountManager.getPassword(newAccount) ) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {

        ProjectSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
