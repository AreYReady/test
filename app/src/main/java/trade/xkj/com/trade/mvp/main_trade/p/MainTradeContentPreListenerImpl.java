package trade.xkj.com.trade.mvp.main_trade.p;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.ACache;
import trade.xkj.com.trade.utils.DataUtil;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.base.MyApplication;
import trade.xkj.com.trade.bean.BeanCurrentServerTime;
import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.HistoryData;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.mvp.main_trade.m.MainTradeContentModelImpl;
import trade.xkj.com.trade.mvp.main_trade.v.MainTradeListener;

/**
 * Created by hsc on 2016-11-23.
 * TODO:
 */

public class MainTradeContentPreListenerImpl implements MainTradeListener.MainTradeContentPreListener {
    public MainTradeListener.MainTradeContentLFragListener mMainTradeContentLFragListener;
    public MainTradeListener.MainTradeContentModelListener mMainTradeContentModel;
    private Handler mHandler;
    private CountDownTimer timer;
    private final String TAG = SystemUtil.getTAG(this);
    private boolean isCountDown = false;
    private boolean isLastCount = false;
    private ACache aCache;
    private HistoryDataList oldData;
    //作为判断当前界面需要显示的交易类型，
    private String currentSymbol;
    private String currentPeriod;
    //作为准确判断是否是，本地数据加上网络后补的唯一标识:symbol+period+count   例如period：　60
    private String swithKey;

    public MainTradeContentPreListenerImpl(MainTradeListener.MainTradeContentLFragListener mListener, Handler mHandler) {
        mMainTradeContentLFragListener = mListener;
        mMainTradeContentModel = new MainTradeContentModelImpl(this, mHandler);
        this.mHandler = mHandler;
    }

    @Override
    public void loading() {
//        mMainTradeContentModel.sendHistoryRequest("AUDCAD", TradeDateConstant.count);
    }


    @Override
    public void refreshView(HistoryDataList data) {
        //只有数据符合当前需要的要求是，才做操作
        if (data.getSymbol().equals(currentSymbol) && data.getPeriod() == DataUtil.selectPeriod(currentPeriod)) {
//            isLastCount=false;
//        mMainTradeContentLFragListener.freshView(oldData, isCountDown || (oldData.getSymbol().concat(String.valueOf(oldData.getPeriod())).concat(String.valueOf(oldData.getCount()))).equals(swithKey));
            //当本地数据不全是，本地数据要等后补网咯数据回来时，补全再显示
            if ((data.getSymbol().concat(String.valueOf(data.getPeriod())).concat(String.valueOf(data.getCount()))).equals(swithKey)) {
                data = repairData(oldData, data);
            } else if (isCountDown) {
                //倒计时生效，更新数据
                data = repairData(oldData, data);
            } else if (data.getCount() < 100) {
                //暂时埋给个坑，以为后台不能相对应提供修改。所以，暂时将item《100以下，默认都为后补
                data = repairData(oldData, data);
            }
            swithKey = "";
            oldData = data;
            mMainTradeContentLFragListener.freshView(data, isCountDown);
//            countDownRefresh(data);
            //暂时不做，没办法完全确认
        }
    }

    /**
     * 修补数据
     *
     * @param oldData 不全的旧数据
     * @param newData 从网咯请求的新数据
     * @return
     */
    private HistoryDataList repairData(HistoryDataList oldData, HistoryDataList newData) {
        List<HistoryData> items = oldData.getItems();
        long lastItemTime = items.get(oldData.getCount() - 1).getT();
        long beginItemTime = items.get(0).getT();
        int period = oldData.getPeriod();
        for (HistoryData historyData : newData.getItems()) {
            items.remove(0);
            items.add(historyData);
        }

        Log.i(TAG, "repairData: 开始时间"+items.get(0).getT()+" 结束时间 "+items.get(items.size()-1).getT());
        for (int i = 0; i < newData.getCount(); i++) {
            items.remove(0);
            items.add(newData.getItems().get(i));
        }
        //修复新加载进来的时间值
        for (int i = 0; i < items.size(); i++) {
            if (i == 0) {
                items.get(0).setT(beginItemTime+60*period*newData.getCount());
            }else {
                items.get(i).setT(items.get(i).getT()-newData.getCount()*60*period);
            }
        }
        Log.i(TAG, "repairData: 开始时间"+items.get(0).getT()+" 结束时间 "+items.get(items.size()-1).getT()+" 数量+"+items.size());
        oldData.setItems(items);
        return oldData;
    }

