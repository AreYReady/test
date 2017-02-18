package com.xkj.trade.mvp.main_trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.PendingAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanPendingPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ThreadHelper;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_position, null);
        return view;
    }

    @Override
    protected void initView() {
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
            }
        });
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
}
