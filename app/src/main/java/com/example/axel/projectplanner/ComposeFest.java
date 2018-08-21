package com.example.axel.projectplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class ComposeFest extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_comose_fest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                EditText name=(EditText)findViewById(R.id.newfest_name);
                String eName=name.getText().toString();
                EditText title=(EditText) findViewById(R.id.newfest_title);
                String eTitle= title.getText().toString();
                EditText description=(EditText) findViewById(R.id.newfest_description);
                String eDescription= description.getText().toString();
                if (eTitle.isEmpty()||eDescription.isEmpty()||eName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Empty fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONObject post_dict = new JSONObject();
                    try {
                        post_dict.put("author" , eName);
                        post_dict.put("title", eTitle);
                        post_dict.put("description",eDescription);
                        post_dict.put("fest_id",10);
                        post_dict.put("date", DateFormat.getDateInstance().format(new Date()));
                        post_dict.put("email",pref.getString("email",null));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (post_dict.length() > 0) {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Sending data");
                        progressDialog.show();
                        new HttpAsyncTask().execute(String.valueOf(post_dict));
                    }
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        Integer except;
        @Override
        protected String doInBackground(String... params) {
            except=0;
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://adjoining-shelf-7514.nanoscaleapi.io/newfest");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);

                writer.close();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    return null;
                }
                JsonResponse = buffer.toString();

                Log.i("tag",JsonResponse);
                return JsonResponse;

            } catch (IOException e) {
                except=1;
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(except==1)
            {
                AlertDialog ErrorBox =new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("An Error occured while connection. Please check your network connection")
                        .create();
                ErrorBox.show();
            }
            else
            {
                ProjectSyncAdapter.syncImmediately(ComposeFest.this);
                Intent intent = new Intent(ComposeFest.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
