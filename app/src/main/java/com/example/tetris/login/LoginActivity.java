package com.example.tetris.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.room.Room;

import com.example.tetris.R;
import com.example.tetris.dao.UserDao;
import com.example.tetris.db.DataBase;
import com.example.tetris.entity.User;
import com.example.tetris.menu.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView text_title; // 标题文本视图
    private EditText edit_account, edit_password; // 账号和密码的编辑文本框
    private TextView text_msg, text_delete; // 提示信息和删除文本视图
    private Button btn_login, btn_register; // 登录和注册按钮
    private ToggleButton openpwd; // 显示密码的开关按钮
    private boolean flag = false; // 密码是否可见的标志
    private String account, password; // 账号和密码
    private DataBase db; // 数据库对象
    private UserDao userDao; // 用户数据访问对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init(); // 初始化界面
    }

    private void init() {
        text_title = (TextView) findViewById(R.id.text_title); // 标题文本视图
        edit_account = (EditText) findViewById(R.id.edit_account); // 账号编辑文本框
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus();
                }
                return false;
            }
        });
        edit_password = (EditText) findViewById(R.id.edit_password); // 密码编辑文本框
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_password.getWindowToken(), 0);
                }
                return false;
            }
        });
        text_msg = (TextView) findViewById(R.id.text_msg); // 提示信息文本视图
        text_delete = (TextView) findViewById(R.id.text_delete); // 删除文本视图
        btn_login = (Button) findViewById(R.id.btn_login); // 登录按钮
        btn_register = (Button) findViewById(R.id.btn_register); // 注册按钮
        openpwd = (ToggleButton) findViewById(R.id.btn_openpwd); // 显示密码的开关按钮
        text_msg.setOnClickListener(this);
        text_delete.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        openpwd.setOnClickListener(this);

        // 初始化数据库
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "mydb")
//                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        userDao = db.userDao();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: // 登录按钮点击事件
                if (edit_account.getText().toString().trim().equals("") | edit_password.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo(); // 读取用户信息
                }
                break;
            case R.id.btn_register: // 注册按钮点击事件
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd: // 显示密码的开关按钮点击事件
                if (flag == true) { // 密码不可见
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setChecked(false);
                } else { // 密码可见
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setChecked(true);
                }
                break;
            case R.id.text_msg: // 提示信息文本视图点击事件
                Intent i = new Intent(LoginActivity.this, ForgotInfoActivity.class);
                startActivity(i);
                break;
            case R.id.text_delete: // 删除文本视图点击事件
                Intent intent1 = new Intent(LoginActivity.this, DeleteActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 读取用户信息
     */
    protected void readUserInfo() {
        if (login(edit_account.getText().toString(), edit_password.getText().toString())) { // 验证登录信息
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("Username", edit_account.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证登录信息
     */
    public boolean login(String username, String password) {
        User user = userDao.findUser(username, password);
        if (user != null) return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}