package com.xkj.trade.mvp.main_trade.fragment_content.p;

import android.content.Context;
import android.os.Handler;

import com.xkj.trade.bean.HistoryDataList;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.bean_.BeanUserListInfo;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeFragListener;
import com.xkj.trade.mvp.main_trade.fragment_content.m.MainTradeContentModelImpl;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.SystemUtil;

import java.util.ArrayList;

/**
 * Created by hsc on 2016-11-23.
 * TODO:
 */

public class MainTradeContentPreListenerImpl implements MainTradeFragListener.MainTradeContentPreListener {
    public MainTradeFragListener.MainTradeContentLFragListener mMainTradeContentLFragListener;
    public MainTradeFragListener.MainTradeContentModelListener mMainTradeContentModel;
    private Handler mHandler;
    private final String TAG = SystemUtil.getTAG(this);
    private HistoryDataList oldData;
    private Context mContext;
    @Override
    public void responseHistoryData(BeanHistory data) {
        mMainTradeContentLFragListener.refreshView(data.getData(),false);
    }

    public MainTradeContentPreListenerImpl(MainTradeFragListener.MainTradeContentLFragListener mListener, Handler mHandler, Context context) {
        mMainTradeContentLFragListener = mListener;
        mMainTradeContentModel = new MainTradeContentModelImpl(this,context);
        this.mHandler = mHandler;
        mContext=context;
    }


    @Override
    public void requestSubSymbols(ArrayList<String> symbolsName, boolean subOrCancel) {
        mMainTradeContentModel.requestSubSymbolsData(symbolsName,subOrCancel);
    }


    @Override
    public void requestHistoryData(String symbol, String period, int count) {
        if(symbol==null){
            mMainTradeContentModel.requestAllSymbolsData();
            return;
        }
         mMainTradeContentModel.requestHistoryData(symbol,period,count);
    }

    @Override
    public void requestUserList() {
        mMainTradeContentModel.requestUserListData(ACache.get(mContext).getAsString(RequestConstant.ACCOUNT));
    }

    @Override
    public void ResponseUserList(BeanUserListInfo info) {
        mMainTradeContentLFragListener.refreshUserInfo(info);
    }

    @Override
    public void requestOpenPosition() {
        mMainTradeContentModel.requestOpenPositionData();
    }

    @Override
    public void responseOpenPosition(BeanOpenPosition beanOpenPosition) {
        mMainTradeContentLFragListener.refreshOpenPosition(beanOpenPosition);
    }

    @Override
    public void responseAllSymbolsData(String response) {
        mMainTradeContentLFragListener.responseAllSymbolsData(response);
    }

}
