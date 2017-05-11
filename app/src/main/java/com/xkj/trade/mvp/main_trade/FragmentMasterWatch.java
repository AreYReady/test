package com.xkj.trade.mvp.main_trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.WatchAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanAttentionTraderData;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_.BeanWatchInfo;
import com.xkj.trade.bean_notification.NotificationMasterStatus;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.mvp.master.info.FragmentMasterInfo;
import com.xkj.trade.mvp.master.info.MasterInfoActivity;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.xkj.trade.base.MyApplication.rank;
import static com.xkj.trade.constant.UrlConstant.URL_MT4_RANKING;
import static org.greenrobot.eventbus.ThreadMode.BACKGROUND;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentMasterWatch extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<BeanWatchInfo.ResponseBean> mDataList;
    private BeanAttentionTraderData mData;
    private WatchAdapter mWatchAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, null);
        return view;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_item_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mWatchAdapter = new WatchAdapter(context, mDataList));
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWatch();
            }
        });
        mWatchAdapter.setWacthListener(new WatchAdapter.WatchListener() {
            @Override
            public void unWatch(final int position) {
//                ThreadHelper.instance().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //在这个列表里。一定是未复制的单位，所以最后的参数为0,且是取消关注，一定为0
//                        EventBus.getDefault().post(new NotificationMasterStatus(mDataList.get(position).getFocusid(), 0, 0));
//                        mDataList.remove(position);
//                        mWatchAdapter.notifyItemRemoved(position);
//                        mWatchAdapter.notifyItemRangeChanged(0, mDataList.size());
////                        mWatchAdapter.notifyDataSetChanged();
//                    }
//                });
            }

            @Override
            public void copy(String focuid) {
                Log.i(TAG, "copy: ");
                if (rank != null) {
                    for (int i = 0; i < rank.getResponse().size(); i++) {
                        if (focuid.equals(rank.getResponse().get(i).getLogin())) {
                            Intent intent = new Intent(context, MasterInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(FragmentMasterInfo.MASTER_INFO, new Gson().toJson(rank.getResponse().get(i)));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        }
                    }
                }
            }
        });
        //未知原因，可能是因为ViewDragHelper和recycle多种嵌套导致fragment高度大于父类。暂不探究原理。代码动态计算高度
        mSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSwipeRefreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mSwipeRefreshLayout.setLayoutParams(new RelativeLayout.LayoutParams(mSwipeRefreshLayout.getWidth(),(int) MainTradeContentActivity.descHeight
                        -(int)MainTradeContentActivity.flIndicatorHeight));
            }
        });
        requestWatch();
        requestMasterRank();
    }

    private void requestWatch() {
        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.ACCOUNT, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_FOCUS_INFO, map, new MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                hideSwipeRefresh();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                hideSwipeRefresh();
                String s = AesEncryptionUtil.decodeUnicode(response.body().string());
                BeanWatchInfo beanWatchInfo = new Gson().fromJson(s, new TypeToken<BeanWatchInfo>(){}.getType());
                Log.i(TAG, "onResponse: 高手关注"+s);
                if (beanWatchInfo.getStatus() == 1) {
                    responseWatch(beanWatchInfo);
                }else{
                    if(s.contains("no data")){
                        mDataList.clear();
                        ThreadHelper.instance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWatchAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }
    private void hideSwipeRefresh(){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void responseWatch(BeanWatchInfo beanWatchInfo) {
        mDataList=new ArrayList<>();
        for (BeanWatchInfo.ResponseBean bean : beanWatchInfo.getResponse()) {
            if (bean.getStatus() == 0)
                mDataList.add(bean);
        }
        mWatchAdapter.setData(mDataList);
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWatchAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        mDataList = new ArrayList<>();
    }

    @Subscribe(threadMode = BACKGROUND)
    public void notificationWatchChanger(NotificationMasterStatus notificationMasterStatus) {
        if (notificationMasterStatus.getFstatus() == 1) {
            //增加
            for (BeanMasterRank.MasterRank masterRank : rank.getResponse()) {
                if (masterRank.getLogin().equals(notificationMasterStatus.getLogin())) {
                    //首先符合login,然后符合未复制
                    if (notificationMasterStatus.getStatus() == 1) {
                        //判断是否是复制状态，是则执行删除
                        deleteItem(notificationMasterStatus);
                    } else {
                        //不是复制状态，遍历
                        //最后如果已有列表不存在，则增加
                        if(!isExistItem(notificationMasterStatus,masterRank)){
                            mDataList.add(0, new BeanWatchInfo.ResponseBean(notificationMasterStatus.getFstatus(), masterRank.getFace_url(), masterRank.getCopynumber(), notificationMasterStatus.getLogin(), masterRank.getName()));
                            notifyDataSetChange();
                        }
                    }
                }
            }
        } else {
            //删除
            if (notificationMasterStatus.getStatus() == 0) {
                //处于关注状态，还是处于非复制状态
                Log.i(TAG, "notificationWatchChanger: ");
                deleteItem(notificationMasterStatus);
            }
        }
    }

    private void notifyDataSetChange(){
        ThreadHelper.instance().runOnUiThread(mRunnable);
    }
    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            mWatchAdapter.notifyDataSetChanged();
        }
    };
    private boolean isExistItem(NotificationMasterStatus notificationMasterStatus,BeanMasterRank.MasterRank masterRank){
        for (BeanWatchInfo.ResponseBean responseBean : mDataList) {
            //最后如果已有列表不存在，则增加
            if (responseBean.getFocusid().equals(notificationMasterStatus.getLogin())) {
                return true;
            }
        }
        return false;
    }
    private void deleteItem(NotificationMasterStatus notificationMasterStatus) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (notificationMasterStatus.getLogin().equals(mDataList.get(i).getFocusid())) {
                mDataList.remove(i);
                notifyDataSetChange();
            }
        }
    }

    private List<String> list;

    private void requestMasterRank() {
        if (MyApplication.getInstance().rank == null) {
            Log.i(TAG, "requestMasterRank: ");
            Map<String, String> map = new TreeMap<>();
            map.put(RequestConstant.LOGIN, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
            map.put(RequestConstant.RANK_TYPE, "per");
//          map.put(RequestConstant.API_SIGN, AesEncryptionUtil.getApiSign(URL_MT4_RANKING, map));
            OkhttpUtils.enqueue(URL_MT4_RANKING, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "onFailure: " + call.request());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Log.i(TAG, "onResponse:高手排行版" + (s));
                    BeanMasterRank info = new Gson().fromJson(s, new TypeToken<BeanMasterRank>(){}.getType());
                    if (info.getStatus() == 1) {
                        rank = info;
                    }
                }
            });
        }
    }
}
