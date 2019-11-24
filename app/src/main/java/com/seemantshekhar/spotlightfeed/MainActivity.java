package com.seemantshekhar.spotlightfeed;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ListView listBlogs;
    private String feedURL = "http://spotlight.nitdelhi.ac.in/category/%s/feed/";
    private String subString = "highlights";
    private String cachedURL = "INVALIDATED";
    private static final String FEED_SUBSTRING = "subString";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listBlogs = (ListView) findViewById(R.id.xmlListView);

        if(savedInstanceState != null){
            subString = savedInstanceState.getString(FEED_SUBSTRING);
        }

        downloadUrl(String.format(feedURL, subString));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.mnuHighlights:
            case R.id.mnuCreativity:
            case R.id.mnuEntertainment:
            case R.id.mnuScienceAndTechnology:
            case R.id.mnuSpotrs:
            case R.id.mnuThirdEye:
                subString = item.getTitle().toString().replace(" ", "-");
                Log.d(TAG, "onOptionsItemSelected: feed url is: " + item.getTitle().toString().replace(" ", "-"));
                break;
            case R.id.mnuRefresh:
                cachedURL = "INVALIDATED";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        downloadUrl(String.format(feedURL,subString));
        return true;
    }

    private void downloadUrl(String feedURL){
        /*
         1. Creating an instance of downloadData class
         2. calling execute method by passing url
          */
        if(!feedURL.equalsIgnoreCase(cachedURL))
        {
            DownloadData downloadData = new DownloadData();
            downloadData.execute(feedURL);
            cachedURL = feedURL;
        } else{
            Log.d(TAG, "downloadUrl: Url not changed");
        }
        
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FEED_SUBSTRING, subString);
        super.onSaveInstanceState(outState);
    }

    // asyncTask class
    private class DownloadData extends AsyncTask <String, Void, String>{
        private static final String TAG = "DownloadData";
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParseBlogs parseBlogs = new ParseBlogs();
            parseBlogs.parse(s);

            // inflating the list item through feedAdapter
            FeedAdapter feedAdapter = new FeedAdapter(
                    MainActivity.this, R.layout.list_item, parseBlogs.getBlogs()
            );
            listBlogs.setAdapter(feedAdapter);

        }



        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with :" + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed == null){
                Log.e(TAG, "doInBackground: Error downloading feed");
            }

            return rssFeed;
        }

        private String downloadXML(String urlPath){
            // String builder to store the xmlData
            StringBuilder xmlData = new StringBuilder();
            try {
                // Establishing HTTP connection
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int responseCode = connection.getResponseCode();
                Log.e(TAG, "downloadXML: Response code was " + responseCode );

                // Buffer reader to read the data
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int charsRead;
                char[]inputBuffer = new char[500];
                while (true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0){
                        break;
                    }
                    if(charsRead > 0){
                        xmlData.append(String.copyValueOf(inputBuffer,0,charsRead));
                    }
                }
                reader.close();
                return xmlData.toString();
            } catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage() );
            } catch (IOException e){
                Log.e(TAG, "downloadXML: IO exception reading data " + e.getMessage());
            }

            return null;

        }
    }
}
