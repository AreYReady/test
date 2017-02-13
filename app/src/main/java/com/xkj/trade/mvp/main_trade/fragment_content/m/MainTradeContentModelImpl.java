package com.xkj.trade.mvp.main_trade.fragment_content.m;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.bean.BeanSymbolConfig;
import com.xkj.trade.bean.EventBusAllSymbol;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.constant.CacheKeyConstant;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeFragListener;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.xkj.trade.constant.UrlConstant.URL_MT4_PRICE;


/**
 * Created by hsc on 2016-11-23.
 */

public class MainTradeContentModelImpl implements MainTradeFragListener.MainTradeContentModelListener {
    private Handler mHandler;
    private final String TAG = SystemUtil.getTAG(this);
    private MainTradeFragListener.MainTradeContentPreListener mMainTradeContentPreListener;
    private EventBusAllSymbol mEventBusAllSymbol;
    private ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol;
    private String oldSubSymbol;
    private Context mContext;
    private LinkedList<String> subSymbols;


    public MainTradeContentModelImpl(MainTradeFragListener.MainTradeContentPreListener mMainTradeContentPreListener, Context context) {
        this.mMainTradeContentPreListener = mMainTradeContentPreListener;
        mContext = context;
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }


    public void sendHistoryRequest(String symbol, String period, int count) {
        Map<String, String> map = DataUtil.postMap();
        map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(symbol));
        map.put(RequestConstant.PERIOD, period);
        map.put(RequestConstant.BARNUM, String.valueOf(count));
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
        Map<String, String> map = DataUtil.postMap();
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
    public void sendSubSymbolsRequest(ArrayList<String> symbolsName, boolean subOrCancel) {
        Log.i(TAG, "sendSubSymbolsRequest: ");
        ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
        for (String name : symbolsName) {
            if (chartWebSocket != null) {
                if (subOrCancel)
                    chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + name + "\"}");
                else {
                    chartWebSocket.sendMessage("{\"msg_type\":1020,\"symbol\":\"" + name + "\"}");
                }
            }
        }
    }

    @Override
    public void loadingUserList(String mt4Login) {

    }

    /**
     * 发送请求数据；
     */
    Request request;

    private void sendRequest(String url, Map<String, String> map, Callback callback) {
        OkhttpUtils.enqueue(url, map, callback);
    }
}
