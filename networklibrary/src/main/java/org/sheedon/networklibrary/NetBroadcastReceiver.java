package org.sheedon.networklibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * java类作用描述
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/10/15 12:54 PM
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkClient.getInstance().updateNetState();
        }
    }

}
