package com.example.tetris.receiver;  // 声明包名

import android.app.Activity;  // 导入android.app.Activity类
import android.content.BroadcastReceiver;  // 导入android.content.BroadcastReceiver类
import android.content.Context;  // 导入android.content.Context类
import android.content.Intent;  // 导入android.content.Intent类

public class FinishReceiver extends BroadcastReceiver {  // 定义一个名为FinishReceiver的类，继承自BroadcastReceiver类

    @Override
    public void onReceive(Context context, Intent intent) {  // 重写BroadcastReceiver类的onReceive方法，接收广播
        // 在接收到广播时执行 finish 操作
        if (context instanceof Activity) {  // 判断context是否为Activity的实例
            ((Activity) context).finish();  // 如果是，则调用finish()方法关闭当前Activity
        }
    }
}
