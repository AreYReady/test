package com.xkj.trade.mvp.master.rank.presenter;
import android.content.Context;

import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.mvp.master.rank.contract.MasterContract;
import com.xkj.trade.mvp.master.rank.model.MasterModelImpl;

/**
* Created by huangsc on 2017/02/13
*/

public class MasterPresenterImpl implements MasterContract.Presenter{
    private Context mContext;
    private MasterContract.View mViewlistener;
    private MasterContract.Model mModelListener;
    public MasterPresenterImpl(Context mContext, MasterContract.View listener){
        this.mContext=mContext;
        this.mViewlistener=listener;
        mModelListener=new MasterModelImpl(mContext,this);
    }

    @Override
    public void requestMasterRank(String rankType) {
        mModelListener.requestMasterRank(rankType);
    }

    @Override
    public void responseMasterRank(BeanMasterRank beanMasterRank) {
        mViewlistener.responseMasterRank(beanMasterRank);
    }
}