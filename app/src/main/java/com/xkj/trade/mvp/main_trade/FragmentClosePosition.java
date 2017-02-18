package com.xkj.trade.mvp.main_trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.BeanOpenPositionData;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.SystemUtil;

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

public class FragmentClosePosition extends BaseFragment {
    private ViewPager mViewPager;
    private List<BeanOpenPositionData> mDataList;
    private BeanOpenPositionData mBeanOpenPositionData;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_close_position, null);
        return view;
    }

    @Override
    protected void initView() {
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_close_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new CloseAdapter());
        //未知原因，可能是因为ViewDragHelper和recycle多种嵌套导致fragment高度大于父类。暂不探究原理。代码动态计算高度
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(mRecyclerView.getWidth(),(int) MainTradeContentActivity.descHeight
                        -(int)MainTradeContentActivity.flIndicatorHeight -view.findViewById(R.id.v_period_buttons).getHeight()));
            }
        });
        requestData();
    }

    private void requestData() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_HISTORY_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: "+call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re=response.body().string();
                Log.i(TAG, "onResponse: "+call.request());
                SystemUtil.show(re,FragmentClosePosition.class);
//                mBeanOpenPosition=new Gson().fromJson(re,new TypeToken<BeanOpenPosition>(){}.getType());
//                responseData();
            }
        });
    }

    @Override
    protected void initData() {
        mDataList=new ArrayList<>();
        for(int i=0;i<30;i++){
            mBeanOpenPositionData=new BeanOpenPositionData();
            mDataList.add(mBeanOpenPositionData);
        }
    }

    class CloseAdapter extends RecyclerView.Adapter<CloseAdapter.MyViewHolder>{


        @Override
        public CloseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder viewHolder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_close_position,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final CloseAdapter.MyViewHolder holder, final int position) {
            holder.llOnclick.setTag(0);
            holder.llOnclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.llHide.getTag()==null) {
                        holder.llHide.setTag(0);
                    }
                    if(0==(int)holder.llHide.getTag()) {
                        holder.llHide.setVisibility(View.VISIBLE);
                        holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.congratulation_joining_background_dark));
                        holder.llHide.setTag(1);
                    }
                    else{
                        holder.llHide.setVisibility(View.GONE);
                        holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.color_primary_2_light_transparent));
                        holder.llHide.setTag(0);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return mDataList.size();
        }



        class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(View itemView) {
                super(itemView);
                llHide=(LinearLayout)itemView.findViewById(R.id.ll_hide_layout) ;
                llOnclick=(LinearLayout)itemView.findViewById(R.id.ll_onclick) ;
                tvCountyName =(TextView)itemView.findViewById(R.id.tv_county_name);
                tvOperate =(TextView)itemView.findViewById(R.id.tv_operater);
                tvMoney =(TextView)itemView.findViewById(R.id.tv_money);
                tvProfit =(TextView)itemView.findViewById(R.id.tv_profit);
                ivType =(ImageView) itemView.findViewById(R.id.iv_type);
                tvCommission =(TextView)itemView.findViewById(R.id.amount);
                tvStopLoss =(TextView)itemView.findViewById(R.id.stop_loss);
                tvTakeProfit =(TextView)itemView.findViewById(R.id.take_profit);
                tvOpenTime1 =(TextView)itemView.findViewById(R.id.open_time1);
//                tvOpenTime2 =(TextView)itemView.findViewById(R.id.open_time2);
                tvSwap =(TextView)itemView.findViewById(R.id.swap);
                tvOpenRate =(TextView)itemView.findViewById(R.id.open_rate);
            }
            LinearLayout llHide;
            LinearLayout llOnclick;
            TextView tvCountyName;
            TextView tvOperate;
            TextView tvMoney;
            TextView tvProfit;
            ImageView ivType;
            TextView tvCommission;
            TextView tvStopLoss;
            TextView tvTakeProfit;
            TextView tvOpenTime1;
//            TextView tvOpenTime2;
            TextView tvSwap;
            TextView tvOpenRate;
        }
    }
}
