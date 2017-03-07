package com.xkj.trade.bean_notification;

/**
 * Created by huangsc on 2017-03-07.
 * TODO:
 */

public class NotificationEditPendingPosition {
    int order;
    String sl;
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    String tp;
}
