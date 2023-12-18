package com.example.tetris.others;  // 声明包名

import androidx.appcompat.app.AppCompatActivity;  // 导入AppCompatActivity类

import android.os.Bundle;  // 导入Bundle类

import com.example.tetris.R;  // 导入R类，用于引用资源

public class AboutUsActivity extends AppCompatActivity {  // 定义AboutUsActivity类，继承自AppCompatActivity类

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // 重写onCreate方法
        super.onCreate(savedInstanceState);  // 调用父类的onCreate方法
        setContentView(R.layout.activity_about_us);  // 设置布局文件为activity_about_us
    }
}