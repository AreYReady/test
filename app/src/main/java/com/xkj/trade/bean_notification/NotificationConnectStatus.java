package com.xkj.trade.bean_notification;

/**
 * Created by huangsc on 2017-02-28.
 * TODO:用来通知连接状态
 */

public class NotificationConnectStatus {
    public NotificationConnectStatus(ConnectStatus connectStatus) {
        mConnectStatus = connectStatus;
    }


    public ConnectStatus getConnectStatus() {
        return mConnectStatus;
    }

    ConnectStatus mConnectStatus;
   public  enum  ConnectStatus{
        YES,
        NO
    }
}
