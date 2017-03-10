package com.xkj.trade.bean_notification;

/**
 * Created by huangsc on 2017-02-28.
 * TODO:用来通知悬浮按钮消失
 */

public class NotificationFloat {
  boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public NotificationFloat(boolean status) {
        this.status = status;
    }
}
