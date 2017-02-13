package com.xkj.trade.utils;

import com.xkj.trade.R;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanCurrentServerTime;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.constant.RequestConstant;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

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
    public static double[] calcMaxMinPrice(BeanHistory.BeanHistoryData historyDataList, int digits) {
       return calcMaxMinPrice(historyDataList,digits,0,historyDataList.getList().size());
    }
    public static double[] calcMaxMinPrice(BeanHistory.BeanHistoryData historyDataList, int digits, int beginIndex, int stopIndex) {
        double[] result = new double[2];
        double minPrice = 0;
        double maxPrice = 0;
        BigDecimal minP, maxP;
        int size = historyDataList.getList().size();
        BeanHistory.BeanHistoryData.HistoryItem historyItem;
        if (size > 0&&beginIndex>=0&&beginIndex<size&&stopIndex<=size) {
            for (int i = beginIndex; i < stopIndex; i++) {
                 historyItem= historyDataList.getList().get(i);
                if(i==beginIndex){
                    minPrice=historyItem.getLow();
                    maxPrice=historyItem.getHigh();
                }
                if(historyItem.getLow()<minPrice){
                    minPrice=historyItem.getLow();
                }
                if(historyItem.getHigh()>maxPrice){
                    maxPrice=historyItem.getHigh();
                }
            }
            double offset = MoneyUtil.mulPrice(MoneyUtil.subPrice(maxPrice,minPrice),0.1);
            maxPrice = MoneyUtil.addPrice(maxPrice, offset);//最大值上面部分的空间
            minPrice = MoneyUtil.subPrice(minPrice, offset);//最小值下面部分的空间
            result[0] = minPrice;
            result[1] = maxPrice;
        }
        return result;
    }
    public static int[] drawLineCount(int digits,double maxPrice,double minPrice ){
        double maxPrices=maxPrice;
        double min=minPrice;
        int i = (int)(maxPrice*Math.pow(10,digits)-minPrice*Math.pow(10,digits));
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
    public static int getSymbolFlag(String symbol){
        int resource=0;
        switch (symbol){
            default:
                resource= R.drawable.ic_instrument_audhuf;
            break;
        }
        return resource;
    }
    public static Map<String,String> postMap(){
        TreeMap<String,String> map=new TreeMap<>();
        map.put(RequestConstant.API_ID, ACache.get(MyApplication.getInstance().getApplicationContext()).getAsString(RequestConstant.API_ID));
        map.put(RequestConstant.API_TIME, DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
        return map;
    }
}
