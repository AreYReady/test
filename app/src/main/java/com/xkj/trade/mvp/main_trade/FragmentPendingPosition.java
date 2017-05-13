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
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.PendingAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanClosePosition;
import com.xkj.trade.bean_.BeanPendingPosition;
import com.xkj.trade.bean_notification.NotificationDeletePending;
import com.xkj.trade.bean_notification.NotificationEditPendingPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.diffcallback.PendingPositionDiff;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.ThreadHelper;
import com.xkj.trade.utils.ToashUtil;

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
    private TextView mTextView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_position, null);
        return view;
    }

    @Override
    protected void initView() {
        mTextView=(TextView)view.findViewById(R.id.tv_4);
        mTextView.setText(R.string.price);
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
        mSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSwipeRefreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mSwipeRefreshLayout.setLayoutParams(new LinearLayout.LayoutParams(mSwipeRefreshLayout.getWidth(),(int) MainTradeContentActivity.descHeight
                        -view.findViewById(R.id.ll_positions_pending_header_impl).getHeight()
                        -(int)MainTradeContentActivity.flIndicatorHeight));
            }
        });
        requestData();
    }
    private void hideSwipeRefresh(){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void requestData() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_PENDING_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: "+call.request());
                hideSwipeRefresh();
                ToashUtil.show(context,"挂单刷新失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re=response.body().string();
                Log.i(TAG, "onResponse:挂单 "+call.request());
                hideSwipeRefresh();

                info=new Gson().fromJson(re,BeanPendingPosition.class);
                if(info.getStatus()==0){
                    return;
                }
                mDataList=info.getData().getList();
//                SystemUtil.show(re,FragmentPendingPosition.class);
                responseData();
//                requestSubSymbol();
            }
        });
    }

    private void requestSubSymbol() {
        for(BeanPendingPosition.DataBean.ListBean bean:mDataList){
            ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
            if(chartWebSocket!=null){
                if(symbols.get(bean.getSymbol())==null) {
                    symbols.put(bean.getSymbol(),true);
                }
            }
        }
    }

    private void responseData() {
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {

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
        if(beanRealTimeList.getQuotes()==null||mDataList==null){
            return;
        }
        mBeanDupOpenList=  (new Gson().fromJson(new Gson().toJson(mDataList),new TypeToken<List<BeanPendingPosition.DataBean.ListBean>>(){}.getType()));
        for(RealTimeDataList.BeanRealTime beanRealTime:beanRealTimeList.getQuotes()){
            for(int i=0;i<mDataList.size();i++) {
                listBean = mDataList.get(i);
                if(listBean.getSymbol().equals(beanRealTime.getSymbol())){
//                    listBean.setProfit(DataUtil.getProfit(beanRealTime,listBean.getCmd(),listBean.getOpenprice(),listBean.getVolume()));
                    if(listBean.getCmd().contains("buy")){
                        listBean.setPrice(String.valueOf(beanRealTime.getBid()));
                    }else{
                        listBean.setPrice(String.valueOf(beanRealTime.getAsk()));
                    }
                }
            }
        }
            diffResult = DiffUtil.calculateDiff(new PendingPositionDiff(mBeanDupOpenList, mDataList), true);
            ThreadHelper.instance().runOnUiThread(mRunnable);
    }
    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            diffResult.dispatchUpdatesTo(mPendingAdapter);
        }
    };
    @Subscribe
    public void getInform(BeanClosePosition beanClosePosition){
        Log.i(TAG, "getInform: ");
        requestData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationEditPending(NotificationEditPendingPosition notificationEditPendingPosition){
        for(int i=0;i<mDataList.size();i++){
            if(notificationEditPendingPosition.getOrder()==mDataList.get(i).getOrder()){
                if(notificationEditPendingPosition.getTp()!=null)
                mDataList.get(i).setTp(notificationEditPendingPosition.getTp());
                if(notificationEditPendingPosition.getSl()!=null)
                    mDataList.get(i).setSl(notificationEditPendingPosition.getSl());
                if(notificationEditPendingPosition.getPrice()!=null)
                    mDataList.get(i).setProfit(notificationEditPendingPosition.getPrice());
                mPendingAdapter.notifyItemChanged(i);
                break;
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationDeletePending(NotificationDeletePending notificationDeletePending){
        for(int i=0;i<mDataList.size();i++){
            if(notificationDeletePending.getOrder()==mDataList.get(i).getOrder()){
                mDataList.remove(i);
                mPendingAdapter.notifyItemRemoved(i);
                mPendingAdapter.notifyItemRangeChanged(i,mDataList.size());
                break;
            }
        }
    }
    @Subscribe
    public void notificationAddPending(BeanPendingPosition beanPendingPosition){
        //暂时休息一秒，以后在说
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestData();
    }
}
