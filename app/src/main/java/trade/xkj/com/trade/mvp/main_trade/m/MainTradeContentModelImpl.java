package trade.xkj.com.trade.mvp.main_trade.m;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import trade.xkj.com.trade.IO.okhttp.ChatWebSocket;
import trade.xkj.com.trade.IO.okhttp.OkhttpUtils;
import trade.xkj.com.trade.bean.BeanCurrentServerTime;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.EventBusAllSymbol;
import trade.xkj.com.trade.bean_.BeanBaseResponse;
import trade.xkj.com.trade.bean_.BeanHistory;
import trade.xkj.com.trade.constant.CacheKeyConstant;
import trade.xkj.com.trade.constant.RequestConstant;
import trade.xkj.com.trade.mvp.main_trade.v.MainTradeListener;
import trade.xkj.com.trade.utils.ACache;
import trade.xkj.com.trade.utils.AesEncryptionUtil;
import trade.xkj.com.trade.utils.DateUtils;
import trade.xkj.com.trade.utils.SystemUtil;

import static trade.xkj.com.trade.constant.UrlConstant.URL_MT4_PRICE;


/**
 * Created by hsc on 2016-11-23.
 */

public class MainTradeContentModelImpl implements MainTradeListener.MainTradeContentModelListener {
    private Handler mHandler;
    private final String TAG = SystemUtil.getTAG(this);
    private MainTradeListener.MainTradeContentPreListener mMainTradeContentPreListener;
    private EventBusAllSymbol mEventBusAllSymbol;
    private ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol;
    private String oldSubSymbol;
    private Context mContext;
    private LinkedList<String> subSymbols;


    public MainTradeContentModelImpl(MainTradeListener.MainTradeContentPreListener mMainTradeContentPreListener, Context context) {
        this.mMainTradeContentPreListener = mMainTradeContentPreListener;
        mContext = context;
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }


    public void sendHistoryRequest(String symbol, String period, int count) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put(RequestConstant.API_ID, ACache.get(mContext).getAsString(RequestConstant.API_ID));
        map.put(RequestConstant.API_TIME, DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
        if (symbol != null) {
            map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(symbol));
        }
        if (period != null) {
            map.put(RequestConstant.PERIOD, period);
        }
        if (count != 0) {
            map.put(RequestConstant.BARNUM, String.valueOf(count));
        }
        map.put(RequestConstant.API_SIGN, AesEncryptionUtil.getApiSign(URL_MT4_PRICE, map));
        sendRequest(URL_MT4_PRICE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i(TAG, "onResponse: " + body);
                BeanHistory beanHistory = new Gson().fromJson(body, new TypeToken<BeanHistory>() {
                }.getType());
                if (beanHistory.getStatus() == 1) {
                    Log.i(TAG, "onResponse: 请求历史数据 " + new Gson().toJson(beanHistory, new TypeToken<BeanHistory>() {
                    }.getType()));
                    mMainTradeContentPreListener.refreshView(beanHistory);
//                    ChatWebSocket.getChartWebSocket().sendMessage("{\"msg_type\":1010,\"symbol\":\"" +beanHistory.getData().getSymbol() + "\"}");
                }
            }
        });
    }

    @Override
    public void sendAllSymbolsRequest() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put(RequestConstant.API_ID, ACache.get(mContext).getAsString(RequestConstant.API_ID));
        map.put(RequestConstant.API_TIME, DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
        map.put(RequestConstant.API_SIGN, AesEncryptionUtil.getApiSign(URL_MT4_PRICE, map));
        sendRequest(URL_MT4_PRICE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i(TAG, "onResponse: " + body);
                BeanBaseResponse beanBaseResponse = new Gson().fromJson(body, new TypeToken<BeanBaseResponse>() {
                }.getType());
                if (beanBaseResponse.getStatus() == 1) {
                    ACache.get(mContext).put(CacheKeyConstant.ALL_SYMBOLS_PRICES, body);
                }
            }
        });
    }

    @Override
    public void sendSubSymbolsRequest(ArrayList<String> symbolsName,boolean subOrCancel) {
        Log.i(TAG, "sendSubSymbolsRequest: ");
        ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
        for (String name : symbolsName) {
            if (chartWebSocket != null) {
                if(subOrCancel)
                    chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + name + "\"}");
                else{
                    chartWebSocket.sendMessage("{\"msg_type\":1020,\"symbol\":\"" + name + "\"}");
                }
            }
        }
    }

    /**
     * 发送请求数据；
     */
    Request request;

    private void sendRequest(String url, Map<String, String> map, Callback callback) {
        OkhttpUtils.enqueue(url, map, callback);
    }
}
