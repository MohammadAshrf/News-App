package com.example.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName ();

    private QueryUtils() {
    }

    public static List<News> getNewsData(String requestUrl) {
        URL url = createUrl (requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = httpRequest (url);
        } catch (IOException e) {
            Log.e (LOG_TAG, "Problem in the http request", e);
        }
        return extractFeatureFromJson (jsonResponse);
    }

    private static URL createUrl(String createUrl) {
        URL url = null;
        try {
            url = new URL (createUrl);
        } catch (MalformedURLException e) {
            Log.e (LOG_TAG, "Problem in running URL ", e);
        }
        return url;
    }

    private static String httpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.setReadTimeout (10000);
            urlConnection.setConnectTimeout (15000);
            urlConnection.setRequestMethod ("GET");
            urlConnection.connect ();

            if (urlConnection.getResponseCode () == 200) {
                inputStream = urlConnection.getInputStream ();
                jsonResponse = readInputStream (inputStream);
            } else {
                Log.e (LOG_TAG, "Error response code: " + urlConnection.getResponseCode ());
            }
        } catch (IOException e) {
            Log.e (LOG_TAG, "Problem in retrieve the news json results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect ();
            }
            if (inputStream != null) {
                inputStream.close ();
            }
        }
        return jsonResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder ();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader (inputStreamReader);
            String line = reader.readLine ();
            while (line != null) {
                output.append (line);
                line = reader.readLine ();
            }
        }
        return output.toString ();
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty (jsonResponse)) {
            return null;
        }

        List<News> news = new ArrayList<> ();

        try {
            JSONObject jsonObject = new JSONObject (jsonResponse);
            JSONObject response = jsonObject.getJSONObject ("response");
            JSONArray results = response.getJSONArray ("results");

            for (int i = 0; i < results.length (); i++) {

                JSONObject properties = results.getJSONObject (i);
                String title = properties.getString ("webTitle");
                String article = properties.getString ("sectionName");
                String date = properties.getString ("webPublicationDate");
                String url = properties.getString ("webUrl");
                JSONArray authorArray = properties.getJSONArray ("tags");
                String author;
                if (authorArray.length () != 0) {
                    JSONObject currentAuthor = authorArray.getJSONObject (0);
                    author = currentAuthor.getString ("webTitle");
                } else {
                    author = "Author not found";
                }

                News newsObj = new News (title, article, url, date, author);

                news.add (newsObj);
            }

        } catch (JSONException e) {
            Log.e ("QueryUtils", "Problem in parsing news json results", e);
        }

        return news;
    }
}
