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
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.CloseAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanClosePosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.diffcallback.ClosePositionDiff;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DateUtils;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_close_position, null);
        return view;
    }

    @Override
    protected void initView() {
        mRadioGroup=(RadioGroup)view.findViewById(R.id.period_group);
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.days_7:
                        mBeginOpenTime=  DateUtils.getShowTime((Calendar.getInstance().getTimeInMillis()-7*24*60*60*1000),"yyyy-MM-dd HH:mm:ss");
                        Log.i(TAG, "onCheckedChanged: "+mBeginOpenTime+"   end"+DateUtils.getCurrenTime());
                        break;
                    case R.id.days_14:
                        mBeginOpenTime=  DateUtils.getShowTime((Calendar.getInstance().getTimeInMillis()-14*24*60*60*1000),"yyyy-MM-dd HH:mm:ss");
                        Log.i(TAG, "onCheckedChanged: "+mBeginOpenTime+"   end"+DateUtils.getCurrenTime());
                        break;
                    case R.id.month_1:
//                        mBeginOpenTime= DateUtils.getShowTime((Calendar.getInstance().getTimeInMillis()-30*24*60*60*1000),"yyyy-MM-dd HH:mm:ss");
                        mBeginOpenTime= DateUtils.getShowTime((Calendar.getInstance().getTimeInMillis()-(long)30*24*60*60*1000),"yyyy-MM-dd HH:mm:ss");
                        break;
                    case R.id.all:
                        mBeginOpenTime=null;
                        break;
                }
                requestData();
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_close_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (mCloseAdapter == null) {
            mCloseAdapter = new CloseAdapter(context, mDataList);
        }
        mRecyclerView.setAdapter(mCloseAdapter);
        requestData();
    }

    private void requestData() {
        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        if(mBeginOpenTime!=null){
            map.put(RequestConstant.OPEN_START_TIME,AesEncryptionUtil.stringBase64toString(mBeginOpenTime));
            map.put(RequestConstant.OPEN_END_TIME,AesEncryptionUtil.stringBase64toString(DateUtils.getCurrenTime()));
        }
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_HISTORY_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re = response.body().string();
                Log.i(TAG, "onResponse: " + call.request());
                Log.i(TAG, "onResponse: "+re);
                SystemUtil.show(re, FragmentClosePosition.class);
                mBeanClosePosition = new Gson().fromJson(re, new TypeToken<BeanClosePosition>() {
                }.getType());
                responseData();
            }
        });
    }

    DiffUtil.DiffResult mDiffResult;

    private void responseData() {
        //使用
        mDataList = mBeanClosePosition.getData().getList();
        mDupDataList = new Gson().fromJson(new Gson().toJson(mDataList), new TypeToken<List<BeanClosePosition.DataBean.ListBean>>() {
        }.getType());
        mCloseAdapter.setData(mDataList);
        mDiffResult = DiffUtil.calculateDiff(new ClosePositionDiff(mDupDataList, mDataList), true);
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                if(mDataList.size()==0){
                mCloseAdapter.notifyDataSetChanged();
                }else {
                    mDiffResult.dispatchUpdatesTo(mCloseAdapter);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mDataList = new ArrayList<>();
        mBeginOpenTime=  DateUtils.getShowTime((Calendar.getInstance().getTimeInMillis()-7*24*60*60*1000),"yyyy-MM-dd HH:mm:ss");
    }

}
