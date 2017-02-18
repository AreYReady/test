package com.xkj.trade.utils;

import com.xkj.trade.R;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanCurrentServerTime;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanAllSymbols;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeContentFrag;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import static com.xkj.trade.constant.TradeDateConstant.VOLUME_MONEY;

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
        return calcMaxMinPrice(historyDataList, digits, 0, historyDataList.getList().size());
    }

    public static double[] calcMaxMinPrice(BeanHistory.BeanHistoryData historyDataList, int digits, int beginIndex, int stopIndex) {
        double[] result = new double[2];
        double minPrice = 0;
        double maxPrice = 0;
        BigDecimal minP, maxP;
        int size = historyDataList.getList().size();
        BeanHistory.BeanHistoryData.HistoryItem historyItem;
        if (size > 0 && beginIndex >= 0 && beginIndex < size && stopIndex <= size) {
            for (int i = beginIndex; i < stopIndex; i++) {
                historyItem = historyDataList.getList().get(i);
                if (i == beginIndex) {
                    minPrice = historyItem.getLow();
                    maxPrice = historyItem.getHigh();
                }
                if (historyItem.getLow() < minPrice) {
                    minPrice = historyItem.getLow();
                }
                if (historyItem.getHigh() > maxPrice) {
                    maxPrice = historyItem.getHigh();
                }
            }
            double offset = MoneyUtil.mulPrice(MoneyUtil.subPrice(maxPrice, minPrice), 0.1);
            maxPrice = MoneyUtil.addPrice(maxPrice, offset);//最大值上面部分的空间
            minPrice = MoneyUtil.subPrice(minPrice, offset);//最小值下面部分的空间
            result[0] = minPrice;
            result[1] = maxPrice;
        }
        return result;
    }

    public static int[] drawLineCount(int digits, double maxPrice, double minPrice) {
        double maxPrices = maxPrice;
        double min = minPrice;
        int i = (int) (maxPrice * Math.pow(10, digits) - minPrice * Math.pow(10, digits));
        int[] data = new int[2];
        int len = String.valueOf(i).length();
        data[1] = 10;
        if (i > 5 * Math.pow(10, len - 1)) {
            data[0] = (int) Math.pow(10, len - 1);

        } else if (i > 2 * Math.pow(10, len - 1)) {
            data[0] = (int) Math.pow(10, len - 2) * 5;
        } else {
            data[0] = (int) Math.pow(10, len - 2) * 2;
        }
        return data;
    }

    public static int selectPeriod(String period) {
        int mPeriod = 1;
        switch (period) {
            case "m1":
                mPeriod = 1;
                break;
            case "m5":
                mPeriod = 5;
                break;
            case "m15":
                mPeriod = 15;
                break;
            case "m30":
                mPeriod = 30;
                break;
            case "h1":
                mPeriod = 60;
                break;
            case "h4":
                mPeriod = 240;
                break;
            case "d1":
                mPeriod = 1440;
                break;
            case "w1":
                mPeriod = 10080;
                break;
        }
        return mPeriod;
    }

    public static String selectPeriod(int period) {
        String mPeriod = "m1";
        switch (period) {
            case 1:
                mPeriod = "m1";
                break;
            case 5:
                mPeriod = "m5";
                break;
            case 15:
                mPeriod = "m15";
                break;
            case 30:
                mPeriod = "m30";
                break;
            case 60:
                mPeriod = "h1";
                break;
            case 240:
                mPeriod = "h4";
                break;
            case 1440:
                mPeriod = "d1";
                break;
            case 10080:
                mPeriod = "w1";
                break;
        }
        return mPeriod;
    }

    public static int getSymbolFlag(String symbol) {
        int resource = 0;
        switch (symbol) {
            default:
                resource = R.drawable.ic_instrument_audhuf;
                break;
        }
        return resource;
    }

    public static Map<String, String> postMap() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put(RequestConstant.API_ID, ACache.get(MyApplication.getInstance().getApplicationContext()).getAsString(RequestConstant.API_ID));
        map.put(RequestConstant.API_TIME, DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
        return map;
    }

    /**
     * 计算利润与杠杆无关的,杠杆只是与占用的保证金有关,与盈亏或者其他没有任何关系的,一般来说选择1：200的人最多吧.利润的计算方法如下：我们将货币对分为以下三类：
     * 1直接盘：EUR/USD, GBP/USD, AUD/USD, NZD/USD
     * 2间接盘：USD/JPY, USD/CHF, USD/CAD, USD/XMN, USD/TRY
     * 3交叉盘：EUR/GBP, EUR/JPY, GBP/JPY, EUR/CHF, CHF/JPY
     * 1.直接盘货币对点值:1手一点的点值为 10美元,
     * 2.间接盘货币对点值
     * 公式： （标准手价值 X 货币最小变动价格）/ 货币价
     * 例如,USD/JPY 报价 91.28 的时候点值计算如下（USD/JPY最小变动价格为 0.01）：
     * 1手一点点差 = （100,000 X 0.01） / 91.28 = $ 10.96
     * 例如,USD/CHF 报价 1.0050 的时候点值计算如下 （USD/CHF最小变动价格为 0.0001）：
     * 1手一点点差 = （100,000 X 0.0001）/ 1.0050 = $ 9.95
     * 3.交叉盘货币对点值
     * 公式： （标准手价值 X 货币最小变动价格 X 基本货币价）/ 交叉盘货币对价
     * 例如： EUR/GBP报价为 0.9036, 于此同时 EUR/USD报价为 1.5021,点值计算如下（EUR/GBP最小变动价格为 0.0001）：
     * 标准手一点点差 = （100,000 X 0.0001 X 1.5021）/ 0.9036 = $16.62
     *
     * @param openPrices  订单的开仓价格
     * @param action        buy为买，sell为卖
     */
    public static String getProfit(RealTimeDataList.BeanRealTime beanRealTime, String action, String openPrices, String volume) {
        return getProfit(beanRealTime.getSymbol(),beanRealTime.getAsk(),beanRealTime.getBid(),action,openPrices,volume);
    }
    public static String getProfit(String symbol,double ask,double bid,String action,String openPrices,String volume) {
        try {
            double currentPrices;
            if (action.equalsIgnoreCase("buy")) {
                currentPrices=bid;
            } else {
                currentPrices=ask;
            }
            if (symbol.substring(0, 2).equals("USD")) {
                //直接盘
              return   MoneyUtil.moneyFormat(MoneyUtil.mulPrice(MoneyUtil.subPrice(Double.valueOf(currentPrices), Double.valueOf(openPrices)), VOLUME_MONEY*Double.valueOf(volume)), 2);
            } else if (symbol.substring(symbol.length() - 3, symbol.length()).equals("USD")) {
                //间接盘
             return   String.valueOf(MoneyUtil.div(VOLUME_MONEY*Double.valueOf(volume)* MoneyUtil.subPrice(Double.valueOf(currentPrices), Double.valueOf(openPrices)), Double.valueOf(currentPrices), 2));
            } else {
                BeanAllSymbols.SymbolPrices symbolPrices = MainTradeContentFrag.realTimeMap.get(symbol.substring(0, 3).concat("USD"));
                //交叉盘
                //例如： EUR/GBP报价为 0.9036, 于此同时 EUR/USD报价为 1.5021,点值计算如下（EUR/GBP最小变动价格为 0.0001）：
//                标准手一点点差 = （100,000 X 0.0001 X 1.5021）/ 0.9036 = $16.62
                  return String.valueOf(MoneyUtil.div(VOLUME_MONEY*Double.valueOf(volume)
                          * MoneyUtil.subPrice(Double.valueOf(currentPrices), Double.valueOf(openPrices))
                          * (Double.valueOf((MainTradeContentFrag.realTimeMap.get(symbol.substring(0, 3).concat("USD"))).getBid())),Double.valueOf(currentPrices), 2));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

}
