package com.xkj.trade.mvp.main_trade.fragment_content.m;

import android.content.Context;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanSymbolConfig;
import com.xkj.trade.bean.EventBusAllSymbol;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeFragListener;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

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


    public void requestHistoryData(String symbol, String period, int count) {

        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(symbol));
        map.put(RequestConstant.PERIOD, period);
        map.put(RequestConstant.BARNUM, String.valueOf(count));
//        map.put(RequestConstant.API_SIGN, AesEncryptionUtil.getApiSign(URL_MT4_PRICE, map));
        sendRequest(URL_MT4_PRICE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i(TAG, "onResponse: 请求历史数据" + body);
                BeanHistory beanHistory = new Gson().fromJson(body, BeanHistory.class);
                if (beanHistory.getStatus() == 1) {
                    mMainTradeContentPreListener.responseHistoryData(beanHistory);
//                    ChatWebSocket.getChartWebSocket().sendMessage("{\"msg_type\":1010,\"symbol\":\"" +beanHistory.getData().getSymbol() + "\"}");
                }
            }
        });
    }

    @Override
    public void requestAllSymbolsData() {
//        Map<String, String> map = DataUtil.postMap();
//        map.put(RequestConstant.API_SIGN, AesEncryptionUtil.getApiSign(URL_MT4_PRICE, map));
        Map<String,String> map=new ArrayMap<>();
//        map.put(RequestConstant.IS_FILTER,"notbo");
        sendRequest(URL_MT4_PRICE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i(TAG, "onResponse: 所有商品报价" + body);
                BeanBaseResponse beanBaseResponse = new Gson().fromJson(body, BeanBaseResponse.class);
                if (beanBaseResponse.getStatus() == 1) {
//                    ACache.get(mContext).put(CacheKeyConstant.ALL_SYMBOLS_PRICES, body);
                    mMainTradeContentPreListener.responseAllSymbolsData(body);
                }
            }
        });
    }

    @Override
    public void requestSubSymbolsData(ArrayList<String> symbolsName, boolean subOrCancel) {
        Log.i(TAG, "requestSubSymbolsData: ");
        ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
        for (String name : symbolsName) {

            if (chartWebSocket != null) {
                if (subOrCancel)
                    chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + name + "\"}");
                else {
                    //后面这个是为了尾部为USD的，都不取消订阅。
                    if(!name.substring(name.length()-3,name.length()).equals("USD")) {
                        chartWebSocket.sendMessage("{\"msg_type\":1020,\"symbol\":\"" + name + "\"}");
                    }
                }
            }
        }
    }

    @Override
    public void requestUserListData(String mt4Login) {

    }

    @Override
    public void requestOpenPositionData() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN,AesEncryptionUtil.stringBase64toString(ACache.get(mContext).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.SYMBOL,AesEncryptionUtil.stringBase64toString(MyApplication.getInstance().beanIndicatorData.getSymbol()));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_MAKET_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: "+call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp;
                Log.i(TAG, "onResponse: "+(temp=response.body().string()));
                BeanBaseResponse info=new Gson().fromJson(temp,BeanBaseResponse.class);
                if(info.getStatus()==1){
                    BeanOpenPosition beanOpenPosition= new Gson().fromJson(temp,BeanOpenPosition.class);
                    mMainTradeContentPreListener.responseOpenPosition(beanOpenPosition);
                }
            }
        });
    }

    /**
     * 发送请求数据；
     */
    Request request;

    private void sendRequest(String url, Map<String, String> map, Callback callback) {
        OkhttpUtils.enqueue(url, map, callback);
    }
}
