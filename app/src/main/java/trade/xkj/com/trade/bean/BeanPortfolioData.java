package trade.xkj.com.trade.bean;

/**
 * Created by huangsc on 2016-12-30.
 * TODO:高手里面的投资数据
 */

public class BeanPortfolioData {
    public int getBeginAngle() {
        return beginAngle;
    }

    public void setBeginAngle(int beginAngle) {
        this.beginAngle = beginAngle;
    }

    private int beginAngle;
    private int sweepAngle;
    private String symbol;
    private String type;

    public int getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
