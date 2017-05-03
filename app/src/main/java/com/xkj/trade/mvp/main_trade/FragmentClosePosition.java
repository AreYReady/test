package com.xkj.trade.mvp.main_trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.CloseAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanClosePosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DateUtils;
import com.xkj.trade.utils.ThreadHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentClosePosition extends BaseFragment {
    private ViewPager mViewPager;
    private List<BeanClosePosition.DataBean.ListBean> mDataList;
    private BeanClosePosition mBeanClosePosition;
    private List<BeanClosePosition.DataBean.ListBean> mDupDataList;
    private RecyclerView mRecyclerView;
    private CloseAdapter mCloseAdapter;
    private RadioGroup mRadioGroup;
    private String mBeginOpenTime;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BeanClosePosition sevenDay = new BeanClosePosition();
    private BeanClosePosition fourteenDay = new BeanClosePosition();
    private BeanClosePosition oneMonth = new BeanClosePosition();
    private BeanClosePosition allTime = new BeanClosePosition();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_close_position, null);
        return view;
    }

    @Override
    protected void initView() {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.period_group);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (mRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.days_7:
                        requestSevenDay();
                        break;
                    case R.id.days_14:
                        requestFourteenDay();
                        break;
                    case R.id.month_1:
                        requestMonthDay();
                        break;
                    case R.id.all:
                        requestAllDay();
                        break;
                }
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.days_7:
                        responseData(sevenDay);
                        break;
                    case R.id.days_14:
                        responseData(fourteenDay);
                        break;
                    case R.id.month_1:
                        responseData(oneMonth);
                        break;
                    case R.id.all:
                        responseData(allTime);
                        break;
                }
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_close_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (mCloseAdapter == null) {
            mCloseAdapter = new CloseAdapter(context, mDataList);
            mRecyclerView.setAdapter(mCloseAdapter);
        }
        requestSevenDay();
        requestFourteenDay();
        requestMonthDay();
        requestAllDay();
    }

    private void requestSevenDay() {
        final Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        String showTimeNoTimeZone = DateUtils.getShowTimeNoTimeZone((Calendar.getInstance().getTimeInMillis() - (long) 7 * 24 * 60 * 60 * 1000));
        String currenTime = DateUtils.getCurrenTime();
        Log.i(TAG, "requestSevenDay: "+showTimeNoTimeZone+currenTime);
        map.put(RequestConstant.OPEN_START_TIME, AesEncryptionUtil.stringBase64toString(DateUtils.getShowTimeNoTimeZone((Calendar.getInstance().getTimeInMillis() - (long)7*24*60*60*1000))));
        map.put(RequestConstant.OPEN_END_TIME, AesEncryptionUtil.stringBase64toString(DateUtils.getCurrenTime()));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_HISTORY_LIST, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sevenDay = new Gson().fromJson(response.body().string(), BeanClosePosition.class);
                if(mRadioGroup.getCheckedRadioButtonId()==R.id.days_7){
                    responseData(sevenDay);
                }
            }
        });
    }

    private void requestFourteenDay() {
        final Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.OPEN_START_TIME, AesEncryptionUtil.stringBase64toString(DateUtils.getShowTimeNoTimeZone((Calendar.getInstance().getTimeInMillis() - (long)14*24*60*60*1000))));
        map.put(RequestConstant.OPEN_END_TIME, AesEncryptionUtil.stringBase64toString(DateUtils.getCurrenTime()));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_HISTORY_LIST, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                fourteenDay = new Gson().fromJson(response.body().string(), BeanClosePosition.class);
                if(mRadioGroup.getCheckedRadioButtonId()==R.id.days_14){
                    responseData(fourteenDay);
                }
            }
        });
    }

    private void requestMonthDay() {
        final Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.OPEN_START_TIME, AesEncryptionUtil.stringBase64toString(DateUtils.getShowTimeNoTimeZone((Calendar.getInstance().getTimeInMillis()-(long)30*24*60*60*1000))));
        map.put(RequestConstant.OPEN_END_TIME, AesEncryptionUtil.stringBase64toString(DateUtils.getCurrenTime()));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_HISTORY_LIST, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();
                oneMonth = new Gson().fromJson(s, BeanClosePosition.class);
                if(mRadioGroup.getCheckedRadioButtonId()==R.id.month_1){
                    responseData(oneMonth);
                }
            }
        });
    }

    private void requestAllDay() {
        final Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_HISTORY_LIST, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                allTime = new Gson().fromJson(response.body().string(), BeanClosePosition.class);
                if(mRadioGroup.getCheckedRadioButtonId()==R.id.all){
                    responseData(allTime);
                }
            }
        });
    }

    private boolean checkDataFit(Map<String, String> map) {
        if (mBeginOpenTime == null && map.get(RequestConstant.OPEN_START_TIME) == null) {
            Log.i(TAG, "checkDataFit: ");
            return true;
        }
        String s = AesEncryptionUtil.stringBase64toString(mBeginOpenTime);
        if (AesEncryptionUtil.stringBase64toString(mBeginOpenTime).equals(map.get(RequestConstant.OPEN_START_TIME))) {
            Log.i(TAG, "checkDataFit: " + s);
            return true;
        }
        return false;
    }

    DiffUtil.DiffResult mDiffResult;

    private void responseData(BeanClosePosition beanClosePosition) {
        //使用
        mDataList =beanClosePosition.getData().getList();
        mCloseAdapter.setData(mDataList);
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mCloseAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        mDataList = new ArrayList<>();
    }
}
