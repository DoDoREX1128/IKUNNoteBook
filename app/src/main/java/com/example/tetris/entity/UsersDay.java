package com.example.tetris.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "usersDay",primaryKeys = {"username", "createTime"})
public class UsersDay {
    @NonNull
    @ColumnInfo(name = "username")
    private String username; // 用户名

    @NonNull
    @ColumnInfo(name = "createTime")
    private String createTime; // 创建时间

    @ColumnInfo(name = "title")
    private String title; // 标题

    @ColumnInfo(name = "content")
    private String content; // 内容

    @ColumnInfo(name = "image_data")
    private String imageData; // 图片数据

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(@NonNull String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}