package com.xkj.trade.mvp.master.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.trade.R;
import com.xkj.trade.adapter.MasterAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_notification.NotificationMasterStatus;
import com.xkj.trade.mvp.master.rank.contract.MasterContract;
import com.xkj.trade.mvp.master.rank.presenter.MasterPresenterImpl;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.Subscribe;

import static com.xkj.trade.base.MyApplication.rank;

/**
 * Created by huangsc on 2016-12-20.
 * TODO:显示社区高手的一些信息和状态线图
 */

public class FragmentMaster extends BaseFragment implements MasterContract.View {
    private RecyclerView mRvMaster;
    private MasterAdapter mMasterAdapter;
    private MasterContract.Presenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


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
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestMasterRank("per");
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(MyApplication.getInstance().rank==null)
        mPresenter.requestMasterRank("per");
        else{
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mRvMaster.setAdapter(mMasterAdapter = new MasterAdapter(context, rank));
                }
            });
        }
    }


    @Override
    public void responseMasterRank(final BeanMasterRank beanMasterRank) {
        rank=beanMasterRank;
        Log.i(TAG, "responseMasterRank: ");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                if(mMasterAdapter==null)
                mRvMaster.setAdapter(mMasterAdapter = new MasterAdapter(context, beanMasterRank));
                else
                    mMasterAdapter.notifyDataSetChanged();
            }
        });
    }
    @Subscribe
    public void getStatusChange(final NotificationMasterStatus notificationWatchStatus){
        if(rank!=null) {
            for (int i = 0; i < rank.getResponse().size(); i++) {
                if (rank.getResponse().get(i).getLogin().equals(notificationWatchStatus.getLogin())) {
                    rank.getResponse().get(i).setFstatus(notificationWatchStatus.getFstatus());
                    rank.getResponse().get(i).setStatus(notificationWatchStatus.getStatus());
                    final int finalI = i;
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMasterAdapter.notifyItemChanged(finalI);
                        }
                    });
                    break;
                }
            }
        }
    }
}
