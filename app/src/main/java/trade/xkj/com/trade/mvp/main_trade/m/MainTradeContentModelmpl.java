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

import trade.xkj.com.trade.Utils.DataUtil;
import trade.xkj.com.trade.Utils.DateUtils;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanCurrentServerTime;
import trade.xkj.com.trade.bean.BeanHistoryRequest;
import trade.xkj.com.trade.bean.BeanServerTime;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.DataEvent;
import trade.xkj.com.trade.bean.EventBusAllSymbol;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.constant.MessageType;
import trade.xkj.com.trade.constant.TradeDateConstant;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPre;

import static trade.xkj.com.trade.constant.TradeDateConstant.diffTimeServiceAndNative;

/**
 * Created by admin on 2016-11-23.
 */

public class MainTradeContentModelmpl implements MainTradeContentModel {
    private Handler mHandler;
    private final  String TAG= SystemUtil.getTAG(this);
    private MainTradeContentPre mMainTradeContentPre;
    private EventBusAllSymbol mEventBusAllSymbol;
    private ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol;
    public MainTradeContentModelmpl(MainTradeContentPre mMainTradeContentPre,Handler handler){
        this.mMainTradeContentPre=mMainTradeContentPre;
        this.mHandler=handler;
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
public void onGetAllSymbol(EventBusAllSymbol eventBusAllSymbol){
    mEventBusAllSymbol=eventBusAllSymbol;
}
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDrawHistroyData(DataEvent dataEvent) {
        Log.i(TAG, "onDrawHistroyData: ");
        HistoryDataList dataList;
        if (dataEvent.getType() == MessageType.TYPE_BINARY_HISTORY_LIST) {//绘制历史数据
            dataList = new Gson().fromJson(dataEvent.getResult(), new TypeToken<HistoryDataList>() {
            }.getType());
            int digits = dataList.getDigits();//当前页面产品的小数位
            DataUtil.calcMaxMinPrice(dataList,digits);
            mMainTradeContentPre.refreshView(dataList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetShowSymbol(BeanSymbolConfig beanSymbolConfig){
        BeanSymbolConfig mBeanSymbolConfig1=beanSymbolConfig;
        TradeDateConstant.tz_delta = mBeanSymbolConfig1.getTz_delta();
        sendSubSymbol(beanSymbolConfig);
        Log.i(TAG, "onGetShowSymbol: tz_delta"+ TradeDateConstant.tz_delta);
    }

    /**
     * 发送订阅的商品类型
     * @param beanSymbolConfig
     */
    private void sendSubSymbol(BeanSymbolConfig beanSymbolConfig) {

        if(mEventBusAllSymbol!=null) {
            if(subTradeSymbol==null)
            subTradeSymbol=new ArrayList<>();
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
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetServiceTime(BeanServerTime beanServerTime){
        BeanCurrentServerTime mBeanCurrentServerTime=BeanCurrentServerTime.getInstance(DateUtils.getOrderStartTime(beanServerTime.getTime()));
        diffTimeServiceAndNative=mBeanCurrentServerTime.getDiffTime();
    }
   @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetRealTimeData(RealTimeDataList realTimeDataList){
    }


    public void sendMessageToSubThread(String subSymblePre) {
        Message messagePre = new Message();
        messagePre.obj = subSymblePre;
        mHandler.sendMessage(messagePre);
    }
    public void sendHistoryRequest(String symbol,int count){
        String request = new Gson().toJson(new BeanHistoryRequest(symbol, count), BeanHistoryRequest.class);
        sendMessageToSubThread(request);
    }

    @Override
    public void refreshIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol) {
        mMainTradeContentPre.refreshIndicator(subTradeSymbol);
    }

}
