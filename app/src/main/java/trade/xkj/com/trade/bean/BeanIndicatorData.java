package trade.xkj.com.trade.bean;

/**
 * Created by huangsc on 2016-12-05.
 * TODO:
 */

public class BeanIndicatorData {
    private int imageResource;
    private String symbolTag;

    public BeanIndicatorData(){};
    public BeanIndicatorData(int imageResource,String symbolTag,String leftString,String rightString){
        this.imageResource = imageResource;
        this.symbolTag=symbolTag;
        this.leftString=leftString;
        this.rightString=rightString;
    }
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
        return rightString;
    }

    public void setRightString(String rightString) {
        this.rightString = rightString;
    }

    private String leftString;
    private String rightString;
}
