package com.example.tetris.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tetris.R;
import com.example.tetris.adapt.DayAdapter;
import com.example.tetris.adapt.DayItem;
import com.example.tetris.dao.UsersDayDao;
import com.example.tetris.db.DataBase;
import com.example.tetris.entity.UsersDay;
import com.example.tetris.receiver.FinishReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageView circularImageButton;  // 圆形按钮
    private TextView centerText;  // 中间文本
    private ImageView rightTopButton;  // 右上角按钮
    private CalendarView calendarView;  // 日历视图
    private ListView listView;  // 列表视图
    private ImageView nextImageView;  // 下一个图像视图
    private TextView username;  // 用户名文本视图
    private String date;  // 日期
    private DataBase db;  // 数据库
    private UsersDayDao usersDayDao;  // 用户日记数据访问对象
    private List<UsersDay> usersDays;  // 用户日记列表
    private String receivedUsername;  // 接收到的用户名
    private DayAdapter adapter;  // 日记适配器
    private ArrayList<DayItem> itemList;  // 日记项列表


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FinishReceiver finishReceiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter("finish_activity");

        registerReceiver(finishReceiver, intentFilter);
        init();
    }

    private void init(){
        circularImageButton = findViewById(R.id.circular_image_button);
        centerText = findViewById(R.id.center_text);
        rightTopButton = findViewById(R.id.right_top_button);
        calendarView = findViewById(R.id.calendar_view);
        listView = findViewById(R.id.list_view);
        nextImageView = findViewById(R.id.imageView);
        username=findViewById(R.id.textView3);

        //数据库初始化
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "mydb")
//                       .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        usersDayDao = db.usersDayDao();

        usersDays=new ArrayList<>();
        itemList = new ArrayList<>();

        // 获取传递过来的Username
        receivedUsername = getIntent().getStringExtra("Username");
        // 将Username设置到TextView中
        username.setText(receivedUsername);

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, ListActivity.class);
                intent.putExtra("Username", receivedUsername);
                startActivity(intent);
            }
        });
        rightTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, CreateDayActivity.class);
                intent.putExtra("Username", receivedUsername);
                startActivityForResult(intent, 100);

            }

        });

        circularImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, PersonActivity.class);
                intent.putExtra("Username", receivedUsername);
                startActivity(intent);
            }
        });
        initListView();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 在这里获取选定的日期
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                date = dateFormat.format(selectedDate.getTime());
                Log.d("flag", "onSelectedDayChange: "+date);
                // 将选定的日期用作需要的操作
                updateListViewData(date);

                Log.d("flag", "onSelectedDayChange: "+usersDays.size());


            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                // 获取从新建页面返回的新数据
                initListView();
                // 将新数据添加到主页的 ListView 中

            }
        }
        else if (resultCode==RESULT_OK&&data.getStringExtra("Edit").equals("edit")){
            Log.d("flag", "onActivityResult: ");
            initListView();
        }
    }

    private void initListView() {
        Calendar selectedDate = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(selectedDate.getTime());

        // 将选定的日期用作需要的操作
        updateListViewData(date);
    }


    private void updateListViewData(String date) {
        usersDays=usersDayDao.getDiariesForSelectedDate(receivedUsername,date);
        if(itemList!=null) itemList.clear();
        for (UsersDay usd:usersDays
        ) {
            itemList.add(new DayItem(usd.getTitle(),usd.getContent(),usd.getCreateTime(),usd.getUsername()));
        }
        adapter = new DayAdapter(HomeActivity.this, itemList);
        listView.setAdapter(adapter);


    }
    @Override
    public void onBackPressed() {
        // 弹出确认对话框
        new AlertDialog.Builder(this)
                .setTitle("确认退出")
                .setMessage("您确定要退出应用吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击确认退出，执行退出操作

                      finish();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击取消，关闭对话框
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initListView();
    }
}