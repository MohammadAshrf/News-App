package com.example.newsapp;

public class News {

    private final String mTitle;
    private final String mArticle;
    private final String mUrl;
    private final String mDate;
    private final String mAuthor;

    public News(String title, String article, String url, String date, String author) {
        mTitle = title;
        mArticle = article;
        mUrl = url;
        mDate = date;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArticle() {
        return mArticle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
