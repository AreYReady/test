package com.xkj.trade.bean_;

/**
 * Created by huangsc on 2017-02-03.
 * TODO:基础的相应实体父类
 */

public class BeanBaseResponse {
    int status;
    String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
