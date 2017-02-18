package com.xkj.trade.mvp.master;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.BeanPortfolioData;
import com.xkj.trade.utils.view.CustomPortfolio;

import java.util.ArrayList;

/**
 * Created by huangsc on 2016-12-27.
 * TODO:
 */

public class FragmentMasterPortfolio extends BaseFragment {
CustomPortfolio customPortfolio;
    ArrayList<BeanPortfolioData> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_portfolio, null);
        return view;
    }

    @Override
    protected void initData() {
        list=new ArrayList<>();
        int begin=-180;
        for(int i=0;i<6;i++) {
            BeanPortfolioData data = new BeanPortfolioData();
            data.setBeginAngle(begin);
            data.setSweepAngle(60);
            begin = begin+60;
            Log.i("hsc", "onCreate: "+begin);
            data.setSymbol("huangsc");
            data.setType("测试");
            list.add(data);
        }

    }

    @Override
    protected void initView() {
        customPortfolio=(CustomPortfolio)  view.findViewById(R.id.cp_portfolio);
        customPortfolio.setData(list);
    }


}