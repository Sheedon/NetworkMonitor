package org.sheedon.networklibrary;

/**
 * 网络监听器
 *
 * public void onState(int state, int type, int signal) {
 *         switch (state) {
 *             case NetworkContact.TYPE_ETHERNET:
 *                 Log.v("SXD", "有线");
 *                 break;
 *             case NetworkContact.TYPE_WIFI:
 *                 Log.v("SXD", "WIFI");
 *                 break;
 *             case NetworkContact.TYPE_MOBILE:
 *                 Log.v("SXD", "蜂窝网络");
 *                 break;
 *             case NetworkContact.TYPE_NONE_NET:
 *                 Log.v("SXD", "无网络");
 *                 break;
 *         }
 *
 *         switch (type) {
 *             case NetworkContact.TYPE_SUB_2G:
 *                 Log.v("SXD", "2G");
 *                 break;
 *             case NetworkContact.TYPE_SUB_3G:
 *                 Log.v("SXD", "3G");
 *                 break;
 *             case NetworkContact.TYPE_SUB_4G:
 *                 Log.v("SXD", "4G");
 *                 break;
 *             case NetworkContact.TYPE_SUB_5G:
 *                 Log.v("SXD", "5G");
 *                 break;
 *             case NetworkContact.TYPE_NONE_NET:
 *                 Log.v("SXD", "无网络");
 *                 break;
 *         }
 *
 *         switch (signal) {
 *             case NetworkContact.STATE_NET_GREAT:
 *                 Log.v("SXD", "网络很好");
 *                 break;
 *             case NetworkContact.STATE_NET_GOOD:
 *                 Log.v("SXD", "网络不错");
 *                 break;
 *             case NetworkContact.STATE_NET_MODERATE:
 *                 Log.v("SXD", "网络还行");
 *                 break;
 *             case NetworkContact.STATE_NET_POOR:
 *                 Log.v("SXD", "网络很差");
 *                 break;
 *             case NetworkContact.STATE_NET_NONE:
 *                 Log.v("SXD", "网络错误");
 *                 break;
 *             case NetworkContact.STATE_NET_NOT_FIND:
 *                 Log.v("SXD", "未获取到");
 *                 break;
 *         }
 *     }
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/9/19 1:00 PM
 */
public interface NetworkListener {

    void onState(int state, int type, int signal);
}
