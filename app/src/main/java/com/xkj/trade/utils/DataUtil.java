package com.xkj.trade.utils;

import android.util.Log;

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
import static com.xkj.trade.utils.MoneyUtil.subPrice;

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
            double offset = MoneyUtil.mulPrice(subPrice(maxPrice, minPrice), 0.1);
            maxPrice = MoneyUtil.addPrice(maxPrice, offset);//最大值上面部分的空间
            minPrice = subPrice(minPrice, offset);//最小值下面部分的空间
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
            if(len==1){
                data[0]=1;
            }else
            data[0] = (int) Math.pow(10, len - 2) * 5;
        } else {
            data[0] = (int) Math.pow(10, len - 2) * 2;
        }
        if(data[0]==0){
            Log.i("hsc", "drawLineCount: ");
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
     *
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
     * @param openPrices 订单的开仓价格
     * @param action     buy为买，sell为卖
     */
    public static String getProfit(RealTimeDataList.BeanRealTime beanRealTime, String action, String openPrices, String volume) {
        return getProfit(beanRealTime.getSymbol(), beanRealTime.getAsk(), beanRealTime.getBid(), action, openPrices, volume);
    }

    public static String getProfit(String symbol, double ask, double bid, String action, String openPrices, String volume) {
        try {
            double currentPrices;
            double diffSpace;
            if (action.contains("buy")) {
                currentPrices = bid;
                diffSpace = subPrice(Double.valueOf(currentPrices), Double.valueOf(openPrices));
            } else {
                currentPrices = ask;
                diffSpace = MoneyUtil.subPrice(Double.valueOf(openPrices), Double.valueOf(currentPrices));
            }
            if (symbol.substring(symbol.length()-3, symbol.length()).equals("USD")) {
                //直接盘:1手一点的点值为 10美元,
//                String a=MainTradeContentFrag.realTimeMap.get("XAUUSD").getDigits();
//                Log.i("getProfit", "getProfit: "+a);
                int digits=openPrices.length()-openPrices.indexOf(".");
                return MoneyUtil.moneyFormat(diffSpace*Double.valueOf(Math.pow(10,Double.valueOf(MainTradeContentFrag.mDigitsList.get(symbol)))*Double.valueOf(volume)), 2);
            } else if (symbol.substring(0, 3).equals("USD")) {
                //间接盘
                // * 例如,USD/JPY 报价 91.28 的时候点值计算如下（USD/JPY最小变动价格为 0.01）：
//                * 1手一点点差 = （100,000 X 0.01） / 91.28 = $ 10.96
                return String.valueOf(MoneyUtil.div(MoneyUtil.mulPrice(VOLUME_MONEY * Double.valueOf(volume), diffSpace), Double.valueOf(currentPrices), 2));
            } else {
                BeanAllSymbols.SymbolPrices symbolPrices = MainTradeContentFrag.realTimeMap.get(symbol.substring(0, 3).concat("USD"));
                //交叉盘
                //例如： EUR/GBP报价为 0.9036, 于此同时 EUR/USD报价为 1.5021,点值计算如下（EUR/GBP最小变动价格为 0.0001）：
//                标准手一点点差 = （100,000 X 0.0001 X 1.5021）/ 0.9036 = $16.62
                return String.valueOf(MoneyUtil.div(VOLUME_MONEY * Double.valueOf(volume)
                        * diffSpace
                        * (Double.valueOf((MainTradeContentFrag.realTimeMap.get(symbol.substring(0, 3).concat("USD"))).getBid())), Double.valueOf(currentPrices), 2));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static int getImageId(String symbol) {
        int id = 0;
        switch (symbol.toLowerCase()) {
            case "我的收藏夹":
                id=R.mipmap.small_my_favourites_icon;
                break;
            case "audcad":
                id = R.mipmap.ic_instrument_audcad;
                break;
            case "audchf":
                id = R.mipmap.ic_instrument_audchf;
                break;
            case "audhuf":
                id = R.mipmap.ic_instrument_audhuf;
                break;
            case "audjpy":
                id = R.mipmap.ic_instrument_audjpy;
                break;
            case "audnzd":
                id = R.mipmap.ic_instrument_audnzd;
                break;
            case "audusd":
                id = R.mipmap.ic_instrument_audusd;
                break;
            case "be":
                id = R.mipmap.ic_instrument_be;
                break;
            case "beans":
                id = R.mipmap.ic_instrument_beans;
                break;
            case "br":
                id = R.mipmap.ic_instrument_br;
                break;
            case "brent":
                id = R.mipmap.ic_instrument_brent;
                break;
            case "bronze":
                id = R.mipmap.ic_instrument_bronze;
                break;
            case "btcusd":
                id = R.mipmap.ic_instrument_btcusd;
                break;
            case "ca":
                id = R.mipmap.ic_instrument_ca;
                break;
            case "cadchf":
                id = R.mipmap.ic_instrument_cadchf;
                break;
            case "cadjpy":
                id = R.mipmap.ic_instrument_cadjpy;
                break;
            case "ch":
                id = R.mipmap.ic_instrument_ch;
                break;
            case "chfjpy":
                id = R.mipmap.ic_instrument_chfjpy;
                break;
            case "chile":
                id = R.mipmap.ic_instrument_chile;
                break;
            case "chinese":
                id = R.mipmap.ic_instrument_chinese;
                break;
            case "cocoa":
                id = R.mipmap.ic_instrument_cocoa;
                break;
            case "coffee":
                id = R.mipmap.ic_instrument_coffee;
                break;
            case "copper":
                id = R.mipmap.ic_instrument_copper;
                break;
            case "corn":
                id = R.mipmap.ic_instrument_corn;
                break;
            case "cotton":
                id = R.mipmap.ic_instrument_cotton;
                break;
            case "de":
                id = R.mipmap.ic_instrument_de;
                break;
            case "es":
                id = R.mipmap.ic_instrument_es;
                break;
            case "eu":
                id = R.mipmap.ic_instrument_eu;
                break;
            case "euraud":
                id = R.mipmap.ic_instrument_euraud;
                break;
            case "eurcad":
                id = R.mipmap.ic_instrument_eurcad;
                break;
            case "eurchf":
                id = R.mipmap.ic_instrument_eurchf;
                break;
            case "eurdkk":
                id = R.mipmap.ic_instrument_eurdkk;
                break;
            case "eurgbp":
                id = R.mipmap.ic_instrument_eurgbp;
                break;
            case "eurhuf":
                id = R.mipmap.ic_instrument_eurhuf;
                break;
            case "eurjpy":
                id = R.mipmap.ic_instrument_eurjpy;
                break;
            case "eurnok":
                id = R.mipmap.ic_instrument_eurnok;
                break;
            case "eurnzd":
                id = R.mipmap.ic_instrument_eurnzd;
                break;
            case "eurpln":
                id = R.mipmap.ic_instrument_eurpln;
                break;
            case "eurrub":
                id = R.mipmap.ic_instrument_eurrub;
                break;
            case "eursek":
                id = R.mipmap.ic_instrument_eursek;
                break;
            case "eurtry":
                id = R.mipmap.ic_instrument_eurtry;
                break;
            case "eurusd":
                id = R.mipmap.ic_instrument_eurusd;
                break;
            case "eurzar":
                id = R.mipmap.ic_instrument_eurzar;
                break;
            case "fr":
                id = R.mipmap.ic_instrument_fr;
                break;
            case "gas":
                id = R.mipmap.ic_instrument_gas;
                break;
            case "gbpaud":
                id = R.mipmap.ic_instrument_gbpaud;
                break;
            case "gbpcad":
                id = R.mipmap.ic_instrument_gbpcad;
                break;
            case "gbpchf":
                id = R.mipmap.ic_instrument_gbpchf;
                break;
            case "gbphuf":
                id = R.mipmap.ic_instrument_gbphuf;
                break;
            case "gbpjpy":
                id = R.mipmap.ic_instrument_gbpjpy;
                break;
            case "gbpnzd":
                id = R.mipmap.ic_instrument_gbpnzd;
                break;
            case "gbprub":
                id = R.mipmap.ic_instrument_gbprub;
                break;
            case "gbpusd":
                id = R.mipmap.ic_instrument_gbpusd;
                break;
            case "gbpzar":
                id = R.mipmap.ic_instrument_gbpzar;
                break;
            case "gold":
                id = R.mipmap.ic_instrument_gold;
                break;
            case "hk":
                id = R.mipmap.ic_instrument_hk;
                break;
            case "in":
                id = R.mipmap.ic_instrument_in;
                break;
            case "it":
                id = R.mipmap.ic_instrument_it;
                break;
            case "jp":
                id = R.mipmap.ic_instrument_jp;
                break;
            case "nl":
                id = R.mipmap.ic_instrument_nl;
                break;
            case "nzdcad":
                id = R.mipmap.ic_instrument_nzdcad;
                break;
            case "nzdchf":
                id = R.mipmap.ic_instrument_nzdchf;
                break;
            case "nzdjpy":
                id = R.mipmap.ic_instrument_nzdjpy;
                break;
            case "nzdusd":
                id = R.mipmap.ic_instrument_nzdusd;
                break;
            case "oat":
                id = R.mipmap.ic_instrument_oat;
                break;
            case "oil":
                id = R.mipmap.ic_instrument_oil;
                break;
            case "orange":
                id = R.mipmap.ic_instrument_orange;
                break;
            case "paladium":
                id = R.mipmap.ic_instrument_paladium;
                break;
            case "rice":
                id = R.mipmap.ic_instrument_rice;
                break;
            case "ru":
                id = R.mipmap.ic_instrument_ru;
                break;
            case "silver":
                id = R.mipmap.ic_instrument_silver;
                break;
            case "sugar":
                id = R.mipmap.ic_instrument_sugar;
                break;
            case "uk":
                id = R.mipmap.ic_instrument_uk;
                break;
            case "us":
                id = R.mipmap.ic_instrument_us;
                break;
            case "usdcad":
                id = R.mipmap.ic_instrument_usdcad;
                break;
            case "usdchf":
                id = R.mipmap.ic_instrument_usdchf;
                break;
            case "usdczk":
                id = R.mipmap.ic_instrument_usdczk;
                break;
            case "usddkk":
                id = R.mipmap.ic_instrument_usddkk;
                break;
            case "usdhkd":
                id = R.mipmap.ic_instrument_usdhkd;
                break;
            case "usdhuf":
                id = R.mipmap.ic_instrument_usdhuf;
                break;
            case "usdils":
                id = R.mipmap.ic_instrument_usdils;
                break;
            case "usdjpy":
                id = R.mipmap.ic_instrument_usdjpy;
                break;
            case "usdmxn":
                id = R.mipmap.ic_instrument_usdmxn;
                break;
            case "usdnok":
                id = R.mipmap.ic_instrument_usdnok;
                break;
            case "usdpln":
                id = R.mipmap.ic_instrument_usdpln;
                break;
            case "usdron":
                id = R.mipmap.ic_instrument_usdron;
                break;
            case "usdrub":
                id = R.mipmap.ic_instrument_usdrub;
                break;
            case "usdsek":
                id = R.mipmap.ic_instrument_usdsek;
                break;
            case "usdsgd":
                id = R.mipmap.ic_instrument_usdsgd;
                break;
            case "usdtry":
                id = R.mipmap.ic_instrument_usdtry;
                break;
            case "usdzar":
                id = R.mipmap.ic_instrument_usdzar;
                break;
            case "wheat":
                id = R.mipmap.ic_instrument_wheat;
                break;
            case "usoil":
                id=R.mipmap.ic_instrument_gas;
            default:
                break;
        }
        if(symbol.contains("XAU")){
            id=R.mipmap.ic_instrument_gold;
        }else if(symbol.contains("XAG")){
            id=R.mipmap.ic_instrument_zinc;
        }
        return id;
    }

    /**
     * UASABD_60
     * * @param symbol
     * @param period
     * @return
     */
    public static  String symbolConnectPeriod(String symbol, String period){
        return symbol.concat("_").concat(period);
    }
}
