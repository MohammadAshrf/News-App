package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {


    private static final int NEWS_LOADER_ID = 1;
    ListView lvNews;
    ProgressBar spinKit;
    private NewsAdapter mAdapter;
    private TextView mTvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        lvNews = findViewById (R.id.lv);
        mTvEmpty = findViewById (R.id.tvEmpty);
        spinKit = findViewById (R.id.spinKit);

        lvNews.setOnItemClickListener ((parent, view, position, id) -> {
            News currentNews = mAdapter.getItem (position);

            Uri uri = Uri.parse (currentNews.getUrl ());

            Intent in = new Intent (Intent.ACTION_VIEW, uri);
            startActivity (in);
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo ();

        if (networkInfo != null && networkInfo.isConnected ()) {
            LoaderManager loaderManager = getSupportLoaderManager ();
            loaderManager.initLoader (NEWS_LOADER_ID, null, this);
        } else {
            spinKit.setVisibility (View.GONE);
            mTvEmpty.setText (R.string.internet_connection);
        }

    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri.Builder builder = new Uri.Builder ();
        builder.scheme ("https")
                .authority ("content.guardianapis.com")
                .appendPath ("search")
                .appendQueryParameter ("show-tags", "contributor")
                .appendQueryParameter ("api-key", "test");

        return new NewsLoader (this, builder.toString ());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        if (data != null) {
            mAdapter = new NewsAdapter (this, data);
            spinKit.setVisibility (View.GONE);
            lvNews.setAdapter (mAdapter);
        } else {
            spinKit.setVisibility (View.GONE);
            mTvEmpty.setText (R.string.error_msg);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {

    }
}