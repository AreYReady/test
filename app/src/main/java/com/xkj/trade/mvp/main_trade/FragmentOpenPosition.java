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
import com.xkj.trade.adapter.OpenAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.diffcallback.OpenPositionDiff;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DataUtil;
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
 * TODO:持仓
 */

public class FragmentOpenPosition extends BaseFragment {
    private BeanOpenPosition mBeanOpenPosition;
    private List<BeanOpenPosition.DataBean.ListBean> mDataList;
    private RecyclerView mRecyclerView;
    private OpenAdapter mOpenAdapter;
    private Map<String,Boolean> symbols=new TreeMap<>();
    private final int REFRESH_OPEN_POSITION =0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_open_position, null);
        return view;
    }

    @Override
    protected void initView() {
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_open_content);
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestOpenPosition();
            }
        });
        if(mDataList==null){
            mDataList=new ArrayList<BeanOpenPosition.DataBean.ListBean>();
        }
        mOpenAdapter=new OpenAdapter(context,mDataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mOpenAdapter);
        requestOpenPosition();
    }

    private void requestOpenPosition() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_MAKET_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: "+call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re=response.body().string();
                Log.i(TAG, "onResponse: "+call.request());
                mBeanOpenPosition=new Gson().fromJson(re,new TypeToken<BeanOpenPosition>(){}.getType());
                requestSubSymbol();
                responseData();
            }
        });
    }

    /**
     * 发送订阅所有打开仓类型数据。
     */
    private void requestSubSymbol() {
        for(BeanOpenPosition.DataBean.ListBean bean:mBeanOpenPosition.getData().getList()){
            ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
            if(chartWebSocket!=null){
                if(symbols.get(bean.getSymbol())==null) {
                    symbols.put(bean.getSymbol(),true);
                    chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + bean.getSymbol() + "\"}");
                }
            }
        }
    }

    //刷新
    private void responseData(){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mDataList=mBeanOpenPosition.getData().getList();
                mOpenAdapter.setData(mDataList);
                mOpenAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void initData() {
    }
    BeanOpenPosition.DataBean.ListBean listBean;
    DiffUtil.DiffResult diffResult;
    private List<BeanOpenPosition.DataBean.ListBean> mBeanDupOpenList;
    //自己接受实时数据，去更新数据
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getRealTimeData(RealTimeDataList beanRealTimeList){
        Log.i(TAG, "getRealTimeData: ");
        mBeanDupOpenList=  (new Gson().fromJson(new Gson().toJson(mDataList),new TypeToken<List<BeanOpenPosition.DataBean.ListBean>>(){}.getType()));
        for(RealTimeDataList.BeanRealTime beanRealTime:beanRealTimeList.getQuotes()){
            for(int i=0;i<mDataList.size();i++) {
                listBean = mDataList.get(i);
                if(listBean.getSymbol().equals(beanRealTime.getSymbol())){
                   listBean.setProfit(DataUtil.getProfit(beanRealTime,listBean.getCmd(),listBean.getOpenprice(),listBean.getVolume()));
                    if(listBean.getCmd().equals("buy")){
                        listBean.setPrice(String.valueOf(beanRealTime.getBid()));
                    }else{
                        listBean.setPrice(String.valueOf(beanRealTime.getAsk()));
                    }
                }
            }
        }
        diffResult = DiffUtil.calculateDiff(new OpenPositionDiff(mBeanDupOpenList,mDataList), true);
        ThreadHelper.instance().runOnUiThread(mRunnable);
    }
    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            diffResult.dispatchUpdatesTo(mOpenAdapter);
        }
    };
}
