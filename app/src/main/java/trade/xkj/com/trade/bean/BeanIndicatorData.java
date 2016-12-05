package trade.xkj.com.trade.bean;

/**
 * Created by huangsc on 2016-12-05.
 * TODO:
 */

public class BeanIndicatorData {
    private int imageResource;
    private String symbolTag;

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getSymbolTag() {
        return symbolTag;
    }

    public void setSymbolTag(String symbolTag) {
        this.symbolTag = symbolTag;
    }

    public String getLeftString() {
        return leftString;
    }

    public void setLeftString(String leftString) {
        this.leftString = leftString;
    }

    public String getRightString() {
        return RightString;
    }

    public void setRightString(String rightString) {
        RightString = rightString;
    }

    private String leftString;
    private String RightString;
}
