package com.seemantshekhar.spotlightfeed;

public class FeedEntry {
    private String name;
    private String author;
    private String pubDate;
    private String summary;

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getSummary() {
        return summary;
    }


    @Override
    public String toString() {
        return  "name=" + name + '\n' +
                "author=" + author + '\n' +
                "published date=" + pubDate + '\n' +
                "summary=" + summary + '\n';
    }
}
