package trade.xkj.com.trade.utils;

import android.util.Log;

import java.math.BigDecimal;

import trade.xkj.com.trade.bean.HistoryDataList;

/**
 * Created by admin on 2016-11-24.
 */

public class DataUtil {
    /**
     * 计算最大价格，最小价格
     *
     * @param historyDataList
     * @return [0]为最小值，[1]为最大值
     */
    public static double[] calcMaxMinPrice(HistoryDataList historyDataList, int digits) {
       return calcMaxMinPrice(historyDataList,digits,0,historyDataList.getItems().size());
    }
    public static double[] calcMaxMinPrice(HistoryDataList historyDataList, int digits,int beginIndex,int stopIndex) {
        double[] result = new double[2];
        double minPrice = 0;
        double maxPrice = 0;
        BigDecimal minP, maxP;
        int size = historyDataList.getItems().size();
        if (size > 0&&beginIndex>=0&&beginIndex<size&&stopIndex<=size) {
            for (int i = beginIndex; i < stopIndex; i++) {
                String price[] = historyDataList.getItems().get(i).getO().split("\\|");
                if (i == 0||i==beginIndex) {
//                    minPrice = MoneyUtil.addPrice(price[0], price[2], digits);
//                    maxPrice = MoneyUtil.addPrice(price[0], price[1], digits);
                    minPrice = Double.valueOf(price[0]) + Double.valueOf(price[2]);
                    maxPrice = Double.valueOf(price[0]) + Double.valueOf(price[1]);
                } else {
//                    double tempi = MoneyUtil.addPrice(price[0], price[2], digits);
                    double tempi = Double.valueOf(price[0]) + Double.valueOf(price[2]);
                    double tempa = Double.valueOf(price[0]) + Double.valueOf(price[1]);
//                    double tempa = MoneyUtil.addPrice(price[0], price[1], digits);
                    if (tempa > maxPrice) {
                        maxPrice = tempa;
                    }
                    if (tempi < minPrice) {
                        minPrice = tempi;
                    }
                }
            }
            double offset = MoneyUtil.mulPrice(MoneyUtil.subPrice(maxPrice,minPrice),0.1);

            maxPrice = MoneyUtil.addPrice(maxPrice, offset);//最大值上面部分的空间
            minPrice = MoneyUtil.subPrice(minPrice, offset);//最小值下面部分的空间
//            maxPrice = maxPrice + (maxPrice - minPrice) * 0.1;
//            minPrice = minPrice - (maxPrice - minPrice) * 0.1;
//            minP = new BigDecimal(minPrice).movePointLeft(digits);
//            maxP = new BigDecimal(maxPrice).movePointLeft(digits);
//            result[0] = minP.doubleValue();
//            result[1] = maxP.doubleValue();
            result[0] = minPrice;
            result[1] = maxPrice;
            historyDataList.setPrice(result);
        }
        return result;
    }
    public static int[] drawLineCount(int digits,double maxPrice,double minPrice ){
        int i = (int)(maxPrice-minPrice);
        int[] data=new int[2];
        int len=String.valueOf(i).length();
        data[1]=10;
        if(i>5*Math.pow(10,len-1)){
            data[0]=(int)Math.pow(10,len-1);

        }else if(i>2*Math.pow(10,len-1)){
            data[0]=(int)Math.pow(10,len-2)*5;
        }else{
            data[0]=(int)Math.pow(10,len-2)*2;
        }

        Log.i("DataUtil:hsc", "drawLine差价 "+data[0] +"  "+  i  );
        return data;
    }
    public static int selectPeriod(String period){
        int mPeriod=1;
        switch (period){
            case "m1":
                mPeriod=1;
                break;
            case "m5":
                mPeriod=5;
                break;
            case "m15":
                mPeriod=15;
                break;
            case "m30":
                mPeriod=30;
                break;
            case "h1":
                mPeriod=60;
                break;
            case "h4":
                mPeriod=240;
                break;
            case "d1":
                mPeriod=1440;
                break;
            case "w1":
                mPeriod=10080;
                break;
        }
        return mPeriod;
    }
    public static String selectPeriod(int period){
        String mPeriod="m1";
        switch (period){
            case 1:
                mPeriod="m1";
                break;
            case 5:
                mPeriod="m5";
                break;
            case 15:
                mPeriod="m15";
                break;
            case 30:
                mPeriod="m30";
                break;
            case 60:
                mPeriod="h1";
                break;
            case 240:
                mPeriod="h4";
                break;
            case 1440:
                mPeriod="d1";
                break;
            case 10080:
                mPeriod="w1";
                break;
        }
        return mPeriod;
    }
}
