package com.example.tetris.menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tetris.R;
import com.example.tetris.dao.UsersDayDao;
import com.example.tetris.db.DataBase;
import com.example.tetris.entity.UsersDay;
import com.example.tetris.function.Function;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateDayActivity extends AppCompatActivity {

    private EditText titleCreateEditText;  // 标题输入框
    private ImageView imageOk;  // 图片确认按钮
    private ImageView imageImageView;  // 图片展示
    private ImageView photographImageView;  // 相机按钮
    private TextView textView;  // 时间展示
    private LinearLayout imageContainer;  // 图片容器
    private EditText contentEditText;  // 内容输入框
    private String receivedUsername;  // 接收到的用户名
    private DataBase db;  // 数据库对象
    private UsersDayDao usersDayDao;  // 用户日记数据访问对象
    private byte[] image;  // 图片字节数组
    private ArrayList<byte[]> images;  // 图片字节数组列表
    private byte[] graph;  // 图片字节数组
    private String title;  // 标题
    private String content;  // 内容

    private String currentTime;  // 当前时间
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createday);
        init();
    }

    private void init() {
        titleCreateEditText = findViewById(R.id.title_create);
        imageOk = findViewById(R.id.image_ok);
        textView = findViewById(R.id.textView);
        contentEditText = findViewById(R.id.content);
        imageImageView = findViewById(R.id.imageView3);
        photographImageView = findViewById(R.id.imageView2);
//        imageContainer = findViewById(R.id.imageContainer);

        images = new ArrayList<>();

        // 获取传递过来的Username
        receivedUsername = getIntent().getStringExtra("Username");

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        // 创建一个格式化时间的对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用格式化对象将时间转换为字符串
        currentTime = dateFormat.format(calendar.getTime());
        // 在TextView中显示当前时间
        textView.setText(currentTime);

        title = titleCreateEditText.getText().toString();
        content = contentEditText.getText().toString();


        //数据库初始化
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "mydb")
//                       .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        usersDayDao = db.usersDayDao();

        imageOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleCreateEditText.getText().toString();
                content = contentEditText.getText().toString();

                if (images.size() > 3) {
                    images.subList(3, images.size()).clear();


                    Log.d("decode", "onClick: "+images.size());
                    Toast.makeText(CreateDayActivity.this, "最多存在三张图片!", Toast.LENGTH_SHORT).show();
                } else {
//                    content=deleteOther();
                    intent = new Intent();
                    setResult(RESULT_OK, intent);
                    if (save()) {
                        Toast.makeText(CreateDayActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateDayActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        setImage();
    }

    /**
     * 点击相册
     */
    private void setImage() {
        // 通过按钮点击事件启动相册应用
        imageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        // 处理相册应用返回的结果
        contentEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    SpannableStringBuilder builder = (SpannableStringBuilder) contentEditText.getText();
                    ImageSpan[] imageSpans = builder.getSpans(0, builder.length(), ImageSpan.class);
                    int cursorPosition = contentEditText.getSelectionStart();

                    for (ImageSpan span : imageSpans) {
                        int start = builder.getSpanStart(span);
                        int end = builder.getSpanEnd(span);

                        if (cursorPosition >= start && cursorPosition <= end) {
                            // 删除对应的ImageSpan
                            builder.replace(start, end, "");
                            builder.removeSpan(span);
                            contentEditText.setText(builder);
                            contentEditText.setSelection(start); // 恢复光标位置
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // 使用选中的图片URI进行处理，显示在ImageView
            try {
                // 将URI转换为Bitmap对象
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                //将图片转成byte[]
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                image = byteArrayOutputStream.toByteArray();
                images.add(image);

                // 创建一个可编辑的SpannableStringBuilder对象，并将图片插入其中
                SpannableStringBuilder builder = new SpannableStringBuilder();

                // 压缩图片
                Bitmap compressedBitmap = compressImage(bitmap);

                // 调整图片大小
                int desiredWidth = 200; // 设置你想要的图片宽度
                int desiredHeight = 200; // 设置你想要的图片高度
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(compressedBitmap, desiredWidth, desiredHeight, false);

                // 将调整大小后的Bitmap对象转换为Drawable对象
                Drawable drawable = new BitmapDrawable(getResources(), scaledBitmap);

                // 创建ImageSpan对象，并将Drawable设置为可删除
                ImageSpan imageSpan = new ImageSpan(drawable);

                // 在可编辑的SpannableStringBuilder对象中插入ImageSpan
                builder.append("[image]");
                builder.setSpan(imageSpan, builder.length() - 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                // 获取EditText的当前光标位置
                int selectionStart = contentEditText.getSelectionStart();

                // 在当前光标位置插入图片
                Editable editable = contentEditText.getText();
                editable.insert(selectionStart, builder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);  // 压缩质量为20%
        byte[] compressedData = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(compressedData, 0, compressedData.length);
    }

    /**
     * 将日记保存，并写入数据库
     */
    private boolean save() {
        UsersDay usersDay = new UsersDay();
        usersDay.setTitle(title);
        usersDay.setUsername(receivedUsername);
        usersDay.setContent(content);
        usersDay.setCreateTime(currentTime);

        String string = Function.listToString(images);
        usersDay.setImageData(string);

        if (receivedUsername != null && currentTime != null) {
            usersDayDao.insertDiary(usersDay);
            return true;
        }
        return false;
    }
}