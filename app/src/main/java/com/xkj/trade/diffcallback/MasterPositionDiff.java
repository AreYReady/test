package com.xkj.trade.diffcallback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.xkj.trade.bean_.BeanMasterPosition;

import java.util.List;

import static com.xkj.trade.constant.RequestConstant.CURRENT_PRICE;
import static com.xkj.trade.constant.RequestConstant.PROFIT;

/**
 * Created by huangsc on 2017-02-07.
 * TODO:高手仓位信息
 */

public class MasterPositionDiff extends DiffUtil.Callback {
    private List<BeanMasterPosition> mOldDatas, mNewDatas;//看名字

    public MasterPositionDiff(List<BeanMasterPosition> mOldDatas, List<BeanMasterPosition> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    //老数据集size
    @Override
    public int getOldListSize() {
        return mOldDatas != null ? mOldDatas.size() : 0;
    }

    //新数据集size
    @Override
    public int getNewListSize() {
        return mNewDatas != null ? mNewDatas.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDatas.get(oldItemPosition).getSymbol().equals(mNewDatas.get(newItemPosition).getSymbol());
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BeanMasterPosition beanOld = mOldDatas.get(oldItemPosition);
        BeanMasterPosition beanNew = mNewDatas.get(newItemPosition);
        if(!beanOld.getProfit().equals(beanNew.getProfit())){
            return false;
        }
        if(!beanNew.getOpenPrice().equals(beanNew.getOpenPrice())){
            return false;
        }

        return true; //默认两个data内容是相同的
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        BeanMasterPosition beanOld = mOldDatas.get(oldItemPosition);
        BeanMasterPosition beanNew = mNewDatas.get(newItemPosition);
        Bundle payload = new Bundle();
        if(!beanOld.getProfit().equals(beanNew.getProfit())){
            payload.putString(PROFIT,PROFIT);
        }
        if(!beanNew.getOpenPrice().equals(beanNew.getOpenPrice())) {
            payload.putString(CURRENT_PRICE, CURRENT_PRICE);
        }
        if (payload.size() == 0)//如果没有变化 就传空
            return null;
        return payload;
    }
}
