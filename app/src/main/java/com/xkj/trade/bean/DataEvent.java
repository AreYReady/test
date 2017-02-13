package com.xkj.trade.bean;

/**
 * @author huangsc
 * @date 2016-11-23
 * 数据message
 */
public class DataEvent {
    private String result;
    private int type;//0为实时数据，1为历史数据,2为下单结果和请求

    public DataEvent(String result, int type) {
        this.result = result;
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public int getType() {
        return type;
    }
}
