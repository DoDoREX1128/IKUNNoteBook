package com.example.tetris.adapt;

public class DayItem {
    private String title;// 日程标题
    private String description;// 日程描述
    private String date;// 日期
    private String username;// 用户名

    public DayItem(String title, String description, String date, String username) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
