package org.sheedon.networklibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 网络客户端
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/9/15 2:12 PM
 */
public class NetworkClient {

    private static volatile NetworkClient instance;

    private TelephonyManager manager;
    private ConnectivityManager connectivityManager;
    private PhoneStateListener phoneStateListener;

    // 网络状态：有线，wifi，蜂窝网络
    private int networkState;
    // 蜂窝网络状态 2G 3G 4G 5G
    private int networkType;
    // 网络信号的值
    private int asuInfo = -1000;

    private long lastTime;

    private NetBroadcastReceiver netBroadcastReceiver;

    private Set<NetworkListener> listeners = new LinkedHashSet<>();

    public static NetworkClient getInstance() {
        if (instance == null) {
            synchronized (NetworkClient.class) {
                if (instance == null) {
                    instance = new NetworkClient();
                }
            }
        }
        return instance;
    }

    private NetworkClient() {

    }

    public void initConfig(Application application) {
        if (manager == null) {
            manager = (TelephonyManager)
                    application.getSystemService(Context.TELEPHONY_SERVICE);
        }

        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        if (phoneStateListener == null) {
            phoneStateListener = create();

            manager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                    | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        }

        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            netBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            application.registerReceiver(netBroadcastReceiver, filter);
        }


        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            convertNetworkType(manager.getNetworkType());
        }
    }

    public void addListener(NetworkListener listener) {
        if (listener == null)
            return;

        listeners.add(listener);
    }

    public void removeListener(NetworkListener listener) {
        if (listener == null)
            return;

        listeners.remove(listener);
    }

    public void updateNetState() {
        convertNetworkState();
    }

    /**
     * 转换网络类型
     *
     * @param networkType 蜂窝网络状态
     */
    private void convertNetworkType(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                this.networkType = NetworkContact.TYPE_SUB_2G;
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                this.networkType = NetworkContact.TYPE_SUB_3G;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                this.networkType = NetworkContact.TYPE_SUB_4G;
                break;
            case TelephonyManager.NETWORK_TYPE_NR:
                this.networkType = NetworkContact.TYPE_SUB_5G;
                break;
            default:
                this.networkType = NetworkContact.TYPE_NONE_NET;
                break;
        }
    }

    /**
     * 转换网络状态
     */
    private void convertNetworkState() {
        if (connectivityManager == null)
            return;

        //获取ConnectivityManager对象对应的NetworkInfo对象
        //获取WIFI连接的信息
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //获取移动数据连接的信息
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //获取有线信息
        NetworkInfo ethernetNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);


        if (ethernetNetworkInfo != null && ethernetNetworkInfo.isConnected()) {
            networkState = NetworkContact.TYPE_ETHERNET;
            noticeNetwork();
            return;
        }

        if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
            networkState = NetworkContact.TYPE_WIFI;
            noticeNetwork();
            return;
        }

        if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected()) {
            networkState = NetworkContact.TYPE_MOBILE;
            noticeNetwork();
            return;
        }

        networkState = NetworkContact.TYPE_NONE_NET;
        noticeNetwork();
    }

    // 转换网络信号
    private int convertNetSignal() {
        if (asuInfo == -1000)
            return NetworkContact.STATE_NET_NOT_FIND;

        if (asuInfo < 0 || asuInfo >= 99)
            return NetworkContact.STATE_NET_NONE;
        else if (asuInfo >= 16)
            return NetworkContact.STATE_NET_GREAT;
        else if (asuInfo >= 8)
            return NetworkContact.STATE_NET_GOOD;
        else if (asuInfo >= 4)
            return NetworkContact.STATE_NET_MODERATE;
        else
            return NetworkContact.STATE_NET_POOR;

    }

    /**
     * 获取网络状态
     */
    public int getNetworkState() {
        return networkState;
    }

    /**
     * 获取网络信号
     */
    public int getAsuInfo() {
        return asuInfo;
    }

    public int getNetSignal(){
        return convertNetSignal();
    }

    /**
     * 获取网络类型
     */
    public int getNetworkType() {
        return networkType;
    }

    public void queryCurrentNetworkState() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime <= 1000) {
            return;
        }

        lastTime = nowTime;

        convertNetworkState();
    }

    private void noticeNetwork() {
        if (listeners == null || listeners.isEmpty())
            return;

        for (NetworkListener listener : listeners) {
            if (listener == null) {
                continue;
            }

            listener.onState(networkState, networkType, convertNetSignal());
        }
    }

    /**
     * 创建手机状态监听器
     */
    private PhoneStateListener create() {
        return new PhoneStateListener() {

            //这个是我们的主角，就是获取对应网络信号强度
            @SuppressLint("MissingPermission")
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                //这个dbm 是2G和3G信号的值
                if (asuInfo != signalStrength.getGsmSignalStrength()) {
                    asuInfo = signalStrength.getGsmSignalStrength();
                    noticeNetwork();
                }
            }

            // 监听对数据连接状态（蜂窝）的更改。
            @Override
            public void onDataConnectionStateChanged(int state, int networkType) {
                super.onDataConnectionStateChanged(state, networkType);

                convertNetworkType(networkType);
                handleState(state);
            }

            private void handleState(int state) {
                switch (state) {
                    case TelephonyManager.DATA_CONNECTED:
                        convertNetworkState();
                        break;
                    default:
                        networkState = NetworkContact.TYPE_NONE_NET;
                        noticeNetwork();
                        break;
                }
            }
        };
    }


    /**
     * 销毁
     */
    public void destroy() {
        if (listeners != null) {
            listeners.clear();
        }



        listeners = null;
        if (phoneStateListener != null && manager != null) {
            manager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        phoneStateListener = null;
        connectivityManager = null;
        manager = null;
        instance = null;
    }
}
