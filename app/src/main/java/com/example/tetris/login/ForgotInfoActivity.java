package com.example.tetris.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

public class ForgotInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text_title; // 标题文本视图
    private EditText edit_account, edit_password, edit_password_again; // 账号、密码、再次输入密码的编辑文本框
    private Button btn_revise, btn_cancel; // 修改按钮、取消按钮
    private DataBase db; // 数据库对象
    private UserDao userDao; // 用户数据访问对象
    private boolean flag; // 标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_info);
        init();
    }

    private void init() {
        flag = false;
        text_title = (TextView) findViewById(R.id.text_title); // 获取标题文本视图
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "mydb") // 获取数据库对象
                .allowMainThreadQueries()
                .build();
        userDao = db.userDao(); // 获取用户数据访问对象

        edit_account = (EditText) findViewById(R.id.edit_account); // 获取账号编辑文本框
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus(); // 清除焦点
                }
                return false;
            }
        });

        edit_password = (EditText) findViewById(R.id.edit_password); // 获取密码编辑文本框
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String s = v.getText().toString();
                    System.out.println(" v: ****** v :" + s.length()); // 打印密码长度
                    if (s.length() >= 6) {
                        System.out.println(" ****** s :" + s.length()); // 打印密码长度
                        edit_password.clearFocus(); // 清除焦点
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edit_password.getWindowToken(), 0); // 隐藏软键盘
                    } else {
                        Toast.makeText(ForgotInfoActivity.this, "密码设置最少为6位！", Toast.LENGTH_SHORT).show(); // 提示密码长度不足
                    }
                }
                return false;
            }
        });

        edit_password_again = (EditText) findViewById(R.id.edit_password_again); // 获取再次输入密码编辑文本框
        edit_password_again.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password_again.clearFocus(); // 清除焦点
                    InputMethodManager im =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(edit_password_again.getWindowToken(), 0); // 隐藏软键盘
                }
                return false;
            }
        });

        btn_revise = (Button) findViewById(R.id.btn_revise); // 获取修改按钮
        btn_revise.setOnClickListener(this); // 设置点击事件
        btn_cancel = (Button) findViewById(R.id.btn_cancel); // 获取取消按钮
        btn_cancel.setOnClickListener(this); // 设置点击事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_revise: // 点击修改按钮
                if (checkUser(edit_account.getText().toString())) { // 检查用户是否存在
                    if (edit_password_again.getText().toString().length() < 6) { // 检查密码长度是否足够
                        Toast.makeText(ForgotInfoActivity.this, "密码设置最少为6位！", Toast.LENGTH_SHORT).show(); // 提示密码长度不足
                    } else {
                        revisePassword(edit_account.getText().toString(), edit_password_again.getText().toString()); // 修改密码
                        Toast.makeText(ForgotInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show(); // 提示修改成功
                        finish(); // 结束当前活动
                    }
                }
                break;
            case R.id.btn_cancel: // 点击取消按钮
                finish(); // 结束当前活动
                break;
        }
    }

    /**
     * 更新密码
     *
     * @param value    用户名
     * @param password 密码
     */
    private void revisePassword(String value, String password) {
        User user = userDao.findUserName(value); // 根据用户名查找用户
        user.password = password; // 更新密码
        userDao.update(user); // 更新用户信息
    }

    /**
     * 检验用户名是否已经注册且原密码是否正确
     */
    private boolean checkUser(String value) {
        User user = userDao.findUserName(value); // 根据用户名查找用户

        if (user == null) { // 用户不存在
            Toast.makeText(ForgotInfoActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show(); // 提示用户名不存在
            return false;
        } else if (!user.password.equals(edit_password.getText().toString())) { // 原密码错误
            Toast.makeText(ForgotInfoActivity.this, "原密码错误！", Toast.LENGTH_SHORT).show(); // 提示原密码错误
            return false;
        } else {
            return true; // 用户名存在且原密码正确
        }
    }
}