package trade.xkj.com.trade.mvp.main_trade.m;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import trade.xkj.com.trade.utils.DataUtil;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanHistoryRequest;
import trade.xkj.com.trade.bean.BeanServerTime;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.DataEvent;
import trade.xkj.com.trade.bean.EventBusAllSymbol;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.constant.MessageType;
import trade.xkj.com.trade.mvp.main_trade.v.MainTradeListener;


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


    public MainTradeContentModelImpl(MainTradeListener.MainTradeContentPreListener mMainTradeContentPreListener, Handler handler) {
        this.mMainTradeContentPreListener = mMainTradeContentPreListener;
        this.mHandler = handler;
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetAllSymbol(EventBusAllSymbol eventBusAllSymbol) {
        mEventBusAllSymbol = eventBusAllSymbol;
        Log.i(TAG, "onGetAllSymbol: ");
    }

    /**
     * 接受类型历史数据
     *
     * @param dataEvent
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDrawHistoryData(DataEvent dataEvent) {
        Log.i(TAG, "onDrawHistoryData: ");
        HistoryDataList dataList;
        if (dataEvent.getType() == MessageType.TYPE_BINARY_HISTORY_LIST) {//绘制历史数据
            dataList = new Gson().fromJson(dataEvent.getResult(), new TypeToken<HistoryDataList>() {
            }.getType());
            DataUtil.calcMaxMinPrice(dataList, dataList.getDigits());
            mMainTradeContentPreListener.refreshView(dataList);
        }

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


    public void sendMessageToSubThread(String subSymbolPre) {
        Message messagePre = new Message();
        messagePre.obj = subSymbolPre;
        mHandler.sendMessage(messagePre);
    }





    public void sendHistoryRequest(String symbol, String period, int count) {
        //取消订阅
        if (oldSubSymbol != null)
            sendMessageToSubThread("{\"msg_type\":1020,\"symbol\":\"" + oldSubSymbol + "\"}");
        oldSubSymbol = symbol;
//        //订阅
        sendMessageToSubThread("{\"msg_type\":1010,\"symbol\":\"" + symbol + "\"}");
        String request = new Gson().toJson(new BeanHistoryRequest(symbol, count, period), BeanHistoryRequest.class);
        sendMessageToSubThread(request);
    }

    @Override
    public void refreshIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol) {
        mMainTradeContentPreListener.refreshIndicator(subTradeSymbol);
    }
}
