package com.xkj.trade.mvp.main_trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.xkj.trade.bean.BeanAttentionTraderData;
import com.xkj.trade.bean_.BeanWatchInfo;
import com.xkj.trade.bean_notification.NotificationWatchStatus;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

import static org.greenrobot.eventbus.ThreadMode.MAIN;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentMasterWatch extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<BeanWatchInfo.ResponseBean> mDataList;
    private BeanAttentionTraderData mData;
    private WatchAdapter mWatchAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_1,null);
        return view;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_item_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mWatchAdapter=new WatchAdapter(context,mDataList));
        mWatchAdapter.setWacthListener(new WatchAdapter.WatchListener() {
            @Override
            public void unWatch(final int position) {
                ThreadHelper.instance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在这个列表里。一定是未复制的单位，所以最后的参数为0,且是取消关注，一定为0
                        EventBus.getDefault().post(new NotificationWatchStatus(mDataList.get(position).getFocusid(),0,0));
                        mDataList.remove(position);
                        mWatchAdapter.notifyItemRemoved(position);
                        mWatchAdapter.notifyItemRangeChanged(position,mDataList.size()-1);
//                        mWatchAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(mRecyclerView.getWidth(),(int) MainTradeContentActivity.descHeight
                        -(int)MainTradeContentActivity.flIndicatorHeight));
            }
        });
        requestWatch();
    }

    private void requestWatch() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.ACCOUNT, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_FOCUS_INFO, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               BeanWatchInfo beanWatchInfo= new Gson().fromJson(response.body().string(),new TypeToken<BeanWatchInfo>(){}.getType());
                if(beanWatchInfo.getStatus()==1) {
                    responseWatch(beanWatchInfo);
                }
            }
        });
    }

    private void responseWatch(BeanWatchInfo beanWatchInfo) {
        for(BeanWatchInfo.ResponseBean bean:beanWatchInfo.getResponse()){
            if(bean.getStatus()==0)
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
        mDataList=new ArrayList<>();
    }
    @Subscribe(threadMode = MAIN)
    public void notificationWatchChanger(BeanWatchInfo.ResponseBean responseBean){
        if(responseBean.getStatus()==1){
            //增加
                   mDataList.add(0,responseBean);
                   mWatchAdapter.notifyItemInserted(0);
                   mWatchAdapter.notifyItemRangeChanged(0,mDataList.size()-1);
        }else{
            //删除
            for(int i=0;i<mDataList.size();i++){
                if(responseBean.getName().equals(mDataList.get(i).getName())){
                    mDataList.remove(i);
                    mWatchAdapter.notifyItemRemoved(i);
                    mWatchAdapter.notifyItemRangeChanged(i,mDataList.size()-1);
                }
            }

        }
    }
    private List<String> list;
}
