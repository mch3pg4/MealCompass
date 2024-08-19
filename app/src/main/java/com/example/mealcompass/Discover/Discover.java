package com.example.mealcompass.Discover;

public class Discover {
    String articleTitle;
    String articleContent;
    String articleImageUrl;
    String articleAuthor;
    String articleTime;

    public Discover() {
    }

    public Discover(String articleTitle, String articleContent, String articleImageUrl, String articleAuthor, String articleTime) {
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleImageUrl = articleImageUrl;
        this.articleAuthor = articleAuthor;
        this.articleTime = articleTime;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public String getArticleImageUrl() {
        return articleImageUrl;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public String getArticleTime() {
        return articleTime;
    }


    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public void setArticleImageUrl(String articleImageUrl) {
        this.articleImageUrl = articleImageUrl;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }



}
