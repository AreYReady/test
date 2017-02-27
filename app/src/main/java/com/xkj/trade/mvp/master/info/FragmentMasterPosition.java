package com.xkj.trade.mvp.master.info;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanClosePosition;
import com.xkj.trade.bean_.BeanMasterPosition;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.bean_.BeanPendingPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.ThreadHelper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

import static com.xkj.trade.constant.RequestConstant.PROFIT;
import static com.xkj.trade.constant.UrlConstant.URL_TRADE_HISTORY_LIST;
import static com.xkj.trade.constant.UrlConstant.URL_TRADE_PENDING_LIST;
import static com.xkj.trade.mvp.master.info.FragmentMasterInfo.MASTER_INFO;

/**
 * Created by huangsc on 2016-12-27.
 * TODO:
 */

public class FragmentMasterPosition extends BaseFragment {
    private RecyclerView mRecyclerView;
    private String url = UrlConstant.URL_TRADE_MAKET_LIST;
    BeanMasterRank.MasterRank rank;
    private List<BeanMasterPosition> mOpenDataList = new LinkedList<>();
    private List<BeanMasterPosition> mPendingDataList = new LinkedList<>();
    private List<BeanMasterPosition> mCloseDataList = new LinkedList<>();
    private List<BeanMasterPosition> mOpenDupDataList = new LinkedList<>();
    private List<BeanMasterPosition> mPendingDupDataList = new LinkedList<>();
    private List<BeanMasterPosition> mCloseDupDataList = new LinkedList<>();
    private RadioGroup mRadioGroup;
    private Map<String, String> map = new TreeMap<>();
    private MyAdapter mMyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_position, null);
        return view;
    }

    @Override
    protected void initData() {
        rank = new Gson().fromJson(this.getArguments().getString(MASTER_INFO), new TypeToken<BeanMasterRank.MasterRank>() {
        }.getType());
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_master_position);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mMyAdapter = new MyAdapter());
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_type);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        responseData(mOpenDataList, url = UrlConstant.URL_TRADE_MAKET_LIST);
                        break;
                    case R.id.rb_2:
                        responseData(mPendingDataList, url = UrlConstant.URL_TRADE_PENDING_LIST);
                        break;
                    case R.id.rb_3:
                        responseData(mCloseDataList, url = UrlConstant.URL_TRADE_HISTORY_LIST);
                        break;
                }
            }
        });
        requestData();
        countDownTime();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        private List<BeanMasterPosition> mDataList;

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master_position, parent, false));
            return holder;
        }

        public void setData(List<BeanMasterPosition> mDataList) {
            this.mDataList = mDataList;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else {
                Bundle payload = (Bundle) payloads.get(0);
                for (String key : payload.keySet()) {
                    switch (key) {
                        case PROFIT:
                            holder.mTvProfit.setText(mDataList.get(position).getProfit());
                            if (Double.valueOf(mDataList.get(position).getProfit()) > 0)
                                holder.mTvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
                            else
                                holder.mTvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
                            break;
                    }
                }
            }
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            BeanMasterPosition beanMasterPosition = mDataList.get(position);
            holder.mTvSymbolName.setText(beanMasterPosition.getSymbol());
            holder.mTvOpenPrice.setText(beanMasterPosition.getOpenPrice());
            holder.mTvOperater.setText(beanMasterPosition.getOperater());
            holder.mTvProfit.setText(beanMasterPosition.getProfit());
        }

        @Override
        public int getItemCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            public MyHolder(View itemView) {
                super(itemView);
                mTvSymbolName = (TextView) itemView.findViewById(R.id.tv_symbol_name);
                mTvOperater = (TextView) itemView.findViewById(R.id.tv_operater);
                mTvOpenPrice = (TextView) itemView.findViewById(R.id.tv_open_price);
                mTvProfit = (TextView) itemView.findViewById(R.id.tv_profit);
            }

            TextView mTvSymbolName;
            TextView mTvOperater;
            TextView mTvOpenPrice;
            TextView mTvProfit;
        }
    }

    private void requestData() {
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(rank.getLogin()));
        map.put(RequestConstant.IS_PAGE, "1");
        map.put(RequestConstant.PAGE_LIMIT, "50");

        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_MAKET_LIST, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, "onResponse: " + s);
                if (call.request().url().toString().equals(UrlConstant.URL_TRADE_MAKET_LIST)) {
                    //持仓
                    BeanOpenPosition beanOpenPosition = new Gson().fromJson(s, new TypeToken<BeanOpenPosition>() {
                    }.getType());
                    if (beanOpenPosition.getStatus() == 1) {
                        List<BeanMasterPosition> list = new LinkedList<BeanMasterPosition>();
                        for (BeanOpenPosition.DataBean.ListBean bean : beanOpenPosition.getData().getList()) {
                            list.add(new BeanMasterPosition(bean.getSymbol(), bean.getCmd().contains("buy") ? "买" : "卖", bean.getOpenprice(), bean.getProfit()));
                        }
                        mOpenDataList=list;
                        responseData(mOpenDataList, UrlConstant.URL_TRADE_MAKET_LIST);
                    }
                }
                OkhttpUtils.enqueue(URL_TRADE_PENDING_LIST, map, new MyCallBack() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //挂单
                        BeanPendingPosition beanPendingPosition = new Gson().fromJson(response.body().string(), new TypeToken<BeanPendingPosition>() {
                        }.getType());
                        if (beanPendingPosition.getStatus() == 1) {
                            List<BeanMasterPosition> list = new LinkedList<BeanMasterPosition>();
                            for (BeanPendingPosition.DataBean.ListBean bean : beanPendingPosition.getData().getList()) {
                                list.add(new BeanMasterPosition(bean.getSymbol(), bean.getCmd().contains("buy") ? "买" : "卖", bean.getOpenprice(), bean.getProfit()));
                            }
                            mPendingDataList=list;
//                            responseData(list, URL_TRADE_PENDING_LIST);
                            OkhttpUtils.enqueue(URL_TRADE_HISTORY_LIST, map, new MyCallBack() {
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //历史
                                    BeanClosePosition beanClosePosition = new Gson().fromJson(response.body().string(), new TypeToken<BeanClosePosition>() {
                                    }.getType());
                                    if (beanClosePosition.getStatus() == 1) {
                                        List<BeanMasterPosition> list = new LinkedList<BeanMasterPosition>();
                                        for (BeanClosePosition.DataBean.ListBean bean : beanClosePosition.getData().getList()) {
                                            list.add(new BeanMasterPosition(bean.getSymbol(), bean.getCmd().contains("buy") ? "买" : "卖", bean.getOpenprice(), bean.getProfit()));
                                        }
                                        mCloseDataList=list;
//                                        responseData(list, URL_TRADE_HISTORY_LIST);
                                    }
                                    map.clear();
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    DiffUtil.DiffResult diffResult;

    private void responseData(final List<BeanMasterPosition> dataList, String url) {
        if ((url.equals(UrlConstant.URL_TRADE_MAKET_LIST) && dataList == mOpenDataList) || (url.equals(UrlConstant.URL_TRADE_PENDING_LIST) && dataList == mPendingDataList)
                || (url.equals(UrlConstant.URL_TRADE_HISTORY_LIST) && dataList == mCloseDataList)) {
            if (mOpenDupDataList.size() == 0) {
                mMyAdapter.setData(dataList);
                ThreadHelper.instance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMyAdapter.notifyDataSetChanged();
                    }
                });
//                    mOpenDupDataList = mOpenDataList;
//                }else{
//        if (this.url.equals(url)) {
//            mMyAdapter.setData(dataList);
//            if (url.equals(URL_TRADE_MAKET_LIST)) {
//                diffResult = DiffUtil.calculateDiff(new MasterPositionDiff(mOpenDupDataList, dataList), true);
//                mOpenDupDataList = dataList;
//            } else if (url.equals(URL_TRADE_PENDING_LIST)) {
//                diffResult = DiffUtil.calculateDiff(new MasterPositionDiff(mPendingDupDataList, dataList), true);
//                mPendingDupDataList = dataList;
//            } else if (url.equals(URL_TRADE_HISTORY_LIST)) {
//                diffResult = DiffUtil.calculateDiff(new MasterPositionDiff(mCloseDupDataList, dataList), true);
//                mCloseDupDataList = dataList;
//            }
//            ThreadHelper.instance().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    diffResult.dispatchUpdatesTo(mMyAdapter);
//                }
//            });
            }
        }
    }


    private void countDownTime() {
        CountDownTimer timer = new CountDownTimer(10000, 10000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "onTick: ");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: ");
                countDownTime();
                requestData();
            }
        };
        timer.start();
    }
}
