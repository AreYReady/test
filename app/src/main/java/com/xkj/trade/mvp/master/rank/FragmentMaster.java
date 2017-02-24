package com.xkj.trade.mvp.master.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.adapter.MasterAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.mvp.master.rank.contract.MasterContract;
import com.xkj.trade.mvp.master.rank.presenter.MasterPresenterImpl;

/**
 * Created by huangsc on 2016-12-20.
 * TODO:显示社区高手的一些信息和状态线图
 */

public class FragmentMaster extends BaseFragment implements MasterContract.View {
    private RecyclerView mRvMaster;
    private MasterAdapter mMasterAdapter;
    private MasterContract.Presenter mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_masters, null);
        return view;
    }

    @Override
    protected void initView() {
        mRvMaster = (RecyclerView) view.findViewById(R.id.rv_master);
        mRvMaster.setLayoutManager(new LinearLayoutManager(context));
        mPresenter = new MasterPresenterImpl(context, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.requestMasterRank("per");
    }


    @Override
    public void responseMasterRank(final BeanMasterRank beanMasterRank) {
        Log.i(TAG, "responseMasterRank: ");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRvMaster.setAdapter(mMasterAdapter = new MasterAdapter(context, beanMasterRank));
            }
        });

    }

}
