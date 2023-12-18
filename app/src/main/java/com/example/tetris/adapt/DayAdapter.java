package com.example.tetris.adapt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.room.Room;

import com.example.tetris.R;
import com.example.tetris.dao.UsersDayDao;
import com.example.tetris.db.DataBase;
import com.example.tetris.entity.UsersDay;
import com.example.tetris.menu.CreateDayActivity;
import com.example.tetris.menu.Edit_dayActivity;
import com.example.tetris.menu.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DayItem> itemList;
    private String username;
    private DataBase db;
    private UsersDayDao usersDayDao;

    // 构造函数，接收上下文和数据列表
    public DayAdapter(Context context, ArrayList<DayItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        }

        // 数据库初始化
        db = Room.databaseBuilder(context, DataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        usersDayDao = db.usersDayDao();

        // 获取列表项中的各个视图
        TextView titleTextView = convertView.findViewById(R.id.item_title);
        TextView descriptionTextView = convertView.findViewById(R.id.item_description);
        TextView dateTextView = convertView.findViewById(R.id.item_date);
        ImageButton button1 = convertView.findViewById(R.id.button1);
        ImageButton button2 = convertView.findViewById(R.id.button2);

        // 获取当前位置的数据项
        DayItem currentItem = itemList.get(position);

        // 设置视图中的数据
        titleTextView.setText(currentItem.getTitle());
        String str = currentItem.getDescription().toString();
        descriptionTextView.setText(str.substring(0, Math.min(str.length(), 10)));
        dateTextView.setText(currentItem.getDate());

        // 设置按钮的点击事件
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Edit_dayActivity.class);
                intent.putExtra("Date", currentItem.getDate());
                intent.putExtra("Username", currentItem.getUsername());
                context.startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 根据用户名和日期获取用户日记
                UsersDay usersDay = usersDayDao.getDiaryByUsernameAndTime(currentItem.getUsername(), currentItem.getDate());
                // 删除用户日记
                usersDayDao.deleteDiary(usersDay);
                ArrayList<DayItem> dayItems = new ArrayList<>();
                // 获取日期
                String date = currentItem.getDate().substring(0, Math.min(str.length(), 10));
                // 根据用户名和日期获取用户日记列表
                List<UsersDay> usersDays = usersDayDao.getDiariesForSelectedDate(currentItem.getUsername(), date);
                for (UsersDay usd : usersDays) {
                    dayItems.add(new DayItem(usd.getTitle(), usd.getContent(), usd.getCreateTime(), usd.getUsername()));
                }
                // 更新数据
                updateData(dayItems);
            }
        });

        return convertView;
    }

    // 更新数据
    public void updateData(ArrayList<DayItem> updatedItemList) {
        itemList.clear();
        itemList.addAll(updatedItemList);
        notifyDataSetChanged();
    }
}