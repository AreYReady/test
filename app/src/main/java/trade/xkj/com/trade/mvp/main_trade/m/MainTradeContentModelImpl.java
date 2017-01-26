package trade.xkj.com.trade.mvp.main_trade.m;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import trade.xkj.com.trade.IO.okhttp.OkhttpUtils;
import trade.xkj.com.trade.bean.BeanCurrentServerTime;
import trade.xkj.com.trade.bean.BeanServerTime;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.EventBusAllSymbol;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.bean_.BeanHistory;
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


    public MainTradeContentModelImpl(MainTradeListener.MainTradeContentPreListener mMainTradeContentPreListener, Context context) {
        this.mMainTradeContentPreListener = mMainTradeContentPreListener;
//        this.mHandler = handler;
        mContext=context;
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetAllSymbol(EventBusAllSymbol eventBusAllSymbol) {
        mEventBusAllSymbol = eventBusAllSymbol;
        Log.i(TAG, "onGetAllSymbol: ");
    }





    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetShowSymbol(BeanSymbolConfig beanSymbolConfig) {
        sendSubSymbol(beanSymbolConfig);
    }

    /**
     * 发送订阅的商品类型
     *
     * @param beanSymbolConfig
     */
    private void sendSubSymbol(BeanSymbolConfig beanSymbolConfig) {

        if (subTradeSymbol == null)
            subTradeSymbol = new ArrayList<>();
        subTradeSymbol.clear();
        Log.i(TAG, "sendSubSymbol: 发送订阅商品实时数据请求");
        ArrayList<BeanSymbolConfig.SymbolsBean> showSymbolsList = beanSymbolConfig.getSymbols();
        for (BeanSymbolConfig.SymbolsBean allsymbol : beanSymbolConfig.getSymbols()) {
            for (BeanSymbolConfig.SymbolsBean symbol : showSymbolsList) {
                if (!symbol.getSymbol().equalsIgnoreCase(allsymbol.getSymbol())) {
                    continue;
                } else {
                    subTradeSymbol.add(symbol);
                }
//                        sendMessageToSubThread("{\"msg_type\":1010,\"symbol\":\"" + symbol.getSymbol() + "\"}");
            }
        }
        refreshIndicator(subTradeSymbol);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetServiceTime(BeanServerTime beanServerTime) {

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetRealTimeData(RealTimeDataList realTimeDataList) {
    }


//    public void sendMessageToSubThread(String subSymbolPre) {
//        Message messagePre = new Message();
//        messagePre.obj = subSymbolPre;
//        mHandler.sendMessage(messagePre);
//    }


    public void sendHistoryRequest(String symbol, String period, int count) {
        TreeMap<String ,String> map=new TreeMap<>();
        map.put(RequestConstant.API_ID, ACache.get(mContext).getAsString(RequestConstant.API_ID));
        map.put(RequestConstant.API_TIME,DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
        map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(symbol));
        map.put(RequestConstant.PERIOD,period);
        map.put(RequestConstant.BARNUM,String.valueOf(count));
        map.put(RequestConstant.API_SIGN,AesEncryptionUtil.getApiSign(URL_MT4_PRICE,map));
        sendRequest(URL_MT4_PRICE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanHistory beanHistory=new Gson().fromJson(response.body().string(),new TypeToken<BeanHistory>(){}.getType());
                if(beanHistory.getStatus()==1){
                    Log.i(TAG, "onResponse: 请求历史数据 "+new Gson().toJson(beanHistory,new TypeToken<BeanHistory>(){}.getType()));
                    mMainTradeContentPreListener.refreshView(beanHistory);
                }
            }
        });
    }

    /**
     * 发送请求数据；
     */
    Request request;

    private void sendRequest(String url, Map<String,String> map, Callback callback) {
        OkhttpUtils.enqueue(url,map,callback);
    }

    public void refreshIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol) {
        mMainTradeContentPreListener.refreshIndicator(subTradeSymbol);
    }
}
