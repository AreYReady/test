package com.xkj.trade.bean_notification;

/**
 * Created by huangsc on 2017-03-07.
 * TODO:
 */

public class NotificationClosePosition {
    public NotificationClosePosition(int order) {
        this.order = order;
    }

    int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
