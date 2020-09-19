package org.sheedon.networklibrary;

/**
 * 网络描述信息
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/9/15 11:07 AM
 */
public interface NetworkContact {

    // 有线
    int TYPE_ETHERNET = 0x101;
    // WIFI
    int TYPE_WIFI = 0x102;
    // 蜂窝网络
    int TYPE_MOBILE = 0x103;
    // 无网络
    int TYPE_NONE_NET = 0x104;

    // 2G信号
    int TYPE_SUB_2G = 0x1001;
    // 3G信号
    int TYPE_SUB_3G = 0x1002;
    // 4G信号
    int TYPE_SUB_4G = 0x1003;
    // 5G信号
    int TYPE_SUB_5G = 0x1004;


    // 网络很好
    int STATE_NET_GREAT = 0x2000;
    // 网络不错
    int STATE_NET_GOOD = 0x2001;
    // 网络还行
    int STATE_NET_MODERATE = 0x2002;
    // 网络很差
    int STATE_NET_POOR = 0x2003;
    // 网络错误
    int STATE_NET_NONE = 0x2004;
    // 未获取到
    int STATE_NET_NOT_FIND = 0x2005;

}
