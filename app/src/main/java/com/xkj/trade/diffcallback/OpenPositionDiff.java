package com.xkj.trade.diffcallback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.xkj.trade.bean_.BeanOpenPosition;

import java.util.List;

import static com.xkj.trade.constant.RequestConstant.CURRENT_PRICE;
import static com.xkj.trade.constant.RequestConstant.PROFIT;
import static com.xkj.trade.constant.RequestConstant.STOP_LOSS;
import static com.xkj.trade.constant.RequestConstant.TAKE_PROFIT;

/**
 * Created by huangsc on 2017-02-07.
 * TODO:
 */

public class OpenPositionDiff extends DiffUtil.Callback {
    private List<BeanOpenPosition.DataBean.ListBean> mOldDatas, mNewDatas;//看名字

    public OpenPositionDiff(List<BeanOpenPosition.DataBean.ListBean> mOldDatas, List<BeanOpenPosition.DataBean.ListBean> mNewDatas) {
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
        return mOldDatas.get(oldItemPosition).getOrder()==mNewDatas.get(newItemPosition).getOrder();
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BeanOpenPosition.DataBean.ListBean beanOld = mOldDatas.get(oldItemPosition);
        BeanOpenPosition.DataBean.ListBean beanNew = mNewDatas.get(newItemPosition);
        if(!beanOld.getProfit().equals(beanNew.getProfit())){
            return false;
        }
        if(!beanOld.getSl().equals(beanNew.getSl()))
            return false;
        if(!beanOld.getTp().equals(beanNew.getTp()))
            return false;
        return true; //默认两个data内容是相同的
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        BeanOpenPosition.DataBean.ListBean beanOld = mOldDatas.get(oldItemPosition);
        BeanOpenPosition.DataBean.ListBean beanNew = mNewDatas.get(newItemPosition);
        Bundle payload = new Bundle();
        if(!beanOld.getProfit().equals(beanNew.getProfit())){
            payload.putString(PROFIT,PROFIT);
        }
        payload.putString(CURRENT_PRICE,CURRENT_PRICE);
        if(!beanOld.getTp().equals(beanNew.getTp())){
            payload.putString(TAKE_PROFIT,TAKE_PROFIT);
        }
        if(!beanOld.getSl().equals(beanNew.getSl()))
            payload.putString(STOP_LOSS,STOP_LOSS);
        if (payload.size() == 0)//如果没有变化 就传空
            return null;
        return payload;
    }
}
