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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanAdapterStream;
import com.xkj.trade.bean_.BeanClosePosition;
import com.xkj.trade.bean_.BeanMasterPosition;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.bean_.BeanPendingPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.TradeDateConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.DateUtils;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        rank = new Gson().fromJson(this.getArguments().getString(MASTER_INFO), BeanMasterRank.MasterRank.class);
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_master_position);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mMyAdapter = new MyAdapter(mOpenDataList));
        mRecyclerView.setFocusable(false);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_type);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        url = UrlConstant.URL_TRADE_MAKET_LIST;
                        mMyAdapter.setData(mOpenDataList);
                        break;
                    case R.id.rb_2:
                        url = UrlConstant.URL_TRADE_PENDING_LIST;
                        mMyAdapter.setData(mPendingDataList);
                        break;
                    case R.id.rb_3:
                        url = UrlConstant.URL_TRADE_HISTORY_LIST;
                        mMyAdapter.setData(mCloseDataList);
                        break;
                }
                mMyAdapter.notifyDataSetChanged();
            }
        });
        requestData();
        countDownTime();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        public MyAdapter(List<BeanMasterPosition> mDataList) {
            this.mDataList = mDataList;
        }

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
            if (Double.valueOf(mDataList.get(position).getProfit()) > 0)
                holder.mTvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
            else
                holder.mTvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
            holder.mImageView.setImageResource(beanMasterPosition.getImageId());
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
                mImageView=(ImageView)itemView.findViewById(R.id.civ_image);
            }
            ImageView mImageView;
            TextView mTvSymbolName;
            TextView mTvOperater;
            TextView mTvOpenPrice;
            TextView mTvProfit;
        }
    }

    BeanOpenPosition beanOpenPosition;
    BeanPendingPosition beanPendingPosition;
    BeanClosePosition beanClosePosition;

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
                    beanOpenPosition = new Gson().fromJson(s, BeanOpenPosition.class);
                    if (beanOpenPosition.getStatus() == 1) {
                        List<BeanMasterPosition> list = new LinkedList<BeanMasterPosition>();
                        for (BeanOpenPosition.DataBean.ListBean bean : beanOpenPosition.getData().getList()) {
                            list.add(new BeanMasterPosition(bean.getSymbol(), bean.getCmd().contains("buy") ? "买" : "卖", bean.getOpenprice(), bean.getProfit()));
                        }
                        responseData(list, UrlConstant.URL_TRADE_MAKET_LIST);
                        mOpenDataList = list;
                    }
                }
                OkhttpUtils.enqueue(URL_TRADE_PENDING_LIST, map, new MyCallBack() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //挂单
                        beanPendingPosition = new Gson().fromJson(response.body().string(), BeanPendingPosition.class);
                        if (beanPendingPosition.getStatus() == 1) {
                            List<BeanMasterPosition> list = new LinkedList<BeanMasterPosition>();
                            for (BeanPendingPosition.DataBean.ListBean bean : beanPendingPosition.getData().getList()) {
                                list.add(new BeanMasterPosition(bean.getSymbol(), bean.getCmd().contains("buy") ? "买" : "卖", bean.getOpenprice(), bean.getProfit()));
                            }
                            responseData(list, URL_TRADE_PENDING_LIST);
                            mPendingDataList = list;
                            OkhttpUtils.enqueue(URL_TRADE_HISTORY_LIST, map, new MyCallBack() {
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //历史
                                    beanClosePosition = new Gson().fromJson(response.body().string(), BeanClosePosition.class);
                                    if (beanClosePosition.getStatus() == 1) {
                                        List<BeanMasterPosition> list = new LinkedList<BeanMasterPosition>();
                                        for (BeanClosePosition.DataBean.ListBean bean : beanClosePosition.getData().getList()) {
                                            list.add(new BeanMasterPosition(bean.getSymbol(), bean.getCmd().contains("buy") ? "买" : "卖", bean.getOpenprice(), bean.getProfit()));
                                        }
                                        responseData(list, URL_TRADE_HISTORY_LIST);
                                        mCloseDataList = list;
                                    }
                                    map.clear();
                                    sortStream();
//                                    EventBus.getDefault().post();
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    //流动表数据
    private void sortStream() {
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        List<BeanAdapterStream> listStream = new ArrayList<>();
        try {

            for (BeanOpenPosition.DataBean.ListBean listBean : beanOpenPosition.getData().getList()) {
                listStream.add(new BeanAdapterStream(String.valueOf(listBean.getOrder()), listBean.getSymbol(), 1, listBean.getOpenprice(), nt.format(MoneyUtil.div(listBean.getProfit(), MoneyUtil.mulPrice(listBean.getVolume(), TradeDateConstant.VOLUME_MONEY_STRING))), listBean.getOpentime(), DataUtil.getImageId(listBean.getSymbol())));

            }
            for (BeanClosePosition.DataBean.ListBean listBean : beanClosePosition.getData().getList()) {
                listStream.add(new BeanAdapterStream(String.valueOf(listBean.getOrder()), listBean.getSymbol(), 2, listBean.getCloseprice(), nt.format(MoneyUtil.div(listBean.getProfit(), MoneyUtil.mulPrice(listBean.getVolume(), TradeDateConstant.VOLUME_MONEY_STRING))), listBean.getClosetime(),DataUtil.getImageId(listBean.getSymbol())));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Collections.sort(listStream, new Comparator<BeanAdapterStream>() {
            @Override
            public int compare(BeanAdapterStream o1, BeanAdapterStream o2) {
                if (DateUtils.getOrderStartTime(o1.getTime()) > DateUtils.getOrderStartTime(o2.getTime())) {
                    return 1;
                } else if (DateUtils.getOrderStartTime(o1.getTime()) == DateUtils.getOrderStartTime(o2.getTime())) {
                    return 0;
                }
                return -1;
            }
        });
        EventBus.getDefault().post(listStream);
    }

    DiffUtil.DiffResult diffResult;

    private void responseData(final List<BeanMasterPosition> dataList, String url) {
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_1:
                if (url.equals(UrlConstant.URL_TRADE_MAKET_LIST)) {
//                        diffResult = DiffUtil.calculateDiff(new MasterPositionDiff(mOpenDataList, dataList), true);
                        mMyAdapter.setData(dataList);
                        diffresult();

                }
                break;
            case R.id.rb_2:
                if (url.equals(URL_TRADE_PENDING_LIST)) {
//                    diffResult = DiffUtil.calculateDiff(new MasterPositionDiff(mPendingDataList, dataList), true);
                    mMyAdapter.setData(dataList);
                    diffresult();
                }
                break;
            case R.id.rb_3:
                if (url.equals(URL_TRADE_HISTORY_LIST)) {
//                    diffResult = DiffUtil.calculateDiff(new MasterPositionDiff(mCloseDataList, dataList), true);
                    mMyAdapter.setData(dataList);
                    diffresult();
                }
                break;
        }
    }

    private void diffresult() {
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                diffResult.dispatchUpdatesTo(mMyAdapter);
                mMyAdapter.notifyDataSetChanged();
            }
        });
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
