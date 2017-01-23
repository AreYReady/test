package trade.xkj.com.trade.bean;

/**
 * Created by huangsc on 2016-12-02.
 * TODO:
 */

public class BeanDrawPriceData {
    Integer priceY;
    String priceString;
    int color=0;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Integer getPriceY() {
        return priceY;
    }

    public void setPriceY(Integer priceY) {
        this.priceY = priceY;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }
}
