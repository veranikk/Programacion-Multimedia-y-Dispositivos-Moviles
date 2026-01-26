package com.example.baseadapter;

public class ListItem {
    public int getImageResId() {
        return imageResId;
    }

    private int imageResId;

    public String getTitle() {
        return title;
    }

    private String title;

    public String getContent() {
        return content;
    }

    private String content;

    public ListItem(int imageResId, String title, String content){
        this.imageResId= imageResId;
        this.title=title;
        this.content=content;
    }



}
