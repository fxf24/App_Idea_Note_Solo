package com.example.screen_manager2;

public class Idea_Data {
    private String title;
    private String memo;
    private String link;
    private String date;
    int idx;
    int visibility;

    public Idea_Data(String title, String memo, String link, String date){
        this.title = title;
        this.memo = memo;
        this.link = link;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
