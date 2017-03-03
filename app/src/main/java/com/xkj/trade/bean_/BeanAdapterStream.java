package com.xkj.trade.bean_;

/**
 * Created by huangsc on 2017-03-01.
 * TODO:流动表adapter实体类
 */

public class BeanAdapterStream {
    /**
     * 1是开仓，2是平仓
     */
    String order;
    int status;
    String prices;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    String tp;
    String time;
    String symbol;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    int imageId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BeanAdapterStream(String order,String symbol,int status, String prices, String tp, String time,int imageId) {
        this.status = status;
        this.symbol=symbol;
        this.prices = prices;
        this.order=order;
        this.tp = tp;
        this.time=time;
        this.imageId=imageId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }
}

