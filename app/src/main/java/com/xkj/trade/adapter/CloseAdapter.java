package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanClosePosition;
import com.xkj.trade.utils.SystemUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by huangsc on 2017-02-16.
 * TODO:
 */

public class CloseAdapter extends RecyclerView.Adapter<CloseAdapter.MyViewHolder>{
    private Context mContext;
    private List<BeanClosePosition.DataBean.ListBean> mDataList;
    private Map<Integer,Boolean> isClick=new ArrayMap<Integer,Boolean>();
    private  String TAG= SystemUtil.getTAG(this);
    private BeanClosePosition.DataBean.ListBean mData;
    public CloseAdapter(Context context,List<BeanClosePosition.DataBean.ListBean> mDataList){
        mContext=context;
        this.mDataList=mDataList;
    }

    @Override
    public CloseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CloseAdapter.MyViewHolder viewHolder=new CloseAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item_social_card_close_position,parent,false));
        return viewHolder;
    }
    public void setData(List<BeanClosePosition.DataBean.ListBean> mDataList){
        this.mDataList=mDataList;
    }


    @Override
    public void onBindViewHolder(final CloseAdapter.MyViewHolder holder, final int position) {
        mData=mDataList.get(position);
        holder.llOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick.containsKey(position)){
                    isClick.remove(position);
                    holder.llHide.setVisibility(View.GONE);
                    holder.llOnclick.setBackgroundColor(mContext.getResources().getColor(R.color.color_primary_2_light_transparent));
                }else{
                    isClick.put(position,true);
                    holder.llHide.setVisibility(View.VISIBLE);
                    holder.llOnclick.setBackgroundColor(mContext.getResources().getColor(R.color.congratulation_joining_background_dark));
                }
            }
        });
        if(isClick.containsKey(position)){
            holder.llHide.setVisibility(View.VISIBLE);
            holder.llOnclick.setBackgroundColor(mContext.getResources().getColor(R.color.congratulation_joining_background_dark));
        }else{
            holder.llHide.setVisibility(View.GONE);
            holder.llOnclick.setBackgroundColor(mContext.getResources().getColor(R.color.color_primary_2_light_transparent));
        }
        holder.tvCountyName.setText(mData.getSymbol());
        if (mData.getCmd().contains("sell")) {
            holder.tvOperate.setText("卖");
            holder.tvOperate.setTextColor(mContext.getResources().getColor(R.color.text_color_price_fall));
        } else {
            holder.tvOperate.setTextColor(mContext.getResources().getColor(R.color.text_color_price_rise));
            holder.tvOperate.setText("买");
        }
        holder.tvMoney.setText(mData.getVolume());
        holder.tvProfit.setText(mData.getProfit());
        if(Double.valueOf(mData.getProfit())>0)
            holder.tvProfit.setTextColor(mContext.getResources().getColor(R.color.text_color_price_rise));
        else
            holder.tvProfit.setTextColor(mContext.getResources().getColor(R.color.text_color_price_fall));
        holder.tvCommission.setText(mData.getCommission());
        holder.tvStopLoss.setText(mData.getSl());
        holder.tvTakeProfit.setText(mData.getTp());
        holder.tvOpenTime1.setText(mData.getOpentime());
        holder.tvSwap.setText(mData.getSwap());
        holder.tvOpenRate.setText(mData.getOpenprice());
        holder.tvCloseTime.setText(mData.getClosetime());
        holder.tvCloseRate.setText(mData.getCloseprice());
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
            tvCountyName =(TextView)itemView.findViewById(R.id.tv_symbol_name);
            tvOperate =(TextView)itemView.findViewById(R.id.tv_operater);
            tvMoney =(TextView)itemView.findViewById(R.id.tv_money);
            tvProfit =(TextView)itemView.findViewById(R.id.tv_profit);
            ivType =(ImageView) itemView.findViewById(R.id.iv_type);
            tvCommission =(TextView)itemView.findViewById(R.id.amount);
            tvStopLoss =(TextView)itemView.findViewById(R.id.stop_loss);
            tvTakeProfit =(TextView)itemView.findViewById(R.id.take_profit);
            tvOpenTime1 =(TextView)itemView.findViewById(R.id.open_time1);
            tvSwap =(TextView)itemView.findViewById(R.id.swap);
            tvOpenRate =(TextView)itemView.findViewById(R.id.open_rate);
            tvCloseTime=(TextView)itemView.findViewById(R.id.tv_close_time);
            tvCloseRate=(TextView)itemView.findViewById(R.id.close_rate);
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
        TextView tvSwap;
        TextView tvOpenRate;
        TextView tvCloseTime;
        TextView tvCloseRate;
    }
}