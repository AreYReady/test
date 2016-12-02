package trade.xkj.com.trade.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author xjunda
 * @date 2016-08-09
 * 小数点前位数不够7位，则补充小数点到7位
 */
public class MoneyUtil {
    public static String moneyFormat(double money) {
        String strMoney = (int) money + "";
        DecimalFormat df = null;
        StringBuilder format = new StringBuilder("0.0");
        if (strMoney.length() < 7) {
            for (int i = 7 - strMoney.length(); i > 2; i--) {//0和小数点已经是2位
                format.append("0");
            }
            df = new DecimalFormat(format.toString());
        } else {
            return strMoney;
        }
        return df.format(money);
    }

    public static String moneyFormat(String money) {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(Double.valueOf(money));
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param a 被加数
     * @param b 加数
     * @return 两个参数的和
     */
    public static double addPrice(double a, double b) {
        BigDecimal aDecimal = new BigDecimal(a + "");
        BigDecimal bDecimal = new BigDecimal(b + "");
        return aDecimal.add(bDecimal).doubleValue();
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param a 被加数
     * @param b 加数
     * @return 两个参数的和
     */
    public static String addPrice(String a, String b) {
        BigDecimal aDecimal = new BigDecimal(a);
        BigDecimal bDecimal = new BigDecimal(b);
        return aDecimal.add(bDecimal).toString();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param a 被减数
     * @param b 减数
     * @return 两个参数的差
     */
    public static double subPrice(String a, String b) {
        BigDecimal aDecimal = new BigDecimal(a);
        BigDecimal bDecimal = new BigDecimal(b);
        return aDecimal.subtract(bDecimal).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double subPrice(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param a 被乘数
     * @param b 乘数
     * @return 两个参数的积
     */
    public static double mulPrice(double a, double b) {
        BigDecimal aDecimal = new BigDecimal(a + "");
        BigDecimal bDecimal = new BigDecimal(b + "");
        return aDecimal.multiply(bDecimal).doubleValue();
    }
    public static String mulPriceToString(double a, double b) {
        BigDecimal aDecimal = new BigDecimal(a + "");
        BigDecimal bDecimal = new BigDecimal(b + "");
        return aDecimal.multiply(bDecimal).toString();
    }

    /**
     * 价钱相加
     *
     * @param a
     * @param b
     * @param digits 小数位
     * @return
     */
    public static double addPrice(String a, String b, int digits) {
        BigDecimal aDecimal = new BigDecimal(moneyFormat(a));
        BigDecimal bDecimal = new BigDecimal(moneyFormat(b));
        return aDecimal.add(bDecimal).movePointLeft(digits).doubleValue();
    }

    public static String addPriceString(String a, String b, int digits) {
        BigDecimal aDecimal = new BigDecimal(moneyFormat(a));
        BigDecimal bDecimal = new BigDecimal(moneyFormat(b));
        return aDecimal.add(bDecimal).movePointLeft(digits).toString();
    }

//    @NonNull
//    /**
//     * 最后3个字放大
//     */
//    public static SpannableString getRealTimePriceTextBig(Context context, String realTimeText) {
//        SpannableString spannableString = new SpannableString(realTimeText);
//        if(spannableString.length()>3){
//            spannableString.setSpan(new TextAppearanceSpan(context, R.style.realTimeLeftText),
//                    0, spannableString.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new TextAppearanceSpan(context, R.style.realTimeRightText),
//                    spannableString.length() - 3, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        return spannableString;
//    }
}
