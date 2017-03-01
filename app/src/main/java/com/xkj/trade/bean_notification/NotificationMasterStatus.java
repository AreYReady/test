package com.xkj.trade.bean_notification;

/**
 * Created by huangsc on 2017-02-28.
 * TODO:用来作为通知高手排名关注变化的实体类
 */

public class NotificationMasterStatus {
    String login;int fstatus;int status;
    public NotificationMasterStatus(String login, int fstatus, int status){
        this.fstatus=fstatus;
        this.status=status;
        this.login=login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getFstatus() {
        return fstatus;
    }

    public void setFstatus(int fstatus) {
        this.fstatus = fstatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
