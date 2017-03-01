package com.xkj.trade.bean_;

/**
 * Created by huangsc on 2017-03-01.
 * TODO:用来装社区的adpter实体类
 */

public class BeanAdapterComnunity {
    int imageId;
    String name;
    String tp;
    int tpColor;

    public BeanAdapterComnunity(int imageId, String name, String tp, int tpColor) {
        this.imageId = imageId;
        this.name = name;
        this.tp = tp;
        this.tpColor = tpColor;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public int getTpColor() {
        return tpColor;
    }

    public void setTpColor(int tpColor) {
        this.tpColor = tpColor;
    }
}
