package com.xkj.trade.mvp.main_trade.fragment_content.p;

import android.content.Context;
import android.os.Handler;

import com.xkj.trade.bean.HistoryDataList;
import com.xkj.trade.bean_.BeanHistory;
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
    public void refreshView(BeanHistory data) {
        mMainTradeContentLFragListener.refreshView(data.getData(),false);
    }

    public MainTradeContentPreListenerImpl(MainTradeFragListener.MainTradeContentLFragListener mListener, Handler mHandler, Context context) {
        mMainTradeContentLFragListener = mListener;
        mMainTradeContentModel = new MainTradeContentModelImpl(this,context);
        this.mHandler = mHandler;
        mContext=context;
    }


    @Override
    public void loadingSubSymbols(ArrayList<String> symbolsName,boolean subOrCancel) {
        mMainTradeContentModel.sendSubSymbolsRequest(symbolsName,subOrCancel);
    }


    @Override
    public void loadingHistoryData(String symbol, String period, int count) {
        if(symbol==null){
            mMainTradeContentModel.sendAllSymbolsRequest();
            return;
        }
         mMainTradeContentModel.sendHistoryRequest(symbol,period,count);
    }

    @Override
    public void loadingUserList() {
        mMainTradeContentModel.loadingUserList(ACache.get(mContext).getAsString(RequestConstant.ACCOUNT));
    }

    @Override
    public void userListResponse(BeanUserListInfo info) {
        mMainTradeContentLFragListener.refreshUserInfo(info);
    }

}
