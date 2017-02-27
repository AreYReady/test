package com.xkj.trade.bean_;

/**
 * Created by huangsc on 2017-02-27.
 * TODO:高手仓位信息
 */

public class BeanMasterPosition {
    String symbol;

    public BeanMasterPosition(String symbol, String operater, String openPrice, String profit) {
        this.symbol = symbol;
        this.operater = operater;
        this.openPrice = openPrice;
        this.profit = profit;
    }

    String operater;
    String openPrice;
    String profit;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
