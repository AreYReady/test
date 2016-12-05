package trade.xkj.com.trade.mvp.main_trade.p;

import android.os.Handler;

import java.util.ArrayList;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.constant.TradeDateConstant;
import trade.xkj.com.trade.mvp.main_trade.m.MainTradeContentModel;
import trade.xkj.com.trade.mvp.main_trade.m.MainTradeContentModelmpl;
import trade.xkj.com.trade.mvp.main_trade.v.MainTradeContentLFragListener;

/**
 * Created by admin on 2016-11-23.
 */

public class MainTradeContentPreImpl implements MainTradeContentPre {
    public MainTradeContentLFragListener mMainTradeContentLFragListener;
    public MainTradeContentModel mMainTradeContentModel;
    private final String TAG= SystemUtil.getTAG(this);
    public MainTradeContentPreImpl(MainTradeContentLFragListener mListener,Handler mHandler){
        mMainTradeContentLFragListener=mListener;
        mMainTradeContentModel=new MainTradeContentModelmpl(this,mHandler);
    }

    @Override
    public void loading() {
        mMainTradeContentModel.sendHistoryRequest("AUDCAD", TradeDateConstant.count);
    }


    @Override
    public void refreshView(HistoryDataList data) {
        mMainTradeContentLFragListener.freshView(data);
    }

    @Override
    public void refreshRealTimeView(RealTimeDataList realTimeDataList) {
    }

    /**
     * 刷新头部Indicator
     * @param subTradeSymbol
     */
    @Override
    public void refreshIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol) {
        ArrayList<BeanIndicatorData> mBeanIndicatorDataList=new ArrayList<>();
        mBeanIndicatorDataList.clear();
        BeanIndicatorData mBeanIndicatorData;
        for(BeanSymbolConfig.SymbolsBean symbolsBean:subTradeSymbol){
            mBeanIndicatorData =new BeanIndicatorData();
            mBeanIndicatorData.setSymbolTag(symbolsBean.getSymbol());
            mBeanIndicatorData.setLeftString(String.valueOf(symbolsBean.getVol_min()));
            mBeanIndicatorData.setRightString(String.valueOf(symbolsBean.getVol_max()));
            mBeanIndicatorData.setImageResource(R.mipmap.ic_instrument_audjpy);
            mBeanIndicatorDataList.add(mBeanIndicatorData);
        }
        mMainTradeContentLFragListener.refreshIndicator(mBeanIndicatorDataList);
    }
}
