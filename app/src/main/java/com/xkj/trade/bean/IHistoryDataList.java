package com.xkj.trade.bean;

import java.util.List;

/**
 * Created by huangshunchao
 */
public interface IHistoryDataList {
    int getMsg_type();
    int getResult_code();
    int getCount();
    int getDigits();
    List<HistoryData> getItems();
    String getSymbol();
    int getPeriod();
    long getCacheTime();
    String getNowPrice();
    int getFlag();
    double[] getPrice();
}
