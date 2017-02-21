package com.xkj.trade.mvp.main_trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.PendingAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanPendingPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.diffcallback.PendingPositionDiff;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentPendingPosition extends BaseFragment  {
    private List<BeanPendingPosition.DataBean.ListBean> mDataList;
    private PendingAdapter mPendingAdapter;
    private RecyclerView mRecyclerView;
    private BeanPendingPosition info;
    private Map<String,Boolean> symbols=new TreeMap<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_position, null);
        return view;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_pending_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mPendingAdapter=new PendingAdapter(context,mDataList));
        requestData();
    }

    private void requestData() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_PENDING_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: "+call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re=response.body().string();
                Log.i(TAG, "onResponse: "+call.request());
                info=new Gson().fromJson(re,new TypeToken<BeanPendingPosition>(){}.getType());
                mDataList=info.getData().getList();
                SystemUtil.show(re,FragmentPendingPosition.class);
                responseData();
                requestSubSymbol();
            }
        });
    }

    private void requestSubSymbol() {
        for(BeanPendingPosition.DataBean.ListBean bean:mDataList){
            ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
            if(chartWebSocket!=null){
                if(symbols.get(bean.getSymbol())==null) {
                    symbols.put(bean.getSymbol(),true);
                    chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + bean.getSymbol() + "\"}");
                }
            }
        }
    }

    private void responseData() {
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mPendingAdapter.setData(mDataList);
                mPendingAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void initData() {
        if(mDataList==null){
            mDataList=new ArrayList<>();
        }
    }
    BeanPendingPosition.DataBean.ListBean listBean;
    DiffUtil.DiffResult diffResult;
    private List<BeanPendingPosition.DataBean.ListBean> mBeanDupOpenList;
    //自己接受实时数据，去更新数据
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getRealTimeData(RealTimeDataList beanRealTimeList){
        Log.i(TAG, "getRealTimeData: ");
        mBeanDupOpenList=  (new Gson().fromJson(new Gson().toJson(mDataList),new TypeToken<List<BeanPendingPosition.DataBean.ListBean>>(){}.getType()));
        for(RealTimeDataList.BeanRealTime beanRealTime:beanRealTimeList.getQuotes()){
            for(int i=0;i<mDataList.size();i++) {
                listBean = mDataList.get(i);
                if(listBean.getSymbol().equals(beanRealTime.getSymbol())){
                    listBean.setProfit(DataUtil.getProfit(beanRealTime,listBean.getCmd(),listBean.getOpenprice(),listBean.getVolume()));
                    if(listBean.getCmd().contains("buy")){
                        listBean.setPrice(String.valueOf(beanRealTime.getBid()));
                    }else{
                        listBean.setPrice(String.valueOf(beanRealTime.getAsk()));
                    }
                }
            }
        }
        diffResult = DiffUtil.calculateDiff(new PendingPositionDiff(mBeanDupOpenList,mDataList), true);
        ThreadHelper.instance().runOnUiThread(mRunnable);
    }
    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            diffResult.dispatchUpdatesTo(mPendingAdapter);
        }
    };
}
