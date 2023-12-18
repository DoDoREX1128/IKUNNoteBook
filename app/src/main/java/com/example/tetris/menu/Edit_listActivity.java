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

import com.example.tetris.dao.UsersListDao;
import com.example.tetris.db.DataBase;
import com.example.tetris.entity.UsersDay;
import com.example.tetris.entity.UsersList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Edit_listActivity extends AppCompatActivity {

    private ImageView imageButtonImageView; // 图片按钮
    private TextView textView; // 文本视图
    private EditText editText; // 编辑文本框
    private String content; // 内容
    private String currentTime; // 当前时间
    private String receivedUsername; // 接收到的用户名
    private DataBase db; // 数据库
    private UsersListDao usersListDao; // 用户列表Dao
    private UsersList usersList; // 用户列表
    private String time; // 时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        init();
    }

    private void init() {
        imageButtonImageView = findViewById(R.id.image_ok);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);

        // 获取传递过来的Username
        receivedUsername = getIntent().getStringExtra("Username");
        time=getIntent().getStringExtra("Date");

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        // 创建一个格式化时间的对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用格式化对象将时间转换为字符串
        currentTime = dateFormat.format(calendar.getTime());
        // 在TextView中显示当前时间
        textView.setText(currentTime);

        //数据库初始化
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "mydb")
//                       .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        usersListDao = db.usersListDao();

        //EditText初始文字
        usersList=usersListDao.getDiaryByUsernameAndTime(receivedUsername,time);
        if (usersList!=null) {
            editText.setText(usersList.getContent());
        }

        // 设置图片按钮的点击事件
        imageButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content=editText.getText().toString();
                if (save()) {
                    Toast.makeText(Edit_listActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(Edit_listActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * 保存待办，并写入数据库
     */
    private Boolean save() {
        UsersList usersList = new UsersList();
        usersList.setCreateTime(time);
        usersList.setUsername(receivedUsername);
        usersList.setContent(content);
        if (receivedUsername != null && currentTime != null) {
            usersListDao.updateList(usersList);
            return true;
        }
        return false;
    }
}