package trade.xkj.com.trade.Utils;

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
//            double offset = MoneyUtil.mulPrice(MoneyUtil.subPrice(maxPrice, minPrice), 0.1);
//            maxPrice = MoneyUtil.addPrice(maxPrice, offset);//最大值上面部分的空间
//            minPrice = MoneyUtil.subPrice(maxPrice, offset);//最小值下面部分的空间
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
        int i = (int)(maxPrice-minPrice) * digits * 10;
        Log.i("DataUtil:hsc", "drawLineCount: i"+i+"        digits" +digits );
        int[] data=new int[2];
        if(i<6*digits*10){
            data[0]=5;
            data[1]= i/5;
        }else{
            data[0]=6;
            data[1]=i/6;
        }
        return data;
    }

}
