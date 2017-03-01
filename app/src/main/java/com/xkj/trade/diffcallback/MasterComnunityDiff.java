package com.xkj.trade.diffcallback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.xkj.trade.bean_.BeanAdapterComnunity;

import java.util.List;

import static com.xkj.trade.constant.RequestConstant.TAKE_PROFIT;

/**
 * Created by huangsc on 2017-03-01.
 * TODO:
 */

public class MasterComnunityDiff extends DiffUtil.Callback {
    private List<BeanAdapterComnunity> mOldDatas, mNewDatas;//看名字

    public MasterComnunityDiff(List<BeanAdapterComnunity> mOldDatas, List<BeanAdapterComnunity> mNewDatas) {
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
        return mOldDatas.get(oldItemPosition).getName().equals(mNewDatas.get(newItemPosition).getName());
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BeanAdapterComnunity beanOld = mOldDatas.get(oldItemPosition);
        BeanAdapterComnunity beanNew = mNewDatas.get(newItemPosition);
        if(!beanOld.getTp().equals(beanNew.getTp())){
            return false;
        }
        return true; //默认两个data内容是相同的
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        BeanAdapterComnunity beanOld = mOldDatas.get(oldItemPosition);
        BeanAdapterComnunity beanNew = mNewDatas.get(newItemPosition);
        Bundle payload = new Bundle();
        if(!beanOld.getTp().equals(beanNew.getTp())){
            payload.putString(TAKE_PROFIT,TAKE_PROFIT);
        }
        if (payload.size() == 0)//如果没有变化 就传空
            return null;
        return payload;
    }
}
