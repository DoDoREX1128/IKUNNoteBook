package com.example.tetris.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 定义一个实体类UsersList，用于表示用户列表
@Entity(tableName = "usersList", primaryKeys = {"username", "createTime"})
public class UsersList {
    @NonNull
    @ColumnInfo(name = "username")
    private String username; // 用户名

    @NonNull
    @ColumnInfo(name = "createTime")
    private String createTime; // 创建时间

    @ColumnInfo(name = "content")
    private String content; // 内容

    // 获取用户名
    @NonNull
    public String getUsername() {
        return username;
    }

    // 设置用户名
    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    // 获取创建时间
    @NonNull
    public String getCreateTime() {
        return createTime;
    }

    // 设置创建时间
    public void setCreateTime(@NonNull String createTime) {
        this.createTime = createTime;
    }

    // 获取内容
    public String getContent() {
        return content;
    }

    // 设置内容
    public void setContent(String content) {
        this.content = content;
    }
}