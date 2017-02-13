package com.xkj.trade.bean;

import com.xkj.trade.utils.DataUtil;

/**
 * Created by huangsc on 2016-12-05.
 * TODO:
 */

public class BeanIndicatorData {
    private int imageResource;
    private String symbol;

    public BeanIndicatorData(){};
    public BeanIndicatorData(String symbolTag,String askString,String bidString){
        this.symbol =symbolTag;
        this.ask =askString;
        this.bid =bidString;
        this.imageResource = DataUtil.getSymbolFlag(symbolTag);
    }
    public BeanIndicatorData(String symbolTag,String askString,String bidString,int askColor,int bidColor){
        this.symbol =symbolTag;
        this.ask =askString;
        this.bid =bidString;
        this.askColor=askColor;
        this.bidColor =bidColor;
        this.imageResource = DataUtil.getSymbolFlag(symbolTag);
    }
    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    private String ask;
    private String bid;

    public int getAskColor() {
        return askColor;
    }

    public void setAskColor(int askColor) {
        this.askColor = askColor;
    }

    private int askColor;
    private int bidColor;

    public int getBidColor() {
        return bidColor;
    }

    public void setBidColor(int bidColor) {
        this.bidColor = bidColor;
    }
}
