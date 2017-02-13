package com.xkj.trade.diffcallback;

import android.support.v7.util.DiffUtil;

import java.util.List;

import com.xkj.trade.bean.BeanIndicatorData;

/**
 * Created by huangsc on 2017-02-07.
 * TODO:
 */

public class SubSymbolsIndicatorDiff extends DiffUtil.Callback {
    private List<BeanIndicatorData> mOldDatas, mNewDatas;//看名字

    public SubSymbolsIndicatorDiff(List<BeanIndicatorData> mOldDatas, List<BeanIndicatorData> mNewDatas) {
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
        BeanIndicatorData beanOld = mOldDatas.get(oldItemPosition);
        BeanIndicatorData beanNew = mNewDatas.get(newItemPosition);
        if (!beanOld.getAsk().equals(beanNew.getAsk())) {
            return false;//如果有内容不同，就返回false
        }
        if (beanOld.getBid() != beanNew.getBid()) {
            return false;//如果有内容不同，就返回false
        }
        return true; //默认两个data内容是相同的
    }
}
