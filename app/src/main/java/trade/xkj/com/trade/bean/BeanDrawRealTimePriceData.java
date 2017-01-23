package trade.xkj.com.trade.bean;

import android.graphics.Color;

/**
 * Created by huangsc on 2017-01-12.
 * TODO:画实时金额的实体类
 */

public class BeanDrawRealTimePriceData {
    private int y;
    private String realTimePrice;
    //暂时默认
    private int color= Color.RED;

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getRealTimePrice() {
        return realTimePrice;
    }

    public void setRealTimePrice(String realTimePrice) {
        this.realTimePrice = realTimePrice;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
