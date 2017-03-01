package com.xkj.trade.mvp.main_trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.CopyAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.BeanAttentionTraderData;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.utils.ACache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentMasterCopy extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<BeanAttentionTraderData> mDataList;
    private BeanAttentionTraderData mData;
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
        mRecyclerView.setAdapter(new CopyAdapter(context,mDataList));
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(mRecyclerView.getWidth(),(int) MainTradeContentActivity.descHeight
                        -(int)MainTradeContentActivity.flIndicatorHeight));
            }
        });
        requestData();
    }

    private void requestData() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.ACCOUNT, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_MY_COPY, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: "+response.body().string());
            }
        });
    }

    @Override
    protected void initData() {
        mDataList=new ArrayList<>();
        for(int i=0;i<20;i++){
            mData=new BeanAttentionTraderData();
            mData.setCopyCount(100);
            mData.setName("huangshunchao");
            mData.setImageResouce(R.mipmap.menu_avatar);
            mData.setButtonText("取消关注");
            mDataList.add(mData);
        }
    }
   private void requestMasterCopy(){

    }
}
