package com.example.tetris.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tetris.R;
import com.example.tetris.dao.UsersDayDao;
import com.example.tetris.dao.UsersListDao;
import com.example.tetris.db.DataBase;
import com.example.tetris.entity.UsersList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CreateListActivity extends AppCompatActivity {

    private ImageView imageButtonImageView; // 图片按钮
    private TextView textView; // 文本视图
    private EditText editText; // 编辑文本框
    private String content; // 待办内容
    private String currentTime; // 当前时间
    private String receivedUsername; // 接收到的用户名
    private DataBase db; // 数据库
    private UsersListDao usersListDao; // 用户列表数据访问对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        init(); // 初始化
    }

    private void init() {
        imageButtonImageView = findViewById(R.id.image_ok); // 获取图片按钮
        textView = findViewById(R.id.textView); // 获取文本视图
        editText = findViewById(R.id.editText); // 获取编辑文本框

        // 获取传递过来的Username
        receivedUsername = getIntent().getStringExtra("Username");

        // 获取当前时间
        Calendar calendar = Calendar.getInstance(); // 获取日历实例
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建格式化时间的对象
        currentTime = dateFormat.format(calendar.getTime()); // 将当前时间格式化为字符串
        textView.setText(currentTime); // 在文本视图中显示当前时间

        // 数据库初始化
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "mydb")
//                       .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        usersListDao = db.usersListDao(); // 获取用户列表数据访问对象

        imageButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = editText.getText().toString(); // 获取编辑文本框中的内容
                if (save()) { // 调用保存方法
                    Toast.makeText(CreateListActivity.this, "保存成功", Toast.LENGTH_SHORT).show(); // 显示保存成功的提示
                    finish(); // 结束当前活动
                } else {
                    Toast.makeText(CreateListActivity.this, "保存失败", Toast.LENGTH_SHORT).show(); // 显示保存失败的提示
                    finish(); // 结束当前活动
                }
            }
        });
    }

    /**
     * 保存待办，并写入数据库
     */
    private Boolean save() {
        UsersList usersList = new UsersList(); // 创建一个用户列表对象
        usersList.setCreateTime(currentTime); // 设置创建时间
        usersList.setUsername(receivedUsername); // 设置用户名
        usersList.setContent(content); // 设置待办内容
        if (receivedUsername != null && currentTime != null) { // 如果用户名和时间不为空
            usersListDao.insertList(usersList); // 将用户列表对象插入数据库
            return true; // 返回保存成功
        }
        return false; // 返回保存失败
    }
}