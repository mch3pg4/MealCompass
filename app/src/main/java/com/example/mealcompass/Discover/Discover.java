package com.example.mealcompass.Discover;

public class Discover {
    private  String articleId;
    private String articleTitle;
    private String articleContent;
    private String articleImageUrl;
    private String articleAuthor;
    private String articleTime;

    public Discover() {
    }


    public Discover(String articleId, String articleTitle, String articleContent, String articleImageUrl, String articleAuthor, String articleTime) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleImageUrl = articleImageUrl;
        this.articleAuthor = articleAuthor;
        this.articleTime = articleTime;
    }

    public String getArticleId() {
        return articleId;
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

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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