    @Override
    public void refreshRealTimeView(RealTimeDataList realTimeDataList) {
    }

    /**
     * 刷新头部Indicator
     *
     * @param subTradeSymbol
     */
    @Override
    public void refreshIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol) {
        ArrayList<BeanIndicatorData> mBeanIndicatorDataList = new ArrayList<>();
        mBeanIndicatorDataList.clear();
        BeanIndicatorData mBeanIndicatorData;
        for (BeanSymbolConfig.SymbolsBean symbolsBean : subTradeSymbol) {
            mBeanIndicatorData = new BeanIndicatorData();
            mBeanIndicatorData.setSymbolTag(symbolsBean.getSymbol());
            mBeanIndicatorData.setLeftString(String.valueOf(symbolsBean.getVol_min()));
            mBeanIndicatorData.setRightString(String.valueOf(symbolsBean.getVol_max()));
            mBeanIndicatorData.setImageResource(R.mipmap.ic_instrument_audjpy);
            mBeanIndicatorDataList.add(mBeanIndicatorData);
        }
        mMainTradeContentLFragListener.refreshIndicator(mBeanIndicatorDataList);
    }

    @Override
    public void loadingHistoryData(String symbol, String period, int count) {
        currentSymbol = symbol;
        currentPeriod = period;
        Log.i(TAG, "loadingHistoryData: " + currentPeriod);
        if (aCache == null) {
            aCache = ACache.get(MyApplication.getInstance().getApplicationContext());
        }
        if (timer != null) {
            timer.cancel();
        }
        String asString = aCache.getAsString(symbol.concat(period));
        if (asString == null) {
            Log.i(TAG, "loadHistoryNativeOrNet: 网络加载数据" + symbol + " " + period + " " + count);
            mMainTradeContentModel.sendHistoryRequest(symbol, period, count);
        } else {
            Log.i(TAG, "loadHistoryNativeOrNet: 本地加载数据");
            oldData = new Gson().fromJson(asString, new TypeToken<HistoryDataList>() {
            }.getType());
            int lostCount = (int) (BeanCurrentServerTime.instance.getCurrentServerTime() - (oldData.getItems().get(0).getT() + oldData.getItems().get(oldData.getCount() - 1).getT()) * 1000) / (DataUtil.selectPeriod(period) * 60 * 1000);
            if (lostCount > 0) {
                swithKey = oldData.getSymbol().concat(String.valueOf(oldData.getPeriod())).concat(String.valueOf(lostCount));
                Log.i(TAG, "loadingHistoryData: 本地数据缺少" + lostCount + "  period" + period);
                mMainTradeContentModel.sendHistoryRequest(symbol, period, lostCount);
            } else {
                refreshView(oldData);
            }
        }
    }

    /**
     * 启动倒计时刷新view
     *
     * @param data
     */

    private void countDownRefresh(final HistoryDataList data) {
        //period+传过来的很时间-计算服务器当前时间；就是倒计时时间。
//        HistoryData lastHistoryData = oldData.getItems().get(oldData.getCount() - 1);
//        int period = oldData.getPeriod();
//        long currentServerTime = BeanCurrentServerTime.instance.getCurrentServerTime();
//        if(timer!=null){
//            timer.cancel();
//        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (timer != null) {
                    timer.cancel();
                }
                isCountDown = false;
                long t = data.getItems().get(0).getT();
                long t1 = data.getItems().get(data.getCount() - 1).getT();
                long t2;
                long startTime = ((data.getItems().get(0).getT() + data.getItems().get(data.getCount() - 1).getT() + data.getPeriod() * 60) * 1000 -(t2=BeanCurrentServerTime.instance.getCurrentServerTime()));
                Log.i(TAG, "run: 开始时间  "+t+" 结束时间  "+t1+"  服务器时间"+ t2);
                Log.i(TAG, "countDownRefresh: startTime " + startTime);
                timer = new CountDownTimer(startTime, startTime) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //每隔countDownInterval秒会回调一次onTick()方法
                        Log.i(TAG, "onTick: 定时器开始" + millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        oldData = data;
                        Log.d(TAG, "onFinish -- 倒计时结束");
                        isCountDown = true;
                        mMainTradeContentModel.sendHistoryRequest(data.getSymbol(), DataUtil.selectPeriod(data.getPeriod()), 1);
                    }
                };
                timer.start();// 开始计时
            }
        });
    }
}
