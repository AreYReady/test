package com.xkj.trade.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;

import com.xkj.trade.R;

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

    public static String moneyFormat(double numble, int digist) {
        BigDecimal b = new BigDecimal(numble);
        return String.valueOf(b.setScale(digist, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public static String moneyFormat(String money) {
        if(money.indexOf(".")>=0){
            if(money.length()-money.indexOf(".")>3){
                return money.substring(0,7);
            }else{
                return money;
            }
        }else{
            if(money.length()<=6)
                return money;
            else{
               return money.substring(0,6);
            }
        }
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

    public static double div(String value1, String value2) throws IllegalAccessException {
        return div(Double.valueOf(value1), Double.valueOf(value2));
    }

    public static double div(String value1, String value2, int scale) throws IllegalAccessException {
        return div(Double.valueOf(value1), Double.valueOf(value2), scale);
    }

    public static double div(String value1, String value2, int scale, int mode) throws IllegalAccessException {
        return div(Double.valueOf(value1), Double.valueOf(value2), scale, mode);
    }


    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    public static double div(double value1, double value2) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.divide(b2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double div(double value1, double value2, int scale, int mode) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.divide(b2, scale, mode).doubleValue();
    }


    @NonNull
    /**
     * 最后3个字放大
     */
    public static SpannableString getRealTimePriceTextBig(Context context, String realTimeText) {
        int index = realTimeText.indexOf(".");
        SpannableString spannableString = new SpannableString(realTimeText);
        if (index > -1) {
            if (spannableString.length() - index > 3) {
                //小数点>=3位的
                spannableString.setSpan(new TextAppearanceSpan(context, R.style.realTimeLeftText),
                        0, spannableString.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new TextAppearanceSpan(context, R.style.realTimeRightText),
                        spannableString.length() - 3, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new SuperscriptSpan(), spannableString.length() - 1, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                //小数点小于3位。
                spannableString.setSpan(new TextAppearanceSpan(context, R.style.realTimeLeftText),
                        0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new TextAppearanceSpan(context, R.style.realTimeRightText),
                        index, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            //不存在小数
            spannableString.setSpan(new TextAppearanceSpan(context, R.style.realTimeLeftText),
                    0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    /**
     * new BigDecimal(123456789)
     * (",###,###",bd)); //out: 123,456,789
     *
     * @return
     */
    public static String parseMoney(int money) {
        BigDecimal bd = new BigDecimal(money);
        DecimalFormat df = new DecimalFormat(",###,###");
        return df.format(bd);
    }
}
