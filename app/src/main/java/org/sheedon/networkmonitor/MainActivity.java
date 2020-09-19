package org.sheedon.networkmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.sheedon.networklibrary.NetworkClient;
import org.sheedon.networklibrary.NetworkContact;
import org.sheedon.networklibrary.NetworkListener;

public class MainActivity extends AppCompatActivity implements NetworkListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkClient.getInstance().initConfig(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkClient.getInstance().addListener(this);
    }

    public void onTouchClick(View view) {
        int asuInfo = NetworkClient.getInstance().getAsuInfo();
        int networkState = NetworkClient.getInstance().getNetworkState();
        int networkType = NetworkClient.getInstance().getNetworkType();

        onState(networkState, networkType, asuInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkClient.getInstance().removeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkClient.getInstance().destroy();
    }

    @Override
    public void onState(int state, int type, int signal) {
        switch (state) {
            case NetworkContact.TYPE_ETHERNET:
                Log.v("SXD", "有线");
                break;
            case NetworkContact.TYPE_WIFI:
                Log.v("SXD", "WIFI");
                break;
            case NetworkContact.TYPE_MOBILE:
                Log.v("SXD", "蜂窝网络");
                break;
            case NetworkContact.TYPE_NONE_NET:
                Log.v("SXD", "无网络");
                break;
        }

        switch (type) {
            case NetworkContact.TYPE_SUB_2G:
                Log.v("SXD", "2G");
                break;
            case NetworkContact.TYPE_SUB_3G:
                Log.v("SXD", "3G");
                break;
            case NetworkContact.TYPE_SUB_4G:
                Log.v("SXD", "4G");
                break;
            case NetworkContact.TYPE_SUB_5G:
                Log.v("SXD", "5G");
                break;
            case NetworkContact.TYPE_NONE_NET:
                Log.v("SXD", "无网络");
                break;
        }

        switch (signal) {
            case NetworkContact.STATE_NET_GREAT:
                Log.v("SXD", "网络很好");
                break;
            case NetworkContact.STATE_NET_GOOD:
                Log.v("SXD", "网络不错");
                break;
            case NetworkContact.STATE_NET_MODERATE:
                Log.v("SXD", "网络还行");
                break;
            case NetworkContact.STATE_NET_POOR:
                Log.v("SXD", "网络很差");
                break;
            case NetworkContact.STATE_NET_NONE:
                Log.v("SXD", "网络错误");
                break;
            case NetworkContact.STATE_NET_NOT_FIND:
                Log.v("SXD", "未获取到");
                break;
        }
    }
}