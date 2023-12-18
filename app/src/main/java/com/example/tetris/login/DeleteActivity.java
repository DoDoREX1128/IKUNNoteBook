package com.example.tetris.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.room.Room;

import com.example.tetris.R;
import com.example.tetris.dao.UserDao;
import com.example.tetris.db.DataBase;
import com.example.tetris.entity.User;

// 删除用户信息的活动页面
public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text_title;  // 标题文本视图
    private Button btn_confirm, btn_cancel;  // 确认按钮和取消按钮
    private EditText edit_account, edit_password;  // 账号和密码输入框
    private DataBase db;  // 数据库对象
    private UserDao userDao;  // 用户数据访问对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        init();
    }

    // 初始化方法
    private void init() {
        text_title = (TextView) findViewById(R.id.text_title);  // 获取标题文本视图
        // 使用字体
        // Typeface typeface = ResourcesCompat.getFont(this, R.font.zhao);
        // text_title.setTypeface(typeface);

        edit_account = (EditText) findViewById(R.id.edit_account);  // 获取账号输入框
        // 获取数据库对象,且对比Data.db中的username是否存在，存在则进行删除操作
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus();
                }
                return false;
            }
        });

        edit_password = (EditText) findViewById(R.id.edit_password);  // 获取密码输入框
        // 获取数据库对象,且对比Data.db中的password是否存在，存在则进行删除操作
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                }
                return false;
            }
        });

        btn_confirm = (Button) findViewById(R.id.btn_confirm);  // 获取确认按钮
        btn_confirm.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);  // 获取取消按钮
        btn_cancel.setOnClickListener(this);
        // 数据库初始化
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        userDao = db.userDao();
    }

    @Override
    public void onClick(View v) {
        // 获取输入的用户名和密码
        String account = edit_account.getText().toString();
        String password = edit_password.getText().toString();
        switch (v.getId()) {
            case R.id.btn_confirm:
                // 对比username和password是否存在，存在则进行删除操作
                User user = userDao.findUser(account, password);
                if (user != null) {
                    userDao.delete(user);
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel:  // 清空输入框
                edit_account.setText("");
                edit_password.setText("");
                finish();
                break;
        }
    }
}