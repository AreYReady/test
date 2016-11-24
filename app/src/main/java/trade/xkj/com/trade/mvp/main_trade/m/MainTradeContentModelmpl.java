package trade.xkj.com.trade.mvp.main_trade.m;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import trade.xkj.com.trade.Utils.DataUtil;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanHistoryRequest;
import trade.xkj.com.trade.bean.DataEvent;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.constant.MessageType;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPre;

/**
 * Created by admin on 2016-11-23.
 */

public class MainTradeContentModelmpl implements MainTradeContentModel {
    private Handler mHandler;
    private final  String TAG= SystemUtil.getTAG(this);
    private MainTradeContentPre mMainTradeContentPre;
    public MainTradeContentModelmpl(MainTradeContentPre mMainTradeContentPre,Handler handler){
        this.mMainTradeContentPre=mMainTradeContentPre;
        this.mHandler=handler;
        EventBus.getDefault().register(this);
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
    public void sendMessageToSubThread(String subSymblePre) {
        Message messagePre = new Message();
        messagePre.obj = subSymblePre;
        mHandler.sendMessage(messagePre);
    }
    public void sendHistoryRequest(String symbol,int count){
        String request = new Gson().toJson(new BeanHistoryRequest(symbol, count), BeanHistoryRequest.class);
        sendMessageToSubThread(request);
    }



}
