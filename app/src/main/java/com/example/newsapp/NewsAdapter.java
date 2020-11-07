package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, List<News> news) {
        super (context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View lv = convertView;
        if (lv == null) {
            lv = LayoutInflater.from (getContext ()).inflate (R.layout.custom_news_list, parent, false);
        }

        News currentNews = getItem (position);

        String date = currentNews.getDate ().substring (0, 10);

        TextView tvTitle = lv.findViewById (R.id.tvTitle);
        tvTitle.setText (currentNews.getTitle ());
        TextView tvArticle = lv.findViewById (R.id.tvArticle);
        tvArticle.setText (currentNews.getArticle ());
        TextView tvDate = lv.findViewById (R.id.tvDate);
        tvDate.setText (date);
        TextView tvAuthor = lv.findViewById (R.id.tvAuthor);
        tvAuthor.setText (currentNews.getAuthor ());

        return lv;

    }
}
