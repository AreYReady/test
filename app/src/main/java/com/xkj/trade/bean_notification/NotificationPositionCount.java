package com.xkj.trade.bean_notification;

/**
 * Created by huangsc on 2017-02-28.
 * TODO:用来作为通知高手排名关注变化的实体类
 */

public class NotificationPositionCount {
    int count;

    public NotificationPositionCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
